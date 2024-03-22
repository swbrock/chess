package server.response;

import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class GameListResponse {

    private List<GameData> games;

    public GameListResponse(List<GameData> games) { this.games = games;}

}
