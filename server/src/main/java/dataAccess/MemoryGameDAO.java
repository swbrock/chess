package dataAccess;

import chess.ChessGame;

import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    HashMap<Integer, ChessGame> games = new HashMap<>();

    public void createGame(int gameID, ChessGame game) throws DataAccessException {
        try {
            games.put(gameID, game);
        } catch (Exception e) {
            throw new DataAccessException("Game could not be created");
        }

    }

    public ChessGame getGame(int gameID) throws DataAccessException {
        try {
            return games.get(gameID);
        } catch (Exception e) {
            throw new DataAccessException("Game not found");
        }
    }

    public List<ChessGame> listGames() throws DataAccessException {
        try {
            return (List<ChessGame>) games.values();
        } catch (Exception e) {
            throw new DataAccessException("Games not found");
        }
    }

    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
        try {
            games.replace(gameID, game);
        } catch (Exception e) {
            throw new DataAccessException("Game could not be updated");
        }
    }

    public void clearGames() throws DataAccessException {
        try {
            games.clear();
        } catch (Exception e) {
            throw new DataAccessException("Games could not be cleared");
        }
    }
}
