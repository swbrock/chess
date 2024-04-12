package client;

import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    
    public WebSocketFacade() {
        try {
            URI uri = new URI("ws://localhost:8080/chess");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, uri);
        } catch (URISyntaxException | DeploymentException | IOException e) {
            e.printStackTrace();
}
        }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
    }

    public void send(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
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

    // public void addMessageHandler(MessageHandler messageHandler) {
    //     session.addMessageHandler(messageHandler);
    // }

    public interface MessageHandler {
        void handleMessage(String message);
    }

}

// Path: client/src/main/java/client/WebSocketFacade.java

