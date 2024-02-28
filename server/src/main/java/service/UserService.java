package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    final UserDAO userDAO;

    public UserService() {
        userDAO = new MemoryUserDAO();
    }

    public void clearUsers() throws DataAccessException {
        userDAO.clearUsers();
    }
    public void createUser(UserData user) throws DataAccessException {
        userDAO.createUser(user);
    }
    public UserData loginUser(UserData user) throws DataAccessException {
        try {
            UserData checkUser = userDAO.getUser(user.username());
            if (checkUser == null) {
                return null;
            }
            if (!checkUser.password().equals(user.password())) {
                return null;
            }
            return userDAO.getUser(user.username());
        } catch (DataAccessException e) {
            return null;
        }

    }
}