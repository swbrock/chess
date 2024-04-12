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

    public ChessClient(int urlPort, State state) {
        String url = "http://localhost:";
        url += String.valueOf(urlPort);
        this.serverUrl = url;
        this.server = new ServerFacade(urlPort);
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

    public void printInGameMenu() {
        System.out.println("1. Redraw Chess Board");
        System.out.println("2. Leave");
        System.out.println("3. Make Move");
        System.out.println("4. Resign");
        System.out.println("5. Highlight Legal Moves");
        System.out.println("6. Help");
        System.out.println("exit");
    }

    public String evalSignedOut(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
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
                    displayGame(game);
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE); // Clear the screen
                    yield "Joined Game";
                }
                case "observegame", "4" -> {
                    System.out.println("Enter Game Number:");
                    Scanner scanner = new Scanner(System.in);
                    var id = scanner.nextLine();
                    GameData game = observeGame(parseInt(id));
                    displayGame(game);
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE); // Clear the screen
                    yield "Observing Game";
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

    public String evalInGame(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "help" -> {
                    System.out.println("Help: ");
                    System.out.println("1. Redraw Chess Board: Redraws the chess board upon the user’s request.");
                    System.out.println("2. Leave: Removes the user from the game (whether they are playing or observing the game). The client transitions back to the Post-Login UI.");
                    System.out.println("3. Make Move: Allow the user to input what move they want to make. The board is updated to reflect the result of the move, and the board automatically updates on all clients involved in the game.");
                    System.out.println("4. Resign: Prompts the user to confirm they want to resign. If they do, the user forfeits the game and the game is over. Does not cause the user to leave the game.");
                    System.out.println("5. Highlight Legal Moves: Allows the user to input what piece for which they want to highlight legal moves. The selected piece’s current square and all squares it can legally move to are highlighted. This is a local operation and has no effect on remote users’ screens.");
                    System.out.println("exit: Exit the program");
                    yield "Help";
                }
                case "redrawchessboard", "1" -> {
                    System.out.println("Redrawing Chess Board");
                    yield "Redrawing Chess Board";
                }
                case "leave", "2" -> {
                    System.out.println("Leaving Game");
                    yield "Leaving Game";
                }
                case "makemove", "3" -> {
                    System.out.println("Enter Move:");
                    Scanner scanner = new Scanner(System.in);
                    String move = scanner.nextLine();
                    yield "Made Move: " + move;
                }
                case "resign", "4" -> {
                    System.out.println("Are you sure you want to resign? (yes/no)");
                    Scanner scanner = new Scanner(System.in);
                    String resign = scanner.nextLine();
                    if (resign.equals("yes")) {
                        yield "Resigned";
                    } else {
                        yield "Did not resign";
                    }
                }
                case "highlightlegalmoves", "5" -> {
                    System.out.println("Enter Piece:");
                    Scanner scanner = new Scanner(System.in);
                    String piece = scanner.nextLine();
                    yield "Highlighting Legal Moves for: " + piece;

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
                this.state = State.INGAME;
                WebSocketFacade ws = new WebSocketFacade(serverUrl);
                //joingame websocket
                ws.joinGame(server.authToken, game.gameID(), ChessGame.TeamColor.valueOf(color.toUpperCase()));
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
                this.state = State.INGAME; //maybe make an observe game function
                return chessGame;
            }
            id += 1;
        }
        return null;
    }

    public String register(String username, String password, String email) throws Exception {
        try {
            server.register(username, password, email);
            this.state = State.SIGNEDIN;
            printSignedInMenu();
            return "Succesfully added! Welcome " + username + "\n";
        } catch (Exception e) {
            return "Unsuccesfull in adding " + username + ". Error: " + e.getMessage();
        }
    }

    public void displayGame(GameData game) {
        ChessGame chessGame = game.game();
        System.out.println("White: " + game.whiteUsername() + "\nBlack: " + game.blackUsername() +"\n");
        printBoard(generateBoard(chessGame, true));
        System.out.println("\nOther Board");
        printBoard(generateBoard(chessGame, false));

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

    // public static void main(String[] args) {
    //     var port = 8080;
    //     var state = State.SIGNEDOUT;
    //     var client = new ChessClient(port, state);
    //     client.printSignedOutMenu();
    //     Scanner scanner = new Scanner(System.in);
    //     while (true) {
    //         String input = scanner.nextLine();
    //         if (client.state == State.SIGNEDOUT) {
    //             if (input.equals("exit")) {
    //                 break;
    //             }
    //             System.out.println(client.evalSignedOut(input));
    //         } else if (client.state == State.SIGNEDIN) {
    //             if (input.equals("exit")) {
    //                 break;
    //             }
    //             System.out.println(client.evalSignedIn(input));
    //         } else if (client.state == State.INGAME) {
    //             if (input.equals("exit")) {
    //                 break;
    //             }
    //             System.out.println(client.evalInGame(input));
    //         }
    //     }
    // }
}