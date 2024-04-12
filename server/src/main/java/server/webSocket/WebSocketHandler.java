package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;


public class WebSocketHandler {
    private static WebSocketHandler instance;
    private Session session;

    private WebSocketHandler() {
    }

    public static WebSocketHandler getInstance() {
        if (instance == null) {
            instance = new WebSocketHandler();
        }
        return instance;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void sendMessage(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        try {
            session.getRemote().sendString(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        session.close();

    }
}
