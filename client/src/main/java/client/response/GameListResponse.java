package client.response;

import model.GameData;

import java.util.List;

public class GameListResponse {

    public List<GameData> games;

    public GameListResponse(List<GameData> games) { this.games = games;}

}
