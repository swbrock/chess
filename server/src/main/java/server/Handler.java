package server;

import com.google.gson.Gson;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.RegistrationService;
import service.UserService;
import spark.Response;
import service.GameService;
import spark.Request;
import spark.Service;

import javax.xml.crypto.Data;

public class Handler {
    private final GameService gameService;
    private final UserService userService;
    private final RegistrationService registrationService;

    public Handler() {
        gameService = new GameService();
        userService = new UserService();
        registrationService = new RegistrationService();
    }
    public Object clear(Request req, Response res) throws DataAccessException {
        gameService.clearGames();
        userService.clearUsers();
        registrationService.clearAuth();
        res.status(200);
        return new Gson().toJson("Worked");
    }

    //clear: A method for clearing all data from the database. This is used during testing.
    public Object clearGames(Request req, Response res) throws DataAccessException {
        gameService.clearGames();
        res.status(200);
        return new Gson().toJson(null);
    }
    public Object createGame(Request req, Response res) throws DataAccessException {
        var game = new Gson().fromJson(req.body(), GameData.class);
        gameService.createGame(game.gameName());
        res.status(200);
        return new Gson().toJson(null);
    }

    //createUser: Create a new user.

    public Object createUser(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        userService.createUser(user);
        res.status(200);
        return new Gson().toJson(null);
    }

    public Object loginUser(Request req, Response res) throws DataAccessException {

        return null;
    }

    public Object logoutUser(Request req, Response res) throws DataAccessException {
        return null;

    }

    public Object listGames(Request req, Response res) throws DataAccessException {
        return null;

    }

    public Object joinGame(Request req, Response res) throws DataAccessException {
        

    }

    public <T extends Exception> void exceptionHandler(T ex, Request req, Response res) {

    }


    //getUser: Retrieve a user with the given username.
    //getGame: Retrieve a specified game with the given game ID.
    //listGames: Retrieve all games.
    //updateGame: Updates a chess game. It should replace the chess game string corresponding to a given gameID. This is used when players join a game or when a move is made.
    //createAuth: Create a new authorization.
    //getAuth: Retrieve an authorization given an authToken.
    //deleteAuth: Delete an authorization so that it is no longer valid.
}
