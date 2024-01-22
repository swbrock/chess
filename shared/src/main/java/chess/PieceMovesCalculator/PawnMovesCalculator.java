package chess.PieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;
import chess.ChessGame;

public class PawnMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

    public PawnMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> getMoves() {
        int[] rowOffsets = { 0, 0};
        int[] colOffsets = { 1, 0};
        possibleMoves = calculatePossibleMoves(position, rowOffsets, colOffsets);
        return possibleMoves;
    }

    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets) {
        int[] rowOffsets2 = { 1, -1};
        int[] colOffsets2 = { 1, 1};
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        //check to see if pawn is in starting position
        var teamColor = currentPosition.getTeamColor();
        if (teamColor == ChessGame.TeamColor.WHITE) {
            if (currentRow == 1) {
                rowOffsets[1] = 0;
                colOffsets[1] = 2;
            }
        } else {
            if (currentRow == 6) {
                rowOffsets[1] = 0;
                colOffsets[1] = 2;
            }
        }
        for (int i = 0; i < rowOffsets.length; i++) {
            int rowOffset = rowOffsets[i];
            int colOffset = colOffsets[i];
            int row = currentRow + rowOffset;
            int col = currentCol + colOffset;
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(position);
            if (piece == null) {
                possibleMoves.add(new ChessMove(currentPosition, position, null));
            } 
        }
        for (int i = 0; i < rowOffsets2.length; i++) {
            int rowOffset = rowOffsets2[i];
            int colOffset = colOffsets2[i];
            int row = currentRow + rowOffset;
            int col = currentCol + colOffset;
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(position);
            if (piece != null) {
                if (piece.getTeamColor() != currentPosition.getTeamColor()) {
                    possibleMoves.add(new ChessMove(currentPosition, position, null));
                }
            }
        }
        return possibleMoves;
    }
}