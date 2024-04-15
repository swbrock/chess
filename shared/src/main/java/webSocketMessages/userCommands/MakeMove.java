package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class MakeMove extends UserGameCommand {


    private ChessMove move;
    private int gameID;
    public MakeMove(String authToken, ChessMove move, int gameID) {
        super(authToken);
        this.move = move;
        this.gameID = gameID;
        this.commandType = CommandType.MAKE_MOVE;
    }

    public ChessMove getMove() {
        return this.move;
    }

    public int getGameID() {
        return gameID;
    }

}