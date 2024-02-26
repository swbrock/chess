package dataAccess;

import chess.ChessGame;

import java.util.List;

public interface GameDAO {
    
        void createGame(int gameID, ChessGame game) throws DataAccessException;
    
        ChessGame getGame(int gameID) throws DataAccessException;
    
        List<ChessGame> listGames() throws DataAccessException;
    
        void updateGame(int gameID, ChessGame game) throws DataAccessException;
    
        void clearGames() throws DataAccessException;
    
}
