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

    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        var currentPiece = board.getPiece(currentPosition);
        var teamColor = currentPiece.getTeamColor();

        for(int i = 0; i < rowOffsets.length; i++) {
            int rowOffset = rowOffsets[i];
            int colOffset = colOffsets[i];
            int row = currentRow + rowOffset;
            int col = currentCol + colOffset;
            if (row > 8 || col > 8 || row < 1 || col < 1) {
                continue;
            }
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(position);
            if (piece == null) {
                possibleMoves.add(new ChessMove(currentPosition, position, null));
            } else {
                if (piece.getTeamColor() != teamColor) {
                    possibleMoves.add(new ChessMove(currentPosition, position, null));
                }
            }
        }
        return possibleMoves;
    }
}