package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import model.AuthData;

import javax.xml.crypto.Data;

public class RegistrationService {
    private final AuthDAO authDAO;

    public RegistrationService() {
        authDAO = new MemoryAuthDAO();
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
