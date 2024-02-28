package service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.AuthData;

public class RegistrationServiceTest {
    GameService gameService = new GameService();
    UserService userService = new UserService();
    RegistrationService registrationService = new RegistrationService();

    @BeforeEach
    public void clear() {
        try {
            gameService.clearGames();
            userService.clearUsers();
            registrationService.clearAuth();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test

    public void testCreateAuth() throws Exception {
        // Positive test case
        try {
            registrationService.createAuth("testUser");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        
        // Negative test case
        registrationService.createAuth("testUser");
        AuthData newAuth = registrationService.createAuth("testUser");
        assertNotNull(newAuth);

        }
    }

    @Test
    public void testClearAuth() throws Exception {
        // Positive test case
        registrationService.createAuth("testUser");
        registrationService.clearAuth();
        AuthData newAuth = registrationService.createAuth("testUser");
        assertNotNull(newAuth);
        // Negative test case
        registrationService.clearAuth();
        newAuth = registrationService.createAuth("testUser");
        assertNotNull(newAuth);
    }
}