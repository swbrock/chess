package dataAccess;

import java.sql.SQLException;

public class AppSettings {

    private final String[] createStatements;

    public AppSettings(String[] createStatements) {
        this.createStatements = createStatements;
    }

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database");
        }
    }
}
