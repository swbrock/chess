package service;

import dataAccess.*;
import model.UserData;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        userDAO = new MemoryUserDAO();
    }

    public void clearUsers() throws DataAccessException {
        userDAO.clearUsers();
    }
    public void createUser(UserData user) throws DataAccessException {
        userDAO.createUser(user);
    }


}