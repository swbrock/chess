package dataAccess;

import java.util.HashMap;
import java.util.UUID;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    static HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public Boolean getAuth(String authToken) throws DataAccessException {
        return auths.get(authToken) != null;
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        auths.put(authToken, auth);
        return auth;
    }


    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try {
            AuthData auth = auths.get(authToken);
            auths.remove(auth.authToken(), auth);
        } catch (Exception e) {
            throw new DataAccessException("User could not be deleted");
        }
    }

    @Override

    public void clearAuths() throws DataAccessException {
        try {
            auths.clear();
        } catch (Exception e) {
            throw new DataAccessException("Users could not be cleared");
        }
    }

    @Override
    public int numberOfAuths() {
        return auths.size();
    }

    //helper method in my MemoryAuthDAO class that returns a boolean for if the auth database contains a given authToken. Then you can easily check if an authToken is valid or not
    public boolean containsAuth(String authToken) {
        return auths.containsKey(authToken);
    }

    public AuthData findAuthDataByUsername(String username) {
        for (AuthData authData : auths.values()) {
            if (authData.username().equals(username)) {
                return authData;
            }
        }
        return null; // Or throw an exception indicating authToken not found
    }
    public AuthData findAuthDataByAuthToken(String authToken) {
        for (AuthData authData : auths.values()) {
            if (authData.authToken().equals(authToken)) {
                return authData;
            }
        }
        return null; // Or throw an exception indicating authToken not found
    }

}
