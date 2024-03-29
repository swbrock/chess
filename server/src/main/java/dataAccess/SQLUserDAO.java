package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    private static final String INSERT_USER_SQL = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)";
    private static final String SELECT_USER_SQL = "SELECT * FROM UserData WHERE username = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM UserData WHERE username = ?";
    private static final String DELETE_ALL_USERS_SQL = "TRUNCATE TABLE UserData";
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public SQLUserDAO() throws DataAccessException {
        try {
            String[] createStatements = {
                    """
        CREATE TABLE IF NOT EXISTS UserData (
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255),
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
        try (Connection connection = DatabaseManager.getConnection()) {
            // Check if a user with the same username already exists
            String checkUserSQL = "SELECT COUNT(*) FROM UserData WHERE username = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkUserSQL)) {
                checkStatement.setString(1, user.username());
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        throw new DataAccessException("User already exists");
                    }
                }
            }

            String hashedPassword = passwordEncoder.encode(user.password());

            try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_USER_SQL)) {
                insertStatement.setString(1, user.username());
                insertStatement.setString(2, hashedPassword); // Store hashed password
                insertStatement.setString(3, user.email());
                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    return user;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create user");
        }
        return null;
    }


    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_SQL)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");
                    if (passwordEncoder.matches(password, hashedPassword)) {
                        return new UserData(resultSet.getString("username"),
                                password,
                                resultSet.getString("email"));
                    }
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
