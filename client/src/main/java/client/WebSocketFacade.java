package client;

import chess.ChessGame;

import chess.ChessPiece;
import chess.ChessPosition;
import client.ChessClient;
import com.google.gson.Gson;
import model.GameData;
import ui.DrawBoard;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    private ChessGame.TeamColor playerColor;
    private DrawBoard draw = new DrawBoard();
    private List<GameData> gameDataList = new ArrayList<>();


    public WebSocketFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = new NotificationHandler() {
                @Override
                public void notify(ServerMessage serverMessage, String message) {
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> System.out.println("Notification: " + serverMessage.getMessage());
                        case ERROR -> System.out.println("Error");
                        case LOAD_GAME -> loadGame(message);
                    }
                }
            };

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    System.out.print("\nClient.WebSocketFacade.onMessage called. \nMessageType: " + notification.getServerMessageType().toString() + "\nMessage: " + notification.getMessage() + "\n");
                    notificationHandler.notify(notification, message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(String authToken, String username, int gameID, ChessGame.TeamColor color, GameData gameData) throws Exception {
        try {
            System.out.print("\nClient.WebSocketFacade.joinGame called \n");
            playerColor = color;
            var action = new JoinPlayer(authToken, username, gameID, color, gameData);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void joinObserver(String username, int gameID, GameData game) throws Exception {
        try {
            var action = new JoinObserver(username, gameID, game);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }


    public void loadGame(String message) {
        System.out.print("WSF: Load Game Called");
        LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
        GameData game = loadGame.getGameData();
        String bUsername = game.blackUsername();
        String wUsername = game.whiteUsername();

        ChessGame.TeamColor color = loadGame.getTeamColor();

        draw.displayGame(game, playerColor, null);
    }

    public void leave(String username) throws Exception {
        try {
            var action = new LeaveGame(username);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public void redrawBoard(String authToken, int gameID) throws Exception {
        try {
            var action = new RedrawBoard(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void makeMove(String authToken, ChessPosition startPos, ChessPosition endPos, int gameID, ChessGame.TeamColor color) throws IOException {
        var action = new MakeMove(authToken, startPos, endPos, gameID, color);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
    }
}