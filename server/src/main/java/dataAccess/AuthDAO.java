package dataAccess;

public interface AuthDAO {
            void createAuth(String username, String password) throws DataAccessException;
        
            String getAuth(String username) throws DataAccessException;
        
            void deleteAuth(String username) throws DataAccessException;
        
            void updateAuth(String username, String password) throws DataAccessException;
        
            void clearAuths() throws DataAccessException;
}
