package client;

import chess.ChessGame;
import com.google.gson.Gson;

import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.*;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.ServerMessage.ServerMessageType.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson gson = new Gson();
                    var serverMessage = gson.fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION:
                            notificationHandler.handleNotification(serverMessage);
                            break;
                        case ERROR:
                            notificationHandler.handleError(serverMessage);
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
    }

    // @Override
    // public void onMessage(Session session, String message) {
    //     System.out.println("Received message: " + message);
    // }
    

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        notificationHandler.notify("Connection closed: " + closeReason.getReasonPhrase());
    }

    @Override
    public void onError(Session session, Throwable thr) {
        notificationHandler.notifyError(thr.getMessage());
    }

    public void sendCommand(UserGameCommand command) {
        Gson gson = new Gson();
        String json = gson.toJson(command);
        try {
            session.getBasicRemote().sendText(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    ///join game
    public void joinGame(String authToken, int gameId, ChessGame.TeamColor color) {
        JoinPlayer command = new JoinPlayer(authToken, gameId, color);
        sendCommand(command);
    }
}