package client;
import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;
import client.Repl;

public class ChessClient {

    private final String serverUrl;
    private ServerFacade server;

    public ChessClient(String serverURL) {
        this.serverUrl = serverURL;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "start" -> {
                    start();
                    yield "Connected to " + serverUrl;
                }
                case "stop" -> {
                    stop();
                    yield "Disconnected from " + serverUrl;
                }
                case "signin" -> {
                    var username = params[0];
                    var password = params[1];
                    yield signIn(username, password);
                }
                case "signout" -> {
                    var authToken = params[0];
                    signOut(authToken);
                    yield "Signed out";
                }
                case "listgames" -> {
                    var authToken = params[0];
                    listGames(authToken);
                    yield "Listed games";
                }
                case "creategame" -> {
                    var authToken = params[0];
                    createGame(authToken);
                    yield "Created game";
                }
                case "joingame" -> {
                    var authToken = params[0];
                    var gameId = params[1];
                    joinGame(authToken, gameId);
                    yield "Joined game";
                }
                case "move" -> {
                    var authToken = params[0];
                    var gameId = params[1];
                    var move = params[2];
                    move(authToken, gameId, move);
                    yield "Moved";
                }
                case "resign" -> {
                    var authToken = params[0];
                    var gameId = params[1];
                    resign(authToken, gameId);
                    yield "Resigned";
                }
                case "observegame" -> {
                    var authToken = params[0];
                    var gameId = params[1];
                    observeGame(authToken, gameId);
                    yield "Observed game";
                }
                case "unobservegame" -> {
                    var authToken = params[0];
                    var gameId = params[1];
                    unobserveGame(authToken, gameId);
                    yield "Unobserved game";
                }
                case "register" -> {
                    var username = params[0];
                    var password = params[1];
                    var email = params[2];
                    yield register(username, password, email);
                }
                default -> "Unknown command";
            };
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String signIn(String username, String password) {
        return "authToken";
    }

    public void start() {
        System.out.println("Connecting to " + serverUrl);
    }

    public void stop() {
        System.out.println("Disconnected from " + serverUrl);
    }

    public void signOut(String authToken) {
    }

    public void listGames(String authToken) {
    }

    public void createGame(String authToken) {
    }

    public void joinGame(String authToken, String gameId) {
    }

    public void move(String authToken, String gameId, String move) {
    }

    public void resign(String authToken, String gameId) {
    }

    public void observeGame(String authToken, String gameId) {
    }

    public void unobserveGame(String authToken, String gameId) {
    }

    public String register(String username, String password, String email) {
        server.register(username, password, email);
        return "User Logged In";
    }
}
