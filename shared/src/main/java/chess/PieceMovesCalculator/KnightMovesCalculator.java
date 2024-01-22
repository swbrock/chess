package chess.PieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

    public KnightMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> getMoves() {
        int[] rowOffsets = {1, 2, 1, 2, -1, -2, -1, -2};
        int[] colOffsets = {2, 1, -2, -1, 2, 1, -2, -1};
        possibleMoves = calculatePossibleMoves(position, rowOffsets, colOffsets);
        return possibleMoves;
    }

    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        for (int i = 0; i < rowOffsets.length; i++) {
            int rowOffset = rowOffsets[i];
            int colOffset = colOffsets[i];
            int row = currentRow + rowOffset;
            int col = currentCol + colOffset;
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(position);
            if (piece == null) {
                possibleMoves.add(new ChessMove(currentPosition, position, null));
            } else {
                if (piece.getTeamColor() != currentPosition.getTeamColor()) {
                    possibleMoves.add(new ChessMove(currentPosition, position, null));
                }
                break;
            }
        }
        return possibleMoves;
    }
}