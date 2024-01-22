package chess.PieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

    public QueenMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> getMoves() {
        return null;
    }

    @Override
    public void calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculatePossibleMoves'");
    }

}