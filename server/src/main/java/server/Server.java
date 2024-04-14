package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import service.GameService;
import server.webSocket.WebSocketHandler;
import spark.*;

public class Server {

    private Handler handler;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        handler = new Handler();
        webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.webSocket("/connect", webSocketHandler);
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) -> handler.clear(req, res));
        Spark.post("/game", (req, res) -> handler.createGame(req, res));
        Spark.post("/user", (req, res) -> handler.createUser(req, res));
        Spark.post("/session", (req, res) -> handler.loginUser(req, res));
        Spark.delete("/session", (req, res) -> handler.logoutUser(req, res));
        Spark.get("/game", (req, res) -> handler.listGames(req, res));
        Spark.put("/game", (req, res) -> handler.joinGame(req, res));
        //Spark.exception(ResponseException.class, (ex, req, res) -> handler.exceptionHandler(ex, req, res));

        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
        
}