package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import model.AuthData;
public interface AuthDAO {
            AuthData createAuth(String username) throws DataAccessException;
        
            Boolean getAuth(String authToken) throws DataAccessException;
        
            void deleteAuth(String authToken) throws DataAccessException;
            void clearAuths() throws DataAccessException;
            int numberOfAuths() throws DataAccessException;
            AuthData findAuthDataByAuthToken(String authToken) throws DataAccessException;
            AuthData findAuthDataByUsername(String username) throws DataAccessException;

    }
