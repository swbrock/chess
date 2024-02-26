package dataAccess;

import java.util.HashMap;

import model.UserData;

public class MemoryUserDAO implements UserDAO {

    HashMap<String, UserData> users = new HashMap<>();

    public void createUser(UserData u) throws DataAccessException {
        //create a new user in the database
        //if the user already exists, throw an error
        //create a unique id for the user
        
        try {
            users.putIfAbsent(u.username(), u);
        } catch (Exception e) {

            throw new DataAccessException("Error creating user");
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        UserData user = users.get(username);
        if (user == null) {
            throw new DataAccessException("User not found");
        }
        return user;
    }

    public void deleteUser(String username) throws DataAccessException {
        try {
            UserData user = users.get(username);
            users.remove(username, user);
        } catch (Exception e) {
            throw new DataAccessException("User could not be deleted");
        }
    }

    public void updateUser(UserData u) throws DataAccessException {
        try {
            UserData oldUser = users.get(u.username());
            users.replace(oldUser.username(), u);
        } catch (Exception e) {
            throw new DataAccessException("User could not be updated");
        }
    }

    public void clearUsers() throws DataAccessException {
        try {
            users.clear();
        } catch (Exception e) {
            throw new DataAccessException("Users could not be cleared");
        }
    }

}