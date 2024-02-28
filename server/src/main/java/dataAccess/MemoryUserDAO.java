package dataAccess;

import java.util.HashMap;

import chess.ChessGame;
import model.GameData;
import model.UserData;

public class MemoryUserDAO implements UserDAO {


    static HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData createUser(UserData newUser) throws DataAccessException {
        if (users.containsKey(newUser.username())) {
            throw new DataAccessException("User already exists");
        }
        users.put(newUser.username(), newUser);
        return newUser;
    }


    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData user = users.get(username);
        if (user == null) {
            throw new DataAccessException("User not found");
        }
        return user;
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        try {
            UserData user = users.get(username);
            users.remove(user.username(), user);
        } catch (Exception e) {
            throw new DataAccessException("User could not be deleted");
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