package dataAccessTests;

import static org.junit.Assert.*;

import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;

public class SQLUserDAOTest {

    private SQLUserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        // Initialize the SQLUserDAO object before each test
        userDAO = new SQLUserDAO();
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clear the users after each test
        userDAO.clearUsers();
    }

    @Test
    public void testCreateUserPositive() {
        // Positive test case: Creating a new user successfully
        UserData user = new UserData("testUser", "testPassword", "test@example.com");
        try {
            UserData createdUser = userDAO.createUser(user);
            assertNotNull(createdUser);
            assertEquals(user.username(), createdUser.username());
        } catch (DataAccessException e) {
            fail("Unexpected DataAccessException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateUserNegative() {
        // Negative test case: Creating a user with an existing username
        UserData existingUser = new UserData("existingUser", "password", "existing@example.com");
        try {
            // First, create the user
            userDAO.createUser(existingUser);
            // Then, try to create another user with the same username
            UserData newUser = new UserData("existingUser", "password2", "new@example.com");
            UserData createdUser = userDAO.createUser(newUser);
            assertNull(createdUser); // The creation should fail
        } catch (DataAccessException e) {
            // This is expected, so the test should pass
            assertTrue(true);
        }
    }

    @Test
    public void testGetUserPositive() {
        // Positive test case: Getting an existing user with correct credentials
        UserData user = new UserData("testUser", "testPassword", "test@example.com");
        try {
            // First, create the user
            userDAO.createUser(user);
            // Then, retrieve the user
            UserData retrievedUser = userDAO.getUser(user.username(), user.password());
            assertNotNull(retrievedUser);
            assertEquals(user.username(), retrievedUser.username());
        } catch (DataAccessException e) {
            fail("Unexpected DataAccessException: " + e.getMessage());
        }
    }

    @Test
    public void testGetUserNegative() {
        // Negative test case: Getting a non-existing user
        try {
            UserData user = userDAO.getUser("nonExistingUser", "password");
            assertNull(user); // The user should not exist
        } catch (DataAccessException e) {
            fail("Unexpected DataAccessException: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteUserPositive() {
        // Positive test case: Deleting an existing user
        UserData user = new UserData("testUser", "testPassword", "test@example.com");
        try {
            // First, create the user
            userDAO.createUser(user);
            // Then, delete the user
            userDAO.deleteUser(user.username());
            // Attempt to retrieve the user after deletion
            UserData retrievedUser = userDAO.getUser(user.username(), user.password());
            assertNull(retrievedUser); // The user should not exist
        } catch (DataAccessException e) {
            fail("Unexpected DataAccessException: " + e.getMessage());
        }
    }
}
