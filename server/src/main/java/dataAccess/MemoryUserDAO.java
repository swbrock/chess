package dataAccess;

import java.util.HashMap;

import chess.ChessGame;
import model.GameData;
import model.UserData;

public class MemoryUserDAO implements UserDAO {
    private int nextId = 1;
    private int authId = 1;
    private int userId = 1;


    HashMap<Integer, UserData> users = new HashMap<>();

    @Override
    public UserData createUser(UserData newUser) throws DataAccessException {
        users.put(userId, newUser);
        userId++;
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
            users.remove(username, user);
        } catch (Exception e) {
            throw new DataAccessException("User could not be deleted");
        }
    }

    @Override
    public void updateUser(UserData u) throws DataAccessException {

    }

    public void clearUsers() throws DataAccessException {
        try {
            users.clear();
        } catch (Exception e) {
            throw new DataAccessException("Users could not be cleared");
        }
    }

}