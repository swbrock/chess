package chess;

import java.util.Collection;

import chess.ChessPiece.PieceType;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;


    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //check to see what piece is at the start position
        //if no piece, return null
        //if piece, get valid moves for that piece
        
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        //get all possible moves for the piece
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        //check to see if the move puts the king in check
        checkForKingInCheck(moves, piece, startPosition);
        return moves;
    }

    public ChessPosition kingPosition(ChessGame.TeamColor teamColor) {
        ChessPosition king = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (board.getPiece(new ChessPosition(i, j)) != null) {
                    if (board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor
                            && board.getPiece(new ChessPosition(i, j)).getPieceType() == PieceType.KING) {
                        king = new ChessPosition(i, j);
                        break;
                    }
                }
            } if (king != null) {
                break;
            }
        }
        return king;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //check if the move is valid
        //if not, throw InvalidMoveException
        //if valid, make the move
        //change the turn to the other team
        ChessPiece piece = board.getPiece(move.getStartPosition());
        ChessPiece oldPiece = board.getPiece(move.getEndPosition());

        if (!validMoves(move.getStartPosition()).contains(move) || getTeamTurn() != piece.getTeamColor()) {
            throw new InvalidMoveException("Invalid move");
        } else {
            //need to check if the move puts the king in check
            //if so, throw InvalidMoveException
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);
            if (isInCheck(piece.getTeamColor())) {
                resetMove(move.getStartPosition(), move.getEndPosition(), piece, oldPiece);
                throw new InvalidMoveException("Invalid move");
            }
            if (move.getPromotionPiece() != null) {
                board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            }
            if (teamTurn == TeamColor.WHITE) {
                teamTurn = TeamColor.BLACK;
            } else {
                teamTurn = TeamColor.WHITE;
            }
        }
    }

    public void resetMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece piece, ChessPiece oldPiece) {
        board.addPiece(startPosition, piece);
        if (oldPiece != null) {
            board.addPiece(endPosition, oldPiece);
        } else {
            board.addPiece(endPosition, null);
        }
    }

    //this method will check the possible moves of a piece to see if the move puts their king in check
    public void checkForKingInCheck(Collection<ChessMove> moves, ChessPiece piece, ChessPosition startPosition) {
        //cant use a for each loop here because we are modifying the collection
        var length = moves.size();
        var iterator = moves.iterator();
        for (int i = 0; i < length; i++) {
            ChessMove move = iterator.next();
            ChessPiece oldPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(startPosition, null);
            if (isInCheck(piece.getTeamColor())) {
                iterator.remove();
            }
            resetMove(startPosition, move.getEndPosition(), piece, oldPiece);
        }
    }

    public void getPromotionPiece(ChessMove move, ChessPiece.PieceType promotionPiece) {
        if (move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8) {
            move.getPromotionPiece();
        }
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //get the position of the king of the team
        //check if any of the other team's pieces can move to that position
        //if so, return true
        //otherwise, return false
        ChessPosition kingPosition = kingPosition(teamColor);
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (board.getPiece(new ChessPosition(i, j)) != null) {
                    if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                        Collection<ChessMove> moves = board.getPiece(new ChessPosition(i, j)).pieceMoves(board,
                                new ChessPosition(i, j));
                        for (ChessMove move : moves) {
                            if (move.getEndPosition().equals(kingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = kingPosition(teamColor);
        if(isInCheck(teamColor)) {
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        if (board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor
                                && board.getPiece(new ChessPosition(i, j)).getPieceType() == PieceType.KING) {
                            kingPosition = new ChessPosition(i, j);
                        }
                    }
                }
            }
            //get all possible moves for the king
            Collection<ChessMove> kingMoves = board.getPiece(kingPosition).pieceMoves(board, kingPosition);
            //if the king can move to a position that is not in check, return false
            for (ChessMove move : kingMoves) {
                if (!isInCheck(teamColor)) {
                    return false;
                }
            }
            //if the king can't move, check if any other piece can move to block the check
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        if (board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                            Collection<ChessMove> moves = board.getPiece(new ChessPosition(i, j)).pieceMoves(board,
                                    new ChessPosition(i, j));
                            for (ChessMove move : moves) {
                                ChessPiece piece = board.getPiece(move.getEndPosition());
                                board.addPiece(move.getEndPosition(), board.getPiece(new ChessPosition(i, j)));
                                board.addPiece(new ChessPosition(i, j), null);
                                if (!isInCheck(teamColor)) {
                                    board.addPiece(new ChessPosition(i, j), board.getPiece(move.getEndPosition()));
                                    board.addPiece(move.getEndPosition(), piece);
                                    return false;
                                }
                                board.addPiece(new ChessPosition(i, j), board.getPiece(move.getEndPosition()));
                                board.addPiece(move.getEndPosition(), piece);
                            }
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        //get the kings position and see if it cant move anywhere and then check to see if its not in check.
        ChessPosition kingPosition = kingPosition(teamColor);
        //get all possible moves for the king
        Collection<ChessMove> kingMoves = board.getPiece(kingPosition).pieceMoves(board, kingPosition);
        //if the king can move to a position that is not in check, return false
        for (ChessMove move : kingMoves) {
            ChessPiece oldPiece = board.getPiece(move.getEndPosition());
            ChessPiece piece = board.getPiece(kingPosition);
            ChessPosition newPosition = move.getEndPosition();
            board.addPiece(move.getEndPosition(), board.getPiece(kingPosition));
            board.addPiece(kingPosition, null);
            if (!isInCheck(teamColor)) {
                return false;
            }
            resetMove(kingPosition, newPosition, piece, oldPiece);
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
