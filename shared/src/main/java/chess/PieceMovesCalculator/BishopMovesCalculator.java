package chess.PieceMovesCalculator;

import java.util.Collection;
import java.util.HashSet;


import chess.*;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();


    public BishopMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> getMoves() {
        int[] rowOffsets = { 1, 1, -1, -1 };
        int[] colOffsets = { 1, -1, 1, -1 };
        possibleMoves = calculatePossibleMoves(position, rowOffsets, colOffsets);
        return possibleMoves;

    }

    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        var currentPiece = board.getPiece(currentPosition);
        var teamColor = currentPiece.getTeamColor();
        for (int i = 0; i < rowOffsets.length; i++) {
            int rowOffset = rowOffsets[i];
            int colOffset = colOffsets[i];
            int row = currentRow + rowOffset;
            int col = currentCol + colOffset;
            if (row > 8 || col > 8 || row < 1 || col < 1) {
                continue;
            }
            while (row >= 1 && row < 9 && col >= 1 && col < 9) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece == null) {
                    possibleMoves.add(new ChessMove(currentPosition, position, null));
                } else {
                    if (piece.getTeamColor() != teamColor) {
                        possibleMoves.add(new ChessMove(currentPosition, position, null));
                        //piece is captured
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