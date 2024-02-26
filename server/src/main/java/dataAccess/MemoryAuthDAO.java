package dataAccess;

import java.util.HashMap;

public class MemoryAuthDAO {

    HashMap<String, String> auths = new HashMap<>();


    public void createAuth(String username, String password) throws DataAccessException {
        //create a new user in the database
        try {
            auths.put(username, password);
        } catch (Exception e) {

            throw new DataAccessException("Error creating user");
        }
    }

    public String getAuth(String username) throws DataAccessException {
        String auth = auths.get(username);
        if (auth == null) {
            throw new DataAccessException("User not found");
        }
        return auth;
    }

    public void deleteAuth(String username) throws DataAccessException {
        try {
            auths.remove(username);
        } catch (Exception e) {
            throw new DataAccessException("User could not be deleted");
        }
    }

    public void updateAuth(String username, String password) throws DataAccessException {
        try {
            auths.replace(username, password);
        } catch (Exception e) {
            throw new DataAccessException("User could not be updated");
        }
    }

    public void clearAuths() throws DataAccessException {
        try {
            auths.clear();
        } catch (Exception e) {
            throw new DataAccessException("Users could not be cleared");
        }
    }
}
