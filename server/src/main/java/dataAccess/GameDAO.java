package dataAccess;

import model.GameData;

import java.util.List;

public interface GameDAO {

        GameData getGame(int gameID) throws DataAccessException;
    
        List<GameData> listGames() throws DataAccessException;
    
        void updateGame(int gameID, GameData game) throws DataAccessException;
    
        void clearGames() throws DataAccessException;

        int createGame(String gameName) throws DataAccessException;
}
