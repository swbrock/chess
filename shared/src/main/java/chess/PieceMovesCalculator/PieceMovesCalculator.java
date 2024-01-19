package chess.PieceMovesCalculator;

import java.util.Collection;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessPosition;
public interface PieceMovesCalculator {
    //TODO: create the neccesary methods
    public void calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets);
    public void calculateMoves(ChessPiece piece);
    public Collection<? extends ChessMove> getMoves();

}
