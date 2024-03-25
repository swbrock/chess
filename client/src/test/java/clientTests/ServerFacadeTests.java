package clientTests;

import client.response.RegisterUserResponse;
import dataAccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import client.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setUp() throws Exception {
        facade.clear();
    }


    @Test
    void registerPositiveTest() {
        try {
            RegisterUserResponse registerResponse = facade.register("testUser", "password", "test@example.com");
            assertNotNull(registerResponse);
            assertNotNull(registerResponse.getAuthToken());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void registerNegativeTest() throws DataAccessException {
        try {
            facade.register("existingUser", "password", "existing@example.com");
        } catch (Exception e) {
            throw new DataAccessException("Failed to register");
        }

        assertThrows(Exception.class, () -> {
            // Register with existing username or email
            facade.register("existingUser", "password", "existing@example.com");
        });
    }

    @Test
    void signInPositiveTest() throws DataAccessException {
        try {
            facade.register("testUser", "password", "existing@example.com");
        } catch (Exception e) {
            throw new DataAccessException("Failed to register");
        }
        try {
            RegisterUserResponse signInResponse = facade.signIn("testUser", "password");
            assertNotNull(signInResponse);
            assertNotNull(signInResponse.getAuthToken());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void signInNegativeTest() throws DataAccessException {
        try {
            facade.register("testUser", "password", "existing@example.com");
        } catch (Exception e) {
            throw new DataAccessException("Failed to register");
        }
        assertThrows(Exception.class, () -> {
            // Sign in with wrong password
            facade.signIn("testUser", "wrongPassword");
        });
    }

    @Test
    void signOutPositiveTest() throws Exception {
        try {
            facade.register("testUser", "password", "existing@example.com");
            facade.signIn("testUser", "password");
        } catch (Exception e) {
            throw new DataAccessException("Failed to register");
        }
        // Positive test case: Signing out successfully
        facade.signOut();
        assertNull(facade.authToken, "Authentication token should be null after sign out");
    }
    @Test
    void signOutNegativeTest() {
        // Negative test case: Attempting to sign out without an active session
        facade.authToken = null; // Simulate no active session
        assertThrows(Exception.class, () -> {
            facade.signOut();
        });
        assertNull(facade.authToken, "Authentication token should remain null if no sign-out operation was performed");
    }
    @Test
    void createGamePositiveTest() throws DataAccessException {
        try {
            facade.register("testUser", "password", "existing@example.com");
            facade.signIn("testUser", "password");
        } catch (Exception e) {
            throw new DataAccessException("Failed to register");
        }
        try {
            facade.createGame("Test Game");
            // No assertion as we're just testing if an exception is thrown
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void createGameNegativeTest() {
        assertThrows(Exception.class, () -> {
            // Create game without be authorized
            facade.createGame("");
        });
    }

    @Test
    void listGamesPositiveTest() throws DataAccessException {
        try {
            facade.register("testUser", "password", "existing@example.com");
            facade.signIn("testUser", "password");
        } catch (Exception e) {
            throw new DataAccessException("Failed to register");
        }
        try {
            facade.createGame("Test Game");
            // No assertion as we're just testing if an exception is thrown
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        try {
            List<GameData> games = facade.listGames();
            assertNotNull(games);
            assertFalse(games.isEmpty());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void listGamesNegativeTest() {
        assertThrows(Exception.class, () -> {
            List<GameData> games = facade.listGames();
            assertNotNull(games);
            assertFalse(games.isEmpty());
        });
    }

    @Test
    void joinGamePositiveTest() throws DataAccessException {
        try {
            facade.register("testUser", "password", "existing@example.com");
            facade.signIn("testUser", "password");
        } catch (Exception e) {
            throw new DataAccessException("Failed to register");
        }
        try {
            facade.createGame("Test Game");
            // No assertion as we're just testing if an exception is thrown
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        try {
            int gameId = 1;
            GameData joinedGame = facade.joinGame(gameId, "white");
            assertNotNull(joinedGame);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void joinGameNegativeTest() throws DataAccessException {
        //join game that doesnt exist
        try {
            facade.register("testUser", "password", "existing@example.com");
            facade.signIn("testUser", "password");
        } catch (Exception e) {
            throw new DataAccessException("Failed to register");
        }
        try {
            facade.createGame("Test Game");
            // No assertion as we're just testing if an exception is thrown
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertThrows(Exception.class, () -> {
            int gameId = 3;
            GameData joinedGame = facade.joinGame(gameId, "white");
            assertNotNull(joinedGame);
        });
    }

    @Test
    void ObserveGamePositiveTest() throws DataAccessException {
        try {
            facade.register("testUser", "password", "existing@example.com");
            facade.signIn("testUser", "password");
        } catch (Exception e) {
            throw new DataAccessException("Failed to register");
        }
        try {
            facade.createGame("Test Game");
            // No assertion as we're just testing if an exception is thrown
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        try {
            int gameId = 1;
            GameData joinedGame = facade.observeGame(gameId);
            assertNotNull(joinedGame);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void observeGameNegativeTest() throws DataAccessException {
        //observe game that doesnt exist
        try {
            facade.register("testUser", "password", "existing@example.com");
            facade.signIn("testUser", "password");
        } catch (Exception e) {
            throw new DataAccessException("Failed to register");
        }
        try {
            facade.createGame("Test Game");
            // No assertion as we're just testing if an exception is thrown
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertThrows(Exception.class, () -> {
            int gameId = 3;
            GameData joinedGame = facade.observeGame(gameId);
            assertNotNull(joinedGame);
        });
    }
}