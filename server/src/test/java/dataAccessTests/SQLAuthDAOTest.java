package dataAccessTests;

import static org.junit.Assert.*;

import model.AuthData;
import org.junit.*;
import dataAccess.*;

public class SQLAuthDAOTest {

    private AuthDAO authDAO;

    @Before
    public void setUp() throws Exception {
        authDAO = new SQLAuthDAO();
    }

    @Test
    public void testCreateAuth_Positive() {
        try {
            AuthData authData = authDAO.createAuth("testuser");
            assertNotNull(authData);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateAuth_Negative() {
        try {
            // Creating an auth with an existing username
            authDAO.createAuth("existinguser");
            AuthData authData = authDAO.createAuth("existinguser");
            assertNull(authData);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetAuth_Positive() {
        try {
            // Creating an auth first
            AuthData authData = authDAO.createAuth("testuser");
            boolean authExists = authDAO.getAuth(authData.authToken());
            assertTrue(authExists);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetAuth_Negative() {
        try {
            // Getting an auth with a non-existing authToken
            boolean authExists = authDAO.getAuth("nonexistingtoken");
            assertFalse(authExists);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteAuth_Positive() {
        try {
            // Creating an auth first
            AuthData authData = authDAO.createAuth("testuser");
            authDAO.deleteAuth(authData.authToken());
            // Asserting that the auth has been deleted
            boolean authExists = authDAO.getAuth(authData.authToken());
            assertFalse(authExists);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testClearAuths_Positive() {
        try {
            authDAO.clearAuths();
            // Asserting that auth data has been cleared
            assertEquals(0, authDAO.numberOfAuths());
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFindAuthDataByAuthToken_Positive() {
        try {
            // Creating an auth first
            AuthData authData = authDAO.createAuth("testuser");
            AuthData foundAuthData = authDAO.findAuthDataByAuthToken(authData.authToken());
            assertNotNull(foundAuthData);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFindAuthDataByAuthToken_Negative() {
        try {
            // Trying to find auth data with a non-existing authToken
            AuthData foundAuthData = authDAO.findAuthDataByAuthToken("nonexistingtoken");
            assertNull(foundAuthData);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFindAuthDataByUsername_Positive() {
        try {
            // Creating an auth first
            AuthData authData = authDAO.createAuth("testuser");
            AuthData foundAuthData = authDAO.findAuthDataByUsername("testuser");
            assertNotNull(foundAuthData);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFindAuthDataByUsername_Negative() {
        try {
            // Trying to find auth data with a non-existing username
            AuthData foundAuthData = authDAO.findAuthDataByUsername("nonexistinguser");
            assertNull(foundAuthData);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
