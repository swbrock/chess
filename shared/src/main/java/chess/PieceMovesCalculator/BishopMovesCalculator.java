package chess.PieceMovesCalculator;

import java.util.Collection;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;

    public BishopMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> getMoves() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void calculateMoves(ChessPiece piece) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculateMoves'");
    }

    @Override
    public void calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculatePossibleMoves'");
    }
}