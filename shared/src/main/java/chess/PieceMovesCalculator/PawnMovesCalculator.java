package chess.PieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;
import chess.ChessGame;

public class PawnMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

    public PawnMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> getMoves() {
        int[] rowOffsets = {1, 0};
        int[] colOffsets = {0, 0};
        possibleMoves = calculatePossibleMoves(position, rowOffsets, colOffsets);
        return possibleMoves;
    }

    //add a method to see if a pawn can use the en passant move
    public boolean canEnPassant(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        var teamColor = board.getPiece(position).getTeamColor();
        if (teamColor == ChessGame.TeamColor.BLACK) {
            rowOffsets[0] = -1;
        }
        for (int i = 0; i < rowOffsets.length; i++) {
            int rowOffset = rowOffsets[i];
            int colOffset = colOffsets[i];
            int row = currentRow + rowOffset;
            int col = currentCol + colOffset;
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(position);
            if (piece != null) {
                if (piece.getTeamColor() != teamColor) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessPosition currentPosition, int[] rowOffsets, int[] colOffsets) {
        int[] rowOffsets2 = {1, 1};
        int[] colOffsets2 = {1, -1};
        var teamColor = board.getPiece(position).getTeamColor();
        if (teamColor == ChessGame.TeamColor.BLACK) {
            rowOffsets[0] = -1;
            rowOffsets2[0] = -1;
            rowOffsets2[1] = -1;
        }
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        //check to see if pawn is in starting position
        if (teamColor == ChessGame.TeamColor.WHITE) {
            if (currentRow == 2) {
                rowOffsets[1] = 2;
            }
        } else {
            if (currentRow == 7) {
                rowOffsets[1] = -2;
            }
        }
        for (int i = 0; i < rowOffsets.length; i++) {
            int rowOffset = rowOffsets[i];
            int colOffset = colOffsets[i];
            int row = currentRow + rowOffset;
            int col = currentCol + colOffset;
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(position);
            if (piece == null) {
                if (row == 8 || row == 1) {
                    possibleMoves.add(new ChessMove(currentPosition, position, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(currentPosition, position, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(currentPosition, position, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(currentPosition, position, ChessPiece.PieceType.KNIGHT));
                    break;
                } else {
                    possibleMoves.add(new ChessMove(currentPosition, position, null));
                }
            } else {
                break;
            }
        }
        for (int i = 0; i < rowOffsets2.length; i++) {
            int rowOffset = rowOffsets2[i];
            int colOffset = colOffsets2[i];
            int row = currentRow + rowOffset;
            int col = currentCol + colOffset;
            ChessPosition position = new ChessPosition(row, col);
            if (col > 8 || col < 0 || row > 8 || row < 0) {
                continue;
            }
            ChessPiece piece = board.getPiece(position);
            if (piece != null) {
                if (piece.getTeamColor() != teamColor) {
                    if (row == 8 || row == 1) {
                        possibleMoves.add(new ChessMove(currentPosition, position, ChessPiece.PieceType.QUEEN));
                        possibleMoves.add(new ChessMove(currentPosition, position, ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(currentPosition, position, ChessPiece.PieceType.ROOK));
                        possibleMoves.add(new ChessMove(currentPosition, position, ChessPiece.PieceType.KNIGHT));
                        break;
                    } else {
                        possibleMoves.add(new ChessMove(currentPosition, position, null));
                    }
                }
            }
        }
        return possibleMoves;
    }
}