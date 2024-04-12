package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    //Integer gameID	Forfeits the match and ends the game (no more moves can be made).
    private int gameID;

    public Resign(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.RESIGN;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public String toString() {
        return "Resign{" +
                "gameID=" + gameID +
                '}';
    }

}
