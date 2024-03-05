package dataAccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    private static final String INSERT_AUTH_SQL = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
    private static final String SELECT_AUTH_SQL = "SELECT COUNT(*) FROM AuthData WHERE authToken = ?";
    private static final String DELETE_AUTH_SQL = "DELETE FROM AuthData WHERE authToken = ?";
    private static final String DELETE_ALL_AUTHS_SQL = "TRUNCATE TABLE AuthData";
    private static final String SELECT_ALL_AUTHS_COUNT_SQL = "SELECT COUNT(*) FROM AuthData";


    public SQLAuthDAO() throws DataAccessException {
        try {
            String[] createStatements = {
                    """
        CREATE TABLE IF NOT EXISTS AuthData (
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (authToken)
        );
        """
            };
            AppSettings appSettings = new AppSettings(createStatements);
            appSettings.configureDatabase();
        } catch (DataAccessException ex) {
            throw new DataAccessException("Failed to initialize SQLAuthDAO");
        }
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_AUTH_SQL)) {
            String authToken = UUID.randomUUID().toString();
            statement.setString(1, authToken);
            statement.setString(2, username);
            statement.executeUpdate();
            return new AuthData(authToken, username);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create auth");
        }
    }

    @Override
    public Boolean getAuth(String authToken) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_AUTH_SQL)) {
            statement.setString(1, authToken);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get auth");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_AUTH_SQL)) {
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete auth");
        }
    }

    @Override
    public void clearAuths() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ALL_AUTHS_SQL)) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to clear auths");
        }
    }

    @Override
    public int numberOfAuths() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_AUTHS_COUNT_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get the number of auths");
        }
    }

    @Override
    public AuthData findAuthDataByAuthToken(String authToken) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_AUTH_SQL)) {
            statement.setString(1, authToken);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    return new AuthData(authToken, username);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to find auth data by authToken");
        }
        return null;
    }

    @Override
    public AuthData findAuthDataByUsername(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM AuthData WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String authToken = resultSet.getString("authToken");
                    return new AuthData(authToken, username);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to find auth data by username");
        }
        return null;
    }
}