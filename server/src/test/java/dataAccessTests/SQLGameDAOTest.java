package dataAccessTests;

import static org.junit.Assert.*;

import model.GameData;
import org.junit.*;
import java.util.List;
import dataAccess.*;

public class SQLGameDAOTest {

    private GameDAO gameDAO;

    @Before
    public void setUp() throws Exception {
        gameDAO = new SQLGameDAO();
    }

    @Test
    public void testCreateGame_Positive() {
        try {
            int gameId = gameDAO.createGame("TestGame");
            assertTrue(gameId > 0);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateGame_Negative() {
        try {
            // Creating a game with the same name as an existing one
            int gameId1 = gameDAO.createGame("ExistingGame");
            int gameId2 = gameDAO.createGame("ExistingGame");
            assertEquals(0, gameId2);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetGame_Positive() {
        try {
            int gameId = gameDAO.createGame("TestGetGame");
            GameData gameData = gameDAO.getGame(gameId);
            assertNotNull(gameData);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetGame_Negative() {
        try {
            // Trying to get a non-existing game
            GameData gameData = gameDAO.getGame(1);
            assertNull(gameData);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testListGames_Positive() {
        try {
            List<GameData> games = gameDAO.listGames();
            assertNotNull(games);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

//    @Test
//    public void testUpdateGame_Positive() {
//        try {
//            int gameId = gameDAO.createGame("TestUpdateGame");
//            GameData gameData = gameDAO.getGame(gameId);
//            //gameData.setGameName("UpdatedGameName");
//            gameDAO.updateGame(gameId, gameData);
//            GameData updatedGameData = gameDAO.getGame(gameId);
//            assertEquals("UpdatedGameName", updatedGameData.gameName());
//        } catch (DataAccessException e) {
//            fail("Unexpected exception: " + e.getMessage());
//        }
//    }

    @Test
    public void testClearGames_Positive() {
        try {
            gameDAO.clearGames();
            List<GameData> games = gameDAO.listGames();
            assertEquals(0, games.size());
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
