package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    private int nextId = 1;
    private int authId = 1;
    private int gameId = 1;

    final private HashMap<Integer, GameData> games = new HashMap<>();
    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        ChessGame newGame = new ChessGame();
        GameData game = new GameData(nextId++, "", "", gameName, newGame);
        games.put(gameId, game);
        gameId++;
        return game;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try {
            return games.get(gameID);
        } catch (Exception e) {
            throw new DataAccessException("Game not found");
        }
    }
    @Override

    public List<ChessGame> listGames() throws DataAccessException {
        return null;
    }

    @Override

    public void clearGames() throws DataAccessException {
        try {
            games.clear();
        } catch (Exception e) {
            throw new DataAccessException("Games could not be cleared");
        }
    }
}
