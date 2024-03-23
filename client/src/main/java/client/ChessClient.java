package client;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;
import client.Repl;
import model.GameData;

public class ChessClient {

    private final String serverUrl;
    public final ServerFacade server;
    private State state;

    public ChessClient(String serverURL, State state) {
        this.serverUrl = serverURL;
        this.server = new ServerFacade(serverURL);
        this.state = state;
    }
    public void printSignedOutMenu() {
        System.out.println("1. Sign in");
        System.out.println("2. Sign up");
        System.out.println("3. Help");
        System.out.println("exit");
    }

    public void printSignedInMenu() {
        System.out.println("1. List games");
        System.out.println("2. Create game");
        System.out.println("3. Join game");
        System.out.println("4. Join observer");
        System.out.println("5. Sign out");
        System.out.println("6. Help");
        System.out.println("exit");
    }

    public String evalSignedOut(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "1", "signin" -> {
                    System.out.println("Enter username:");
                    Scanner scanner = new Scanner(System.in);
                    String username = scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();
                    yield signIn(username, password);
                }
                case "2", "signup", "register" -> {
                    System.out.println("Enter username:");
                    Scanner scanner = new Scanner(System.in);
                    String newUsername = scanner.nextLine();
                    System.out.println("Enter password:");
                    String newPassword = scanner.nextLine();
                    System.out.println("Enter email:");
                    String newEmail = scanner.nextLine();
                    yield register(newUsername, newPassword, newEmail);
                }
                case "3", "help" -> {
                    System.out.println("Help: ");
                    System.out.println("1. Sign in: Sign in to your account");
                    System.out.println("2. Sign up: Create a new account");
                    System.out.println("3. Help: Display this help message");
                    System.out.println("exit: Exit the program");
                    yield "Help";
                }
                case "start" -> {
                    start();
                    yield "Connected to " + serverUrl;
                }
                case "stop" -> {
                    stop();
                    yield "Disconnected from " + serverUrl;
                }
                default -> "Unknown command";
            };
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String evalSignedIn (String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signout", "5" -> {
                    signOut();
                    yield "Signed out";
                }
                case "listgames", "1" -> {
                    listGames();
                    yield "List of Games";
                }
                case "creategame", "2" -> {
                    System.out.println("Enter Game Name:");
                    Scanner scanner = new Scanner(System.in);
                    String gameName = scanner.nextLine();
                    createGame(gameName);
                    yield "Created game";
                }
                case "joingame", "3" -> {
                    System.out.println("Enter Game Number:");
                    Scanner scanner = new Scanner(System.in);
                    var id = scanner.nextLine();
                    System.out.println("Enter Color:");
                    var color = scanner.nextLine();
                    joinGame(parseInt(id), color);
                    yield "Joined game";
                }
                case "observegame", "4" -> {
                    //observeGame();
                    yield "Observed game";
                }
                case "help", "6" -> {
                    System.out.println("Help: ");
                    System.out.println("1. List games: List all games");
                    System.out.println("2. Create game: Create a new game");
                    System.out.println("3. Join game: Join a game");
                    System.out.println("4. Join observer: Join a game as an observer");
                    System.out.println("5. Sign out: Sign out of your account");
                    System.out.println("6. Help: Display this help message");
                    System.out.println("exit: Exit the program");
                    yield "Help";
                }
                default -> "Unknown command";
            };
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    public String signIn(String username, String password) {
        try {
            server.signIn(username, password);
            System.out.println("Succesfully signed in! Welcome " + username);
        } catch (Exception e){
            System.out.println("Unsuccesfull in signing in " + username + ". Error: " + e.getMessage());
        }
        printSignedInMenu();
        return "";
    }

    public void start() {
        System.out.println("Connecting to " + serverUrl);
    }

    public void stop() {
        System.out.println("Disconnected from " + serverUrl);
    }

    public void signOut() {
        try {
            server.signOut();
            System.out.println("Succesfully signed out!");
        } catch (Exception e){
            System.out.println("Unsuccesfull in signing out. Error: " + e.getMessage());
        }
        this.state = State.SIGNEDOUT;
        printSignedOutMenu();
    }

    public void listGames() {
        //list games in ordered list with game name and usernames
        try {
            List<GameData> games = server.listGames();
            var id = 1;
            System.out.println("Games");
            for (GameData game : games) {
                System.out.println(id + ". " + game.gameName() + " - White Username: " + game.whiteUsername() + " - Black Username: " + game.blackUsername());
                id += 1;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
    }

    public void createGame(String gameName) throws Exception {
        server.createGame(gameName);
    }

    public void joinGame(Number gameId, String color) throws Exception {
        //loop through the games and join the game on the number given
        int id = 1;
        List<GameData> games = server.listGames();
        for (GameData game : games) {
            if (id == gameId.intValue()) {
                server.joinGame(game.gameID(), color);
                System.out.println("Joined game " + game.gameName());
                return;
            }
            id += 1;
        }
    }


    public void observeGame(String authToken, String gameId) {
    }

    public String register(String username, String password, String email) {
        try {
            server.register(username, password, email);
            System.out.println("Succesfully added! Welcome " + username);
        } catch (Exception e){
            System.out.println("Unsuccesfull in adding " + username + ". Error: " + e.getMessage());
        }
        return "Registered";
    }
}
