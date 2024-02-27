package dataAccess;

import java.util.HashMap;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    HashMap<String, String> auths = new HashMap<>();

    @Override
    public AuthData checkToken(AuthData auth) throws DataAccessException {
//        String password = auths.get(username);
//        if (password == null) {
//            throw new DataAccessException("User not found");
//        }
//        return new AuthData(username, password);
        return null;
    }

    @Override
    public AuthData insertToken(AuthData auth) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData removeUser(AuthData auth) throws DataAccessException {
        return null;
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
        return 0;
    }

    //helper method in my MemoryAuthDAO class that returns a boolean for if the auth database contains a given authToken. Then you can easily check if an authToken is valid or not
    public boolean containsAuth(String authToken) {
        return auths.containsKey(authToken);
    }
}
