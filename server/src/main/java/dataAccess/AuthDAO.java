package dataAccess;

import model.AuthData;

public interface AuthDAO {
            AuthData insertToken(AuthData auth) throws DataAccessException;
        
            AuthData checkToken(AuthData auth) throws DataAccessException;
        
            AuthData removeUser(AuthData auth) throws DataAccessException;
        

            void clearAuths() throws DataAccessException;
            int numberOfAuths();
}
