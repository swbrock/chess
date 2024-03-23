package client.response;

public class JoinGameResponse {
    private String playerColor;
    private int gameID;

    public JoinGameResponse(String playerColor, int ID){
        this.playerColor = playerColor;
        this.gameID = ID;
    }

}
