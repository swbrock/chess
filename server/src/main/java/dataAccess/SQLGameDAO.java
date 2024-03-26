package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.io.IOException;
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
    private static final String DELETE_ALL_GAMES_SQL = "TRUNCATE TABLE GameData";
    private static final Gson gson = new Gson();


    public SQLGameDAO() throws DataAccessException {
        try {
            String[] createStatements = {
                    """
    CREATE TABLE IF NOT EXISTS GameData (
        gameID INT NOT NULL AUTO_INCREMENT,
        whiteUsername VARCHAR(255),
        blackUsername VARCHAR(255),
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
            } catch (Exception e) {
                throw new DataAccessException("Not able to get game");
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

            // Serialize ChessGame object to JSON string
            String serializedGame = gson.toJson(game.game());
            statement.setString(4, serializedGame);

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
            ChessGame game = new ChessGame();
            game.getBoard().resetBoard();
            statement.setString(1, null);
            statement.setString(2, null);
            statement.setString(3, gameName);
            statement.setString(4, gson.toJson(game));
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
        int gameID = resultSet.getInt("gameID");
        String whiteUsername = resultSet.getString("whiteUsername");
        String blackUsername = resultSet.getString("blackUsername");
        String gameName = resultSet.getString("gameName");
        String serializedGame = resultSet.getString("gameData");

        // Deserialize ChessGame object
        ChessGame game = gson.fromJson(serializedGame, ChessGame.class);

        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
