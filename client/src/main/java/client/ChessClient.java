package client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import ui.DrawBoard;
import ui.EscapeSequences;

public class ChessClient {

    private final String serverUrl;
    public final ServerFacade server;
    public State state;
    private List<GameData> gameDataList = new ArrayList<>();
    private WebSocketFacade ws;
    private int gameID;
    private String authToken;
    ChessGame.TeamColor playerColor;
    private DrawBoard draw;

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
                    joinGame(parseInt(id), color);
                    //displayGame(game);
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE); // Clear the screen
                    printInGameMenu();
                    yield "Joined Game";
                }
                case "observegame", "4" -> {
                    System.out.println("Enter Game Number:");
                    Scanner scanner = new Scanner(System.in);
                    var id = scanner.nextLine();
                    GameData game = observeGame(parseInt(id));
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
                    redrawChessBoard();
                    yield "Redrawing Chess Board";
                }
                case "leave", "2" -> {
                    leaveGame();
                    yield "Leaving Game";
                }
                case "makemove", "3" -> {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter Starting Position:");
                    String start = scanner.nextLine();
                    System.out.println("Enter End Position:");
                    String end = scanner.nextLine();
                    yield makeMove(start, end);
                }
                case "resign", "4" -> {
                    System.out.println("Are you sure you want to resign? (yes/no)");
                    Scanner scanner = new Scanner(System.in);
                    String resignInput = scanner.nextLine();
                    if (resignInput.equalsIgnoreCase("yes")) {
                        resign();
                        yield "Resigned";
                    } else {
                        yield "Did not resign";
                    }
                }
                case "highlightlegalmoves", "5" -> {
                    System.out.println("Enter Piece Position:");
                    Scanner scanner = new Scanner(System.in);
                    String piece = scanner.nextLine();
                    highlightLegalMoves(piece);
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
            this.authToken = server.signIn(username, password);
            this.state = State.SIGNEDIN;
            this.gameDataList = server.listGames();
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
            this.gameDataList = games;
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

    public void joinGame(Integer gameIndex, String playerColor) throws Exception {
            int id = 1;
            if (id > gameDataList.size()){
                throw new Exception("Game doesn't exist");
            }
            this.gameID = gameDataList.get(gameIndex - 1).gameID();
            playerColor = playerColor.toUpperCase();
            ChessGame.TeamColor teamColor = convertColor(playerColor);
            this.playerColor = teamColor;
            server.joinGame(this.gameID, playerColor);

            ws = new WebSocketFacade(serverUrl);
            ws.joinGame(authToken, this.gameID, teamColor);
            state = State.INGAME;
    }


    public GameData observeGame(Integer gameId) throws Exception {
        //loop through the games and join the game on the number given
        int id = 1;
        List<GameData> games = server.listGames();
        for (GameData game : games) {
            if (id == gameId) {
                GameData chessGame = server.observeGame(game.gameID());
                System.out.println("Observing game " + game.gameName());
                this.state = State.INGAME;
                WebSocketFacade ws = new WebSocketFacade(serverUrl);
                this.gameID = id;
                //observe game websocket
                ws.joinObserver(this.authToken, this.gameID);
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

    public void redrawChessBoard() throws Exception {
        ws.redrawBoard(this.authToken, this.gameID);
    }

    public void leaveGame() throws Exception {
        state = State.SIGNEDIN;
        ws = new WebSocketFacade(serverUrl);
        ws.leave(authToken, gameID);
        this.playerColor = null;
        this.gameID = 0;
    }

    public String makeMove(String start, String end) throws Exception {
        System.out.print("Making Move");
            String startPosString = start;
            char columnChar = startPosString.charAt(0);
            String rowChar = String.valueOf(startPosString.charAt(1));
            int startRow = Integer.parseInt(rowChar);
            int startCol = letterToNumber(columnChar);
            ChessPosition startPos = new ChessPosition(startRow, startCol);

            String endPosString = end;
            columnChar = endPosString.charAt(0);
            rowChar = String.valueOf(endPosString.charAt(1));
            int endRow = Integer.parseInt(rowChar);
            int endCol = letterToNumber(columnChar);
            ChessPosition endPos = new ChessPosition(endRow, endCol);
            ChessMove move = new ChessMove(startPos, endPos, null);
            ws.makeMove(this.authToken, move, this.gameID);
        return "";
    }

    public int letterToNumber(char letter) throws Exception {
        if (letter == 'A' || letter == 'a'){
            return 1;
        }
        else if (letter == 'B' || letter == 'b'){
            return 2;
        }
        else if (letter == 'C' || letter == 'c'){
            return 3;
        }
        else if (letter == 'D' || letter == 'd'){
            return 4;
        }
        else if (letter == 'E' || letter == 'e'){
            return 5;
        }
        else if (letter == 'F' || letter == 'f'){
            return 6;
        }
        else if (letter == 'G' || letter == 'g'){
            return 7;
        }
        else if (letter == 'H' || letter == 'h'){
            return 8;
        }
        else {
            throw new Exception("Error: expected makemove <(A-H)(1-8)> <(A-H)(1-8)>");
        }
    }

    public void resign() throws IOException {
        ws.resign(authToken, gameID);
    }

    public void highlightLegalMoves(String piece) throws Exception {
        System.out.print("\nHighlighting legal moves");
        String posString = piece;
        char columnChar = posString.charAt(0);
        String rowChar = String.valueOf(posString.charAt(1));
        int row = Integer.parseInt(rowChar);
        int col = letterToNumber(columnChar);
        ChessPosition pos = new ChessPosition(row,col);
        updateGame();
        GameData gameData = null;
        for (int i = 0; i < gameDataList.size(); i++){
            if(gameDataList.get(i).gameID() == gameID) {
                gameData = gameDataList.get(i);
            }
        }
        draw = new DrawBoard();
        draw.displayGame(gameData, playerColor, pos);
    }

    private void updateGameList(Collection<GameData> gameList) throws Exception {
        gameDataList.clear();
        for (GameData gameData : gameList) {
            this.gameDataList.add(gameData);
        }
    }


    private void updateGame() throws Exception {
        this.gameDataList.clear();
        Collection<GameData> newGameList = server.listGames();
        for (GameData game: newGameList) {
            gameDataList.add(game);
        }

    }

    private void displayGameList(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("Listing Games: \n");
        for (int i = 1; i < gameDataList.size()+1; i++){
            GameData gameData = gameDataList.get(i-1);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            System.out.print(EscapeSequences.SET_TEXT_BOLD);
            System.out.print("  Game: " + i + "\n");
            System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.print("      Game Name: " +  gameData.gameName() + "\n");
            String wUsername = gameData.whiteUsername();
            String bUsername = gameData.blackUsername();
            if (wUsername == null){
                wUsername = "No player added";
            }
            if (bUsername == null){
                bUsername = "No player added";
            }
            System.out.print("      White Player: " +  wUsername + "\n");
            System.out.print("      Black Player: " +  bUsername + "\n");
        }
    }


    private ChessGame.TeamColor convertColor(String color) throws Exception{
        if (color == null || color == "" || color == "blank"){
            throw new Exception("HTTP Request needs to be made first");
        }
        else if (color.equals("WHITE")){
            return ChessGame.TeamColor.WHITE;
        }
        else if (color.equals("BLACK")) {
            return ChessGame.TeamColor.BLACK;
        }
        else {
            throw new Exception("Invalid Team Color");
        }
    }
}