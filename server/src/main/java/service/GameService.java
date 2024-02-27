package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;

public class GameService {
    private final GameDAO gameDAO;

    public GameService() {
        gameDAO = new MemoryGameDAO();
    }

    public void clearGames() throws DataAccessException {
        gameDAO.clearGames();
    }
    public void createGame(String gameName) throws DataAccessException {
        gameDAO.createGame(gameName);
    }


}