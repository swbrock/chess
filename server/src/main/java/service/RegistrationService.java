package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import model.AuthData;

import javax.xml.crypto.Data;

public class RegistrationService {
    private static AuthDAO authDAO;

    public RegistrationService() {
        try {
            authDAO = new SQLAuthDAO();
        } catch (Exception ignored) {

        }
    }

    public void clearAuth() throws DataAccessException {
        authDAO.clearAuths();
    }
    //insert token
    public AuthData createAuth(String username) throws DataAccessException {
        return authDAO.createAuth(username);
    }
    public void logoutUser(String authToken) throws DataAccessException {
        authDAO.deleteAuth(authToken);
    }
}
