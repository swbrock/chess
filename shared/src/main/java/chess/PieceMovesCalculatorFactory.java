package chess;

import chess.ChessPiece.PieceType;
import chess.PieceMovesCalculator.BishopMovesCalculator;
import chess.PieceMovesCalculator.KingMovesCalculator;
import chess.PieceMovesCalculator.KnightMovesCalculator;
import chess.PieceMovesCalculator.PawnMovesCalculator;
import chess.PieceMovesCalculator.PieceMovesCalculator;
import chess.PieceMovesCalculator.QueenMovesCalculator;
import chess.PieceMovesCalculator.RookMovesCalculator;

public class PieceMovesCalculatorFactory {

    public static PieceMovesCalculator getCalculator(PieceType type, ChessBoard board, ChessPosition myPosition) {
        switch (type) {
        case BISHOP:
            return new BishopMovesCalculator(board, myPosition);
        case KING:
            return new KingMovesCalculator(board, myPosition);
        case KNIGHT:
            return new KnightMovesCalculator(board, myPosition);
        case PAWN:
            return new PawnMovesCalculator(board, myPosition);
        case QUEEN:
            return new QueenMovesCalculator(board, myPosition);
        case ROOK:
            return new RookMovesCalculator(board, myPosition);
        default:
            throw new RuntimeException("Unknown piece type: " + type);
        }
    }

}
