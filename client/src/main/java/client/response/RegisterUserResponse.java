package client.response;

public class RegisterUserResponse {
    private String authToken;
    private String username;

    public RegisterUserResponse(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
