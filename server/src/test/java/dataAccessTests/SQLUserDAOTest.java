package dataAccessTests;

import static org.junit.Assert.*;

import dataAccess.DataAccessException;
import model.UserData;

public class SQLUserDAOTest {

    private SQLUserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        // Initialize the SQLUserDAO object before each test
        userDAO = new SQLUserDAO();
    }

    @After
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

    @Test
    public void testClearUsersPositive() {
        // Positive test case: Clearing all users from the database
        try {
            // First, create some users
            userDAO.createUser(new UserData("user1", "password1", "user1@example.com"));
            userDAO.createUser(new UserData("user2", "password2", "user2@example.com"));
            userDAO.createUser(new UserData("user3", "password3", "user3@example.com"));
            // Then, clear all users
            userDAO.clearUsers();
            // Attempt to retrieve all users after clearing
            int numberOfUsers = userDAO.numberOfUsers();
            assertEquals(0, numberOfUsers); // There should be no users in the database
        } catch (DataAccessException e) {
            fail("Unexpected DataAccessException: " + e.getMessage());
        }
    }
}
