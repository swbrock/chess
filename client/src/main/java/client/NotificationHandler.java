package client;

import webSocketMessages.serverMessages.ServerMessage;

public class NotificationHandler {
    public void notify(String message) {
        System.out.println(message);
    }

    public void notifyError(String message) {
        System.out.println("Error: " + message);
    }

    public void notifySuccess(String message) {
        System.out.println("Success: " + message);
    }

    public void notifyWarning(String message) {
        System.out.println("Warning: " + message);
    }

    public void handleNotification(ServerMessage serverMessage) {
        System.out.println(serverMessage);
    }

    public void handleError(ServerMessage serverMessage) {
        System.out.println("Error: " + serverMessage);
    }
}
