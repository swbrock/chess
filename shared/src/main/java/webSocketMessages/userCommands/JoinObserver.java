package webSocketMessages.userCommands;

import model.GameData;

public class JoinObserver extends UserGameCommand{

    private int gameID;

    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }

    public int getGameID() {
        return gameID;
    }

}