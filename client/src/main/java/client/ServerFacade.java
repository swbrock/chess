package client;

import com.google.gson.Gson;
import org.glassfish.grizzly.http.util.HttpUtils;

import model.UserData;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private String serverURL;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    public void register(String username, String password, String email) {
        var path = "/user";
        UserData user = new UserData(username, password, email);
        try {
            makeRequest("POST", path, user, UserData.class);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void signIn(String username, String password) {
        var path = "/session";

    }

    public void signOut(String authToken) {

    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (request != null) {
                http.setDoOutput(true);
            }
            writeBody(request, http);
            http.connect();
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception("Not succesfull");
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
