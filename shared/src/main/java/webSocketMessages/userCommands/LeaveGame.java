package webSocketMessages.userCommands;

public class LeaveGame extends UserGameCommand{
    //	Integer gameID	Tells the server you are leaving the game so it will stop sending you notifications.
    private int gameID;

    public LeaveGame(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.LEAVE;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public String toString() {
        return "LeaveGame{" +
                "gameID=" + gameID +
                '}';
    }
}
