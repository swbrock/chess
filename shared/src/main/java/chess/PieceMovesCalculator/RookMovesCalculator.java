package chess.PieceMovesCalculator;
import java.util.Collection;
import java.util.HashSet;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;


public class RookMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

    public RookMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> getMoves() {
        int[] rowOffSets = {1, -1, 0, 0};
        int[] colOffSets = {0, 0, 1, -1};
        possibleMoves = calculatePossibleMoves(position, rowOffSets, colOffSets);
        return possibleMoves;
    }

    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        var teamColor = board.getPiece(position).getTeamColor();
        for (int i = 0; i < rowOffsets.length; i++) {
            int rowOffset = rowOffsets[i];
            int colOffset = colOffsets[i];
            int row = rowOffset + currentRow;
            int col = colOffset + currentCol;
            if (row > 8 || col > 8 || row < 1 || col < 1) {
                continue;
            }
            while (row >= 1 && row < 9 && col >= 1 && col < 9) {
                ChessPosition chessPosition = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(chessPosition);
                if (piece == null) {
                    possibleMoves.add(new ChessMove(currentPosition, chessPosition, null));
                } else {
                    if (piece.getTeamColor() != teamColor) {
                        possibleMoves.add(new ChessMove(currentPosition, chessPosition, null));
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