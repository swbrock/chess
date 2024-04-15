package ui;

import chess.*;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;

import static ui.EscapeSequences.*;

public class DrawBoard {

    private Collection<ChessMove> validMoves = null;
    HashSet<ChessPosition> highlightSquares = null;
    ChessPosition pos;
    public DrawBoard(){

    }

    public void displayGame(GameData gameData, ChessGame.TeamColor playerColor, ChessPosition pos){


        if (pos != null) {
            this.pos = pos;
            validMoves = gameData.game().validMoves(pos);
            highlightSquares = getValidPositions();
        }
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var output = "\n" + gameData.gameName() + ":\n";
        String wUsername = gameData.whiteUsername();
        String bUsername = gameData.blackUsername();
        if (wUsername == null){
            wUsername = "No player added";
        }
        if (bUsername == null){
            bUsername = "No player added";
        }
        output += "White Player: " + wUsername + "\n";
        output += "Black Player: " + bUsername + "\n";
        if (gameData.game().getTeamTurn() == ChessGame.TeamColor.WHITE){
            output += wUsername + "'s turn to move";
        }
        else {
            output += bUsername + "'s turn to move";
        }
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(output);
        out.print("\n");
        if (playerColor == ChessGame.TeamColor.BLACK){
            drawBoardBlack(out, gameData.game());
            //generateBoard(gameData.game(), false, out);
        }
        else {
            drawBoardWhite(out, gameData.game());
            //generateBoard(gameData.game(), true, out);
        }

    }



    private void drawBoardWhite(PrintStream out, ChessGame game) {
        writeLetters(out);
        for (int j = 1; j < 9; j++){
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(j);
            for (int i = 1; i < 9; i++) {
                ChessPosition current = new ChessPosition(j, i);
                if (current.equals(pos)){
                    doMainPieceWhite(out, game, j, i);
                }
                else if (highlightSquares != null && highlightSquares.contains(new ChessPosition(j, i))){
                    doHighlightWhite(out, game, j, i);
                }
                else {
                    if ((j % 2) != 0){
                        if ((i % 2) == 0){
                            setYellow(out);
                        }
                        else {
                            setBrown(out);
                        }
                        evalBoard(out, game, j, i);
                    }
                    else {
                        if ((i % 2) == 0){
                            setBrown(out);
                        }
                        else {
                            setYellow(out);
                        }
                        evalBoard(out, game, j, i);
                    }
                }
            }
            out.print(RESET_BG_COLOR);
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(j);
            out.print("\n");
        }
        writeLetters(out);
    }
    private static void writeLetters(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print("  A ");
        out.print(" B ");
        out.print(" C ");
        out.print(" D ");
        out.print(" E ");
        out.print(" F ");
        out.print(" G ");
        out.print(" H \n");
    }


    private void doMainPieceBlack(PrintStream out, ChessGame game, int j, int i) {
        setNeonYellow(out);
        evalBoard(out, game, j, i);
    }

    private void doMainPieceWhite(PrintStream out, ChessGame game, int j, int i) {
        setBlue(out);
        evalBoard(out, game, j, i);
    }

    private void doHighlightWhite(PrintStream out, ChessGame game, int j, int i) {
        if ((j % 2) != 0){
            if ((i % 2) == 0){
                setGreen(out);
            }
            else {
                setDarkGreen(out);
            }
            evalBoard(out, game, j, i);
        }
        else {
            if ((i % 2) == 0){
                setDarkGreen(out);
            }
            else {
                setGreen(out);
            }
            evalBoard(out, game, j, i);
        }
    }

    private void doHighlightBlack(PrintStream out, ChessGame game, int j, int i) {
        if ((j % 2) != 0){
            if ((i % 2) == 0){
                setGreen(out);
            }
            else {
                setDarkGreen(out);
            }
            evalBoard(out, game, 9-j, 9-i);
        }
        else {
            if ((i % 2) == 0) {
                setDarkGreen(out);
            } else {
                setGreen(out);
            }
            evalBoard(out, game, 9 - j, 9 - i);
        }
    }




    private void drawBoardBlack(PrintStream out, ChessGame game) {
        out.print(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print("  H ");
        out.print(" G ");
        out.print(" F ");
        out.print(" E ");
        out.print(" D ");
        out.print(" C ");
        out.print(" B ");
        out.print(" A \n");
        for (int j = 1; j < 9; j++){
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(9-j);
            for (int i = 1; i < 9; i++) {
                ChessPosition current = new ChessPosition(9-j, 9-i);
                if (current.equals(pos)){
                    doMainPieceBlack(out, game, 9-j, 9-i);
                }
                else if (highlightSquares != null && highlightSquares.contains(new ChessPosition(9-j, 9-i))){
                    doHighlightBlack(out, game, 9-j, 9-i);
                }
                else {
                    if ((j % 2) != 0){
                        if ((i % 2) == 0){
                            setNeonYellow(out);
                        }
                        else {
                            setBrown(out);
                        }
                        evalBoard(out, game, 9-j, 9-i);
                    }
                    else {
                        if ((i % 2) == 0){
                            setBrown(out);
                        }
                        else {
                            setNeonYellow(out);
                        }
                        evalBoard(out, game, 9-j, 9-i);
                    }
                }

            }
            out.print(RESET_BG_COLOR);
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(9-j);
            out.print("\n");
        }
        out.print(SET_TEXT_COLOR_WHITE);
        out.print("  H ");
        out.print(" G ");
        out.print(" F ");
        out.print(" E ");
        out.print(" D ");
        out.print(" C ");
        out.print(" B ");
        out.print(" A ");
        out.print("\n");
    }



    public static void evalBoard(PrintStream out, ChessGame game, int row, int col){
        ChessBoard board = game.getBoard();
        ChessPosition pos = new ChessPosition(row, col);
        if (board.getPiece(pos) != null){
            ChessPiece piece = board.getPiece(pos);
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_BLACK);
            }
            else {
                out.print(SET_TEXT_COLOR_WHITE);
            }
            out.print(" " + getPieceType(piece) + " ");
        }
        else {
            out.print("   ");
        }
    }


    private HashSet<ChessPosition> getValidPositions() {
        HashSet<ChessPosition> highlightMoves = new HashSet<>();
        for (ChessMove move: validMoves){
            highlightMoves.add(move.getEndPosition());
        }
        return highlightMoves;
    }



    public static String getPieceType(ChessPiece piece){
        return switch (piece.getPieceType()) {
            case QUEEN -> "Q";
            case KING -> "K";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
    }

    private static void setBrown(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setYellow(PrintStream out) {
        out.print(SET_BG_COLOR_MAGENTA);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setGreen(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setDarkGreen(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setNeonYellow(PrintStream out) {
        out.print(SET_BG_COLOR_MAGENTA);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
    }



    private String[][] generateBoard(ChessGame chessGame, boolean whiteAtBottom, PrintStream out) {
        String[][] board = new String[8][8];
        writeLetters(out);

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