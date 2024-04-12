package webSocketMessages.userCommands;

public class MakeMove extends UserGameCommand{
    //MAKE_MOVE	Integer gameID, ChessMove move	Used to request to make a move in a game.
    private int gameID;

    public MakeMove(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.MAKE_MOVE;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public String toString() {
        return "MakeMove{" +
                "gameID=" + gameID +
                '}';
    }
}
