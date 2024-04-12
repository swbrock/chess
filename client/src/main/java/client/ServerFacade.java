package client;

import chess.ChessGame;
import client.response.GameListResponse;
import client.response.GameResponse;
import client.response.JoinGameResponse;
import client.response.RegisterUserResponse;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import org.glassfish.grizzly.http.util.HttpUtils;

import model.UserData;

import java.io.*;
import java.net.*;
import java.util.List;

public class ServerFacade {
    private final String serverURL;
    public String authToken;

    public ServerFacade(int urlPort) {
        String url = "http://localhost:";
        url += String.valueOf(urlPort);
        this.serverURL = url;
    }

    public RegisterUserResponse register(String username, String password, String email) throws Exception {
        var path = "/user";
        UserData createUser = new UserData(username, password, email);
        makeRequest("POST", path, createUser, UserData.class);
        var signInPath = "/session";
        UserData user = new UserData(username, password, null);
        RegisterUserResponse res = makeRequest("POST", signInPath, user, RegisterUserResponse.class);
        this.authToken = res.getAuthToken();
        return res;
    }

    public String signIn(String username, String password) throws Exception {
        var path = "/session";
        //get the user that matches the username and password
        UserData user = new UserData(username, password, null);
        RegisterUserResponse res = makeRequest("POST", path, user, RegisterUserResponse.class);
        this.authToken = res.getAuthToken();
        return authToken;
    }

    public List<GameData> listGames() throws Exception {
        var path = "/game";
        GameListResponse games = makeRequest("GET", path, null, GameListResponse.class);
        return games.games;
    }

    public void signOut() throws Exception {
        var path = "/session";
        makeRequest("DELETE", path, null, null);
        this.authToken = null;
    }
    public void createGame(String gameName) throws Exception {
        var path = "/game";
        GameData game = new GameData(1, "", "", gameName, new ChessGame());
        makeRequest("POST", path, game, null);
    }
    public GameData joinGame(int gameId, String color) throws Exception {
        var path = "/game";
        JoinGameResponse res = new JoinGameResponse(color.toUpperCase(), gameId);
        return makeRequest("PUT", path, res, GameData.class);
    }
    public GameData observeGame(int gameId) throws Exception {
        var path = "/game";
        GameResponse res = new GameResponse(gameId);
        return makeRequest("PUT", path, res, GameData.class);
    }
    public void clear () throws Exception {
        var path = "/db";
        makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (this.authToken != null) {
                http.addRequestProperty("Authorization", this.authToken);
            } else {
                http.setDoOutput(true);
            }
            if (request != null) {
                http.setDoOutput(true);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception("Not succesfull");
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
