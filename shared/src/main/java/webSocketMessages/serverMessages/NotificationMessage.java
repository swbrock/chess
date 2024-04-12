package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage{
    //NOTIFICATION	String message	This is a message meant to inform a player when another player made an action.
    private String message;

    public NotificationMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
        this.serverMessageType = ServerMessageType.NOTIFICATION;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
