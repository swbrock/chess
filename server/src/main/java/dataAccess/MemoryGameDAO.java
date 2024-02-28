package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    static int nextId = 1;
    static int gameId = 1;

    static HashMap<Integer, GameData> games = new HashMap<>();
    @Override
    public int createGame(String gameName) throws DataAccessException {
        ChessGame newGame = new ChessGame();
        newGame.getBoard().resetBoard();
        GameData game = new GameData(nextId++, null, null, gameName, newGame);
        games.put(gameId, game);
        gameId++;
        return gameId - 1;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try {
            return games.get(gameID);
        } catch (Exception e) {
            throw new DataAccessException("Error: bad request");
        }
    }
    @Override

    public List<GameData> listGames() throws DataAccessException {
        try {
            // Convert the values of the map to a list
            return new ArrayList<>(games.values());
        } catch (Exception e) {
            throw new DataAccessException("Games could not be listed"); // Include the original exception for better error handling
        }
    }


    @Override

    public void clearGames() throws DataAccessException {
        try {
            games.clear();
        } catch (Exception e) {
            throw new DataAccessException("Games could not be cleared");
        }
    }

    @Override
    public void updateGame(int id, GameData game) throws DataAccessException {
        try {
            games.replace(id, game);
        } catch (Exception e) {
            throw new DataAccessException("Game could not be updated");
        }
    }
}
