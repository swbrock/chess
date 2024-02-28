package server.response;

import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameListResponse {

    private Collection<GameData> games;

    public GameListResponse(Collection<GameData> games) { this.games = games;}

}
