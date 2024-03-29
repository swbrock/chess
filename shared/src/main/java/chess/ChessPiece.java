package chess;

import java.util.Collection;
import java.util.Objects;

import chess.PieceMovesCalculator.PieceMovesCalculator;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator calculator = PieceMovesCalculatorFactory.getCalculator(this.type, board, myPosition);
        Collection<ChessMove> moves = calculator.getMoves();
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        if (getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (this.type == PieceType.KING){
                return "K";
            } else if (this.type == PieceType.KNIGHT) {
                return "N";
            } else if (this.type == PieceType.BISHOP) {
                return "B";
            } else if (this.type == PieceType.QUEEN) {
                return "Q";
            } else if (this.type == PieceType.ROOK) {
                return "R";
            } else {
                return "P";
            } 
        } else {
            if (this.type == PieceType.KING){
                return "k";
            } else if (this.type == PieceType.KNIGHT) {
                return "n";
            } else if (this.type == PieceType.BISHOP) {
                return "b";
            } else if (this.type == PieceType.QUEEN) {
                return "q";
            } else if (this.type == PieceType.ROOK) {
                return "r";
            } else if (this.type == PieceType.PAWN) {
                return "p";
            } else {
                return " ";
            }
        }
    }
}
