package dataAccessTests;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDAOTest {

    private static SQLAuthDAO authDAO;

    @BeforeAll
    static void setUp() {
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        // Clear the database after each test case
        try {
            authDAO.clearAuths();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createAuthPositiveTest() {
        // Positive test case: Successfully create authentication data
        try {
            AuthData authData = authDAO.createAuth("username");
            assertNotNull(authData);
            assertEquals("username", authData.username());
            assertNotNull(authData.authToken());
        } catch (DataAccessException e) {
            fail("Exception thrown when creating auth: " + e.getMessage());
        }
    }

    @Test
    void createAuthNegativeTest() {
        assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(null);
        });
    }

    @Test
    void getAuthPositiveTest() {
        // Positive test case: Successfully retrieve authentication data
        try {
            // Insert a mock authentication data into the database
            AuthData mockAuthData = authDAO.createAuth("mock_username");
            // Retrieve the authentication data using getAuth method
            assertTrue(authDAO.getAuth(mockAuthData.authToken()));
        } catch (DataAccessException e) {
            fail("Exception thrown when getting auth: " + e.getMessage());
        }
    }

    @Test
    void getAuthNegativeTest() {
        // Negative test case: Failing to retrieve authentication data
        try {
            assertFalse(authDAO.getAuth("invalid_token"));
        } catch (DataAccessException e) {

        }
    }
    // Implement similar positive and negative test cases for other methods...

}
