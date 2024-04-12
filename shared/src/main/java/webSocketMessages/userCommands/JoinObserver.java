package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinObserver extends UserGameCommand{
    
    //JOIN_OBSERVER	Integer gameID	Used to request to start observing a game.
    private int gameID;

    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public String toString() {
        return "JoinObserver{" +
                "gameID=" + gameID +
                '}';
    }
}