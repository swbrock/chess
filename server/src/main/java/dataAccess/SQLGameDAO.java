package dataAccess;

import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements GameDAO {

    private static final String INSERT_GAME_SQL = "INSERT INTO GameData (whiteUsername, blackUsername, gameName, gameData) VALUES (?, ?, ?, ?)";
    private static final String SELECT_GAME_SQL = "SELECT * FROM GameData WHERE gameID = ?";
    private static final String SELECT_ALL_GAMES_SQL = "SELECT * FROM GameData";
    private static final String UPDATE_GAME_SQL = "UPDATE GameData SET whiteUsername = ?, blackUsername = ?, gameName = ?, gameData = ? WHERE gameID = ?";
    private static final String DELETE_GAME_SQL = "DELETE FROM GameData WHERE gameID = ?";
    private static final String DELETE_ALL_GAMES_SQL = "TRUNCATE TABLE GameData";

    public SQLGameDAO() throws DataAccessException {
        try {
            String[] createStatements = {
                    """
    CREATE TABLE IF NOT EXISTS GameData (
        gameID INT NOT NULL AUTO_INCREMENT,
        whiteUsername VARCHAR(255) NOT NULL,
        blackUsername VARCHAR(255) NOT NULL,
        gameName VARCHAR(255) NOT NULL,
        gameData TEXT NOT NULL,
        PRIMARY KEY (gameID)
    );
    """
            };
            AppSettings appSettings = new AppSettings(createStatements);
            appSettings.configureDatabase();
        } catch (DataAccessException ex) {
            throw new DataAccessException("Failed to initialize SQLGameDAO");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_GAME_SQL)) {
            statement.setInt(1, gameID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractGameData(resultSet);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get game");
        }
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_GAMES_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                games.add(extractGameData(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to list games");
        }
        return games;
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_GAME_SQL)) {
            statement.setString(1, game.whiteUsername());
            statement.setString(2, game.blackUsername());
            statement.setString(3, game.gameName());
            statement.setString(4, game.game().toString()); // Serialize ChessGame object
            statement.setInt(5, gameID);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update game");
        }
    }

    @Override
    public void clearGames() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ALL_GAMES_SQL)) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to clear games");
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_GAME_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, null); // whiteUsername
            statement.setString(2, null); // blackUsername
            statement.setString(3, gameName);
            statement.setString(4, null); // gameData (serialize ChessGame object)
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create game");
        }
        return 0;
    }

    private GameData extractGameData(ResultSet resultSet) throws SQLException {
        return new GameData(
                resultSet.getInt("gameID"),
                resultSet.getString("whiteUsername"),
                resultSet.getString("blackUsername"),
                resultSet.getString("gameName"),
                null // Deserialize ChessGame object
        );
    }
}
