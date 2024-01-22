package chess.PieceMovesCalculator;

import java.util.Collection;

import chess.ChessMove;
import chess.ChessPosition;

public interface PieceMovesCalculator {
    //TODO: create the neccesary methods
    public Collection<ChessMove> calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets);
    public Collection<ChessMove> getMoves();
}
