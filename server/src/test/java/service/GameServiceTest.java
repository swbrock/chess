package service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dataAccess.GameDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;
import model.UserData;

public class GameServiceTest {
    GameService gameService = new GameService();
    UserService userService = new UserService();

    public GameServiceTest() throws DataAccessException {
    }
    // Good tests extensively show that we get the expected behavior. This could be asserting that data put into the database is really there, or that a function throws an error when it should. Write a positive and a negative JUNIT test case for each public method on your Service classes, except for Clear which only needs a positive test case. A positive test case is one for which the action happens successfully (e.g., successfully claiming a spot in a game). A negative test case is one for which the operation fails (e.g., trying to claim an already claimed spot).

     //The service unit tests must directly call the methods on your service classes. They should not use the HTTP server pass off test code that is provided with the starter code.

    @BeforeEach
    public void clear() {
        try {
            gameService.clearGames();
            userService.clearUsers();
        } catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateGame() throws Exception {
        // Positive test case
        //write a test that creates a game and checks that it was created

        gameService.createGame("testGame");
        GameData newGame = gameService.getGameById(1);
        assertNotNull(newGame);
    }

    @Test
    public void testListGames() throws Exception {
        // Positive test case
        //write a test that creates a game and then lists the games and checks that the game was listed
        gameService.createGame("testGame");
        GameData newGame = gameService.getGameById(1);
        assertNotNull(newGame);
        assertNotNull(gameService.listGames());
        // Negative test case
        //write a test that lists the games and checks that the game was not listed
    }

    @Test
    public void testJoinGame() throws Exception {
        // Positive test case
        //write a test that creates a game and then joins the game and checks that the game was joined
        gameService.createGame("testGame");
        GameData newGame = gameService.getGameById(1);
        assertNotNull(newGame);
        userService.createUser(new UserData("steve", "dsfsdf", "null"));
        userService.createUser(new UserData("bob", "dsfsdf", "null"));
        gameService.updateGame(1, new GameData(1, "steve", "bob", "testGame", new ChessGame()));
        assertNotNull(gameService.getGameById(1));
        // Negative test case
        //write a test that joins the game and checks that the game was not joined
    }

    @Test
    public void testClearGames() throws Exception {
        // Positive test case
        //write a test that creates a game and then clears the games and checks that the game was cleared
        gameService.createGame("testGame");
        GameData newGame = gameService.getGameById(1);

        assertNotNull(newGame);
        gameService.clearGames();

        assertNull(gameService.getGameById(1));
        // Negative test case
        //write a test that clears the games and checks that the game was not cleared

        gameService.clearGames();
        assertNull(gameService.getGameById(1));
    }

}
