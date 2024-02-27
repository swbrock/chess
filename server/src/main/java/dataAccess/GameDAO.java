package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public interface GameDAO {

        GameData getGame(int gameID) throws DataAccessException;
    
        List<ChessGame> listGames() throws DataAccessException;
    
        //void updateGame(int gameID, ChessGame game) throws DataAccessException;
    
        void clearGames() throws DataAccessException;

        GameData createGame(String gameName) throws DataAccessException;
}
