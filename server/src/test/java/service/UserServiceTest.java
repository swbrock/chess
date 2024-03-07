package service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataAccess.AuthDAO;
import model.AuthData;
import model.UserData;

public class UserServiceTest {

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
    public void testCreateUser_positive() throws Exception {
        // Positive test case
        UserData user = new UserData("dfd", "dfsdf", "Dfsdf");
        userService.createUser(user);
        UserData test = userService.userDAO.getUser("dfd", "dfsdf");
        assertNotNull(test);

    }


    @Test
    public void testClearUsers() throws Exception {
        // Positive test case
        userService.clearUsers();
        // Negative test case
        userService.clearUsers();
    }
}


