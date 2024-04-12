package webSocketMessages.userCommands;

import chess.ChessGame;
import model.GameData;

public class JoinObserver extends UserGameCommand{
    
    //JOIN_OBSERVER	Integer gameID	Used to request to start observing a game.
    private int gameID;
    private GameData game;

    public JoinObserver(String authToken, int gameID, GameData gameData) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
        this.game = gameData;
    }

    public int getGameID() {
        return gameID;
    }

    public GameData getGame() {return game; }
}