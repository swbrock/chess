package server;

import com.google.gson.Gson;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import server.response.ErrorResponse;
import server.response.GameListResponse;
import server.response.GameResponse;
import service.RegistrationService;
import service.UserService;
import spark.Response;
import service.GameService;
import spark.Request;
import spark.Service;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        try {
            gameService.clearGames();
            userService.clearUsers();
            registrationService.clearAuth();
            res.status(200);
            return "{}";
        } catch (Exception e) {
            res.status(500);
            ErrorResponse response = new ErrorResponse("Error: " + e.getMessage());
            return new Gson().toJson(response);
        }
    }

    //clear: A method for clearing all data from the database. This is used during testing.
    public Object createGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("Authorization");
        Boolean isValid = new SQLAuthDAO().getAuth(authToken);
        if (!isValid) {
            res.status(401);
            ErrorResponse response = new ErrorResponse("Error: unauthorized");
            return new Gson().toJson(response);
        } else {
            var game = new Gson().fromJson(req.body(), GameData.class);
            try {
                int gameID = gameService.createGame(game.gameName());
                res.status(200);
                GameResponse response = new GameResponse(gameID);
                return new Gson().toJson(response);
            } catch (DataAccessException e) {
                if (e.getMessage().equals("bad request")) {
                    res.status(400);
                    ErrorResponse response = new ErrorResponse("Error: bad request");
                    return new Gson().toJson(response);
                }
                if (e.getMessage().equals("Unauthorized")) {
                    res.status(401);
                    ErrorResponse response = new ErrorResponse("Error: unauthorized");
                    return new Gson().toJson(response);
                }
                res.status(500);
                ErrorResponse response = new ErrorResponse("Error: " + e.getMessage());
                return new Gson().toJson(response);
            }
        }
    }

    //createUser: Create a new user.

    public Object createUser(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        if (user.username() == null || user.password() == null) {
            res.status(400);
            ErrorResponse response = new ErrorResponse("Error: bad request");
            return new Gson().toJson(response);
        }
        AuthData auth = registrationService.createAuth(user.username());
        try {
            userService.createUser(user);
            res.status(200);
            //AuthResponse response = new AuthResponse(auth);
            return new Gson().toJson(auth);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("bad request")) {
                res.status(400);
                ErrorResponse response = new ErrorResponse("Error: bad request");
                return new Gson().toJson(response);
            }
            if (e.getMessage().equals("User already exists")) {
                res.status(403);
                ErrorResponse response = new ErrorResponse("Error: already taken");
                return new Gson().toJson(response);
            }
            res.status(500);
            ErrorResponse response = new ErrorResponse("Error: " + e.getMessage());
            return new Gson().toJson(response);
        }
    }

    public Object loginUser(Request req, Response res) throws DataAccessException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            if (user.password() == null || user.username() == null) {
                res.status(400);
                ErrorResponse response = new ErrorResponse("Error: bad request");
                return new Gson().toJson(response);
            }
            if (userService.loginUser(user) == null) {
                res.status(401);
                ErrorResponse response = new ErrorResponse("Error: unauthorized");
                return new Gson().toJson(response);
            }

            AuthData auth = registrationService.createAuth(user.username());
            res.status(200);
            return new Gson().toJson(auth);
        } catch (DataAccessException e) {
            res.status(500);
            ErrorResponse response = new ErrorResponse("Error: " + e.getMessage());
            return new Gson().toJson(response);
        }
    }

    public Object logoutUser(Request req, Response res) throws DataAccessException {
        try {
            String authToken = req.headers("Authorization");
            Boolean isValid = new SQLAuthDAO().getAuth(authToken);
            if (isValid) {
                registrationService.logoutUser(authToken);
                res.status(200);
                return "{}";
            } else {
                res.status(401);
                ErrorResponse response = new ErrorResponse("Error: unauthorized");
                return new Gson().toJson(response);
            }
        } catch (DataAccessException e) {
            res.status(500);
            ErrorResponse response = new ErrorResponse("Error: " + e.getMessage());
            return new Gson().toJson(response);
        }
    }


    public Object listGames(Request req, Response res) {
        try {
            // Check if authToken is provided
            String authToken = req.headers("Authorization");
            Boolean isValid = new SQLAuthDAO().getAuth(authToken);
            if (authToken == null || !isValid) {
                res.status(401);
                ErrorResponse response = new ErrorResponse("Error: unauthorized");
                return new Gson().toJson(response);
            }
            res.status(200);
            List<GameData> gameList = gameService.listGames();

            GameListResponse games = new GameListResponse(gameList);
            return new Gson().toJson(games);

        } catch (DataAccessException e) {
            res.status(500);
            ErrorResponse response = new ErrorResponse("Error: " + e.getMessage());
            return new Gson().toJson(response);
        }
    }

    public Object joinGame(Request req, Response res) throws DataAccessException {
        try {
            // Extract authToken from headers
            String authToken = req.headers("Authorization");
            Boolean isValid = new SQLAuthDAO().getAuth(authToken);

            // Check if authToken is valid
            if (!isValid) {
                res.status(401);
                ErrorResponse response = new ErrorResponse("Error: unauthorized");
                return new Gson().toJson(response);
            }
            AuthData auth = new SQLAuthDAO().findAuthDataByAuthToken(authToken);

            // Parse request body
            Map<String, Object> request = new Gson().fromJson(req.body(), Map.class);
            String playerColor = (String) request.get("playerColor");
            if (request.get("gameID") == null) {
                res.status(400);
                ErrorResponse response = new ErrorResponse("Error: bad request");
                return new Gson().toJson(response);
            }
            double id = (double) request.get("gameID");
            int gameID = (int) id;
            GameData game = gameService.getGameById(gameID);
            if (game == null) {
                res.status(400);
                ErrorResponse response = new ErrorResponse("Error: bad request");
                return new Gson().toJson(response);
            }


            if (Objects.equals(playerColor, "BLACK")) {
                if (Objects.equals(game.blackUsername(), null)) {
                    GameData newGame = new GameData(gameID, game.whiteUsername(), auth.username(), game.gameName(), game.game());
                    gameService.updateGame(gameID, newGame);
                    res.status(200);
                    return "{}";
                } else {
                    //throw error - already a black player
                    res.status(403);
                    ErrorResponse response = new ErrorResponse("Error: already taken");
                    return new Gson().toJson(response);
                }
            } else if (Objects.equals(playerColor, "WHITE")) {
                if (Objects.equals(game.whiteUsername(), null)) {
                    GameData newGame = new GameData(gameID, auth.username(), game.blackUsername(), game.gameName(), game.game());
                    gameService.updateGame(gameID, newGame);
                    res.status(200);
                    return"{}";
                } else {
                    res.status(403);
                    ErrorResponse response = new ErrorResponse("Error: already taken");
                    return new Gson().toJson(response);
                }
            } else {
                res.status(200);
                return "{}";
                //add observer
            }
        } catch (DataAccessException e) {
            res.status(500);
            ErrorResponse response = new ErrorResponse("Error: " + e.getMessage());
            return new Gson().toJson(response);
        }
    }
}