package client;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;
import ui.EscapeSequences;

public class ChessClient {

    private final String serverUrl;
    public final ServerFacade server;
    public State state;

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

    public String evalSignedIn(String input) {
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
                    GameData game = joinGame(parseInt(id), color);
                    yield displayGame(game);
                }
                case "observegame", "4" -> {
                    System.out.println("Enter Game Number:");
                    Scanner scanner = new Scanner(System.in);
                    var id = scanner.nextLine();
                    GameData game = observeGame(parseInt(id));
                    yield displayGame(game);
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
                default -> "Unknown command, type 'help' for options";
            };
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    public String signIn(String username, String password) {
        try {
            server.signIn(username, password);
            this.state = State.SIGNEDIN;
            printSignedInMenu();
        } catch (Exception e) {
            System.out.println("Unsuccesfull in signing in " + username + ". Error: " + e.getMessage());
            this.state = State.SIGNEDOUT;
            printSignedOutMenu();
        }
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
            this.state = State.SIGNEDOUT;
            printSignedOutMenu();
        } catch (Exception e) {
            System.out.println("Unsuccesfull in signing out. Error: " + e.getMessage());
            this.state = State.SIGNEDIN;
            printSignedInMenu();
        }
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

    public GameData joinGame(Integer gameId, String color) throws Exception {
        //loop through the games and join the game on the number given
        int id = 1;
        List<GameData> games = server.listGames();
        for (GameData game : games) {
            if (id == gameId) {
                GameData chessGame = server.joinGame(game.gameID(), color);
                System.out.println("Joined game " + game.gameName());
                return chessGame;
            }
            id += 1;
        }
        return null;
    }


    public GameData observeGame(Integer gameId) throws Exception {
        int id = 1;
        List<GameData> games = server.listGames();
        for (GameData game : games) {
            if (id == gameId) {
                GameData chessGame = server.observeGame(game.gameID());
                System.out.println("Observing game " + game.gameName());
                return chessGame;
            }
            id += 1;
        }
        return null;
    }

    public String register(String username, String password, String email) throws Exception {
        try {
            server.register(username, password, email);
            return "Succesfully added! Welcome " + username;
        } catch (Exception e) {
            return "Unsuccesfull in adding " + username + ". Error: " + e.getMessage();
        }
    }

    public String displayGame(GameData game) {
        ChessGame chessGame = game.game();
        System.out.println("White: " + game.whiteUsername() + "\nBlack: " + game.blackUsername() +"\n");
        printBoard(generateBoard(chessGame, true));
        System.out.println("\nOther Board");
        printBoard(generateBoard(chessGame, false));
        return "Chess Game";
    }

    private String[][] generateBoard(ChessGame chessGame, boolean whiteAtBottom) {
        String[][] board = new String[8][8];

        //need to display the pieces on the board

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = EscapeSequences.EMPTY;
            }
        }

        // Place pieces on the board based on the ChessGame object
        ChessPiece[][] pieces = chessGame.getBoard().getSquares();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = pieces[i][j];
                if (piece != null) {
                    String symbol = getPieceSymbol(piece);
                    if (whiteAtBottom) {
                        board[8 - i][j - 1] = symbol; // White pieces at the bottom
                    } else {
                        board[i - 1][8 - j] = symbol; // Black pieces at the bottom
                    }
                }
            }
        }

        return board;
    }

    private void printBoard(String[][] board) {
        System.out.print(EscapeSequences.ERASE_SCREEN); // Clear the screen
        System.out.print(EscapeSequences.moveCursorToLocation(1, 1)); // Move cursor to top-left corner

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Add color to pieces based on the background
                String bgColor = ((i + j) % 2 == 0) ? EscapeSequences.SET_BG_COLOR_MAGENTA : EscapeSequences.SET_BG_COLOR_BLUE;
                String coloredSquare = bgColor + board[i][j] + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR;
                System.out.print(coloredSquare);
            }
            System.out.println(); // Move to the next line after printing each row
        }
    }
    private String getPieceSymbol(ChessPiece piece) {
        String symbol = "";
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            symbol = switch (piece.getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        } else {
            symbol = switch (piece.getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        }
        return symbol;
    }
}