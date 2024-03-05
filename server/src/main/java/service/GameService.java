package service;

import java.util.List;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.SQLGameDAO;
import model.GameData;

public class GameService {
    private static GameDAO gameDAO;

    public GameService() {
        try {
            gameDAO = new SQLGameDAO();
        } catch (Exception ignored) {

        }
    }

    public void clearGames() throws DataAccessException {
        gameDAO.clearGames();
    }
    public int createGame(String gameName) throws DataAccessException {
        return gameDAO.createGame(gameName);
    }
    //list games
    public List<GameData> listGames() throws DataAccessException {
        return gameDAO.listGames();
    }
    //update game
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        gameDAO.updateGame(gameID, game);
    }

    public GameData getGameById(int gameID) throws DataAccessException {
        return gameDAO.getGame(gameID);
    }
}