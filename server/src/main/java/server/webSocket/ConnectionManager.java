
package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session) {
        var connection = new Connection(visitorName, session);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void sendMessage(String recipient, ServerMessage message) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.visitorName.equals(recipient)) {
                    c.send(new Gson().toJson(message));
                }
            }
        }
    }
    public void broadcast(String excludeVisitorName, ServerMessage message) throws IOException {



        var removeList = new ArrayList<Connection>();
        if (message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            for (var c : connections.values()) {
                if (c.session.isOpen()) {
                    if (!c.visitorName.equals(excludeVisitorName)) {

                        c.send(new Gson().toJson(message));
                    }
                } else {
                    removeList.add(c);
                }
            }
        }
        else {

            for (var c : connections.values()) {
                if (c.session.isOpen()) {
                    if (!c.visitorName.equals(excludeVisitorName)) {
                        c.send(new Gson().toJson(message));
                    }
                } else {
                    removeList.add(c);
                }
            }
            // Clean up any connections that were left open.
            for (var c : removeList) {
                connections.remove(c.visitorName);
            }
        }

    }
}