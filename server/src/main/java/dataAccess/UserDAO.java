package dataAccess;

import model.UserData;

public interface UserDAO {

    UserData createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void deleteUser(String username) throws DataAccessException;


    void clearUsers() throws DataAccessException;


}
