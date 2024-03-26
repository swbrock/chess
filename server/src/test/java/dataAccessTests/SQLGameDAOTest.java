//package dataAccessTests;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import chess.ChessGame;
//import dataAccess.GameDAO;
//import dataAccess.SQLGameDAO;
//import org.junit.After;
//import org.junit.*;
//
//import dataAccess.DataAccessException;
//import model.GameData;
//
//import java.util.List;
//
//
//public class SQLGameDAOTest {
//
//    private GameDAO gameDAO;
//
//    @Before
//    public void setUp() throws Exception {
//        gameDAO = new SQLGameDAO();
//    }
//    @After
//    public void tearDown() {
//        // Clear the database after each test case
//        try {
//            gameDAO.clearGames();
//        } catch (DataAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testCreateGame_Positive() {
//        try {
//            int gameId = gameDAO.createGame("TestGame");
//            assertTrue(gameId > 0);
//        } catch (DataAccessException e) {
//            fail("Unexpected exception: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testCreateGame_Negative() {
//        assertThrows(AssertionError.class, () -> {
//            int gameId = gameDAO.createGame("test");
//            int gameId2 = gameDAO.createGame("test");
//            assertEquals(gameId, gameId2);
//        });
//    }
//
//    @Test
//    public void testGetGame_Positive() {
//        try {
//            int gameId = gameDAO.createGame("TestGetGame");
//            GameData gameData = gameDAO.getGame(gameId);
//            assertNotNull(gameData);
//        } catch (DataAccessException e) {
//            fail("Unexpected exception: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testGetGame_Negative() throws DataAccessException {
//        try {
//            GameData game = gameDAO.getGame(1);
//            assertNull(game);
//        } catch (Exception e) {
//            throw new DataAccessException("Failed");
//        }
//    }
//
//    @Test
//    public void testListGames_Positive() {
//        try {
//            List<GameData> games = gameDAO.listGames();
//            assertNotNull(games);
//        } catch (DataAccessException e) {
//            fail("Unexpected exception: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testListGames_Negative() {
//        try {
//            List<GameData> games = gameDAO.listGames();
//            assertNotNull(games);
//            assertTrue(games.isEmpty());
//        } catch (DataAccessException e) {
//            fail("Unexpected exception: " + e.getMessage());
//        }
//    }
//
//
//    @Test
//    public void testUpdateGame_Positive() {
//        try {
//            int gameId = gameDAO.createGame("TestUpdateGame");
//            GameData gameData = gameDAO.getGame(gameId);
//            GameData newGame = new GameData(gameData.gameID(), "white", "black", gameData.gameName(), gameData.game());
//            gameDAO.updateGame(gameId, newGame);
//            GameData updatedGameData = gameDAO.getGame(gameId);
//            assertEquals("white", updatedGameData.whiteUsername());
//        } catch (DataAccessException e) {
//            fail("Unexpected exception: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testClearGames_Positive() {
//        try {
//            gameDAO.clearGames();
//            List<GameData> games = gameDAO.listGames();
//            assertEquals(0, games.size());
//        } catch (DataAccessException e) {
//            fail("Unexpected exception: " + e.getMessage());
//        }
//    }
//}
