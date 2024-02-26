package dataAccess;

import model.UserData;

public interface UserDAO {

    void createUser(UserData u) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void deleteUser(String username) throws DataAccessException;

    void updateUser(UserData u) throws DataAccessException;

    void clearUsers() throws DataAccessException;


}
