package service;

import java.util.List;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import model.GameData;

public class GameService {
    private final GameDAO gameDAO;

    public GameService() {
        gameDAO = new MemoryGameDAO();
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