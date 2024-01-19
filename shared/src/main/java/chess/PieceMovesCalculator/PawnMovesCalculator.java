package chess.PieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;

    public PawnMovesCalculator(ChessBoard board, ChessPosition position) {
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