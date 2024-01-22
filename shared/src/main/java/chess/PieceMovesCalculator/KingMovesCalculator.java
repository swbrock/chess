package chess.PieceMovesCalculator;

import java.util.Collection;
import java.util.HashSet;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessGame;

public class KingMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

    public KingMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> getMoves() {
        int[] rowOffsets = { 1, 1, 1, 0, 0, -1, -1, -1 };
        int[] colOffsets = { 1, 0, -1, 1, -1, 1, 0, -1 };
        possibleMoves = calculatePossibleMoves(position, rowOffsets, colOffsets);
        return possibleMoves;
    }

    //create function to check if king is in check
    public boolean isInCheck(ChessPosition currentPosition) {
    //     //check to see if any of the opponent's pieces can move to the king's position
    //     //if so, return true
    //     //else, return false
    //     if (currentPosition.getTeamColor() == ChessGame.TeamColor.BLACK) {
    //     for (ChessPiece piece : board.getBlackPieces()) {
    //         if (piece.getMoves().contains(currentPosition)) {
    //             return true;
    //         }
    //     } else {
    //         for (ChessPiece piece : board.getWhitePieces()) {
    //             if (piece.getMoves().contains(currentPosition)) {
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }
    return false;
    }

    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        for(int i = 0; i < rowOffsets.length; i++) {
            int rowOffset = rowOffsets[i];
            int colOffset = colOffsets[i];
            int row = currentRow + rowOffset;
            int col = currentCol + colOffset;
            while (row >= 0 && row < 8 && col >= 0 && col < 8) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece == null) {
                    if (isInCheck(position)) {
                        break;
                    }
                    possibleMoves.add(new ChessMove(currentPosition, position, null));
                } else {
                    if (piece.getTeamColor() != currentPosition.getTeamColor()) {
                        possibleMoves.add(new ChessMove(currentPosition, position, null));
                    }
                    break;
                }
                row += rowOffset;
                col += colOffset;
            }
        }
        return possibleMoves;
    }
}