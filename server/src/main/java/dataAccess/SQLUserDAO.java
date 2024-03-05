package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    private static final String INSERT_USER_SQL = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)";
    private static final String SELECT_USER_SQL = "SELECT * FROM UserData WHERE username = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM UserData WHERE username = ?";
    private static final String DELETE_ALL_USERS_SQL = "TRUNCATE TABLE UserData";

    public SQLUserDAO() throws DataAccessException {
        try {
            String[] createStatements = {
                    """
        CREATE TABLE IF NOT EXISTS UserData (
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            PRIMARY KEY (username)
        );
        """
            };
            AppSettings appSettings = new AppSettings(createStatements);
            appSettings.configureDatabase();
        } catch (DataAccessException ex) {
            throw new DataAccessException("Failed to initialize SQLUserDAO");
        }
    }

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER_SQL)) {
            statement.setString(1, user.username());
            statement.setString(2, user.password());
            statement.setString(3, user.email());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                return user;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create user");
        }
        return null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_SQL)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new UserData(resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email"));
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get user");
        }
        return null;
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_SQL)) {
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete user");
        }
    }

    @Override
    public void clearUsers() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ALL_USERS_SQL)) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to clear users");
        }
    }
}
