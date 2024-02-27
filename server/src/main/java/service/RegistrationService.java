package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import model.AuthData;

public class RegistrationService {
    private final AuthDAO authDAO;

    public RegistrationService() {
        authDAO = new MemoryAuthDAO();
    }

    public void clearAuth() throws DataAccessException {
        authDAO.clearAuths();
    }






}
