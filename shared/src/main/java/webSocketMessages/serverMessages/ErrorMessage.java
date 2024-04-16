package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{
    //ERROR	String errorMessage	This message is sent to a client when it sends an invalid command. The message must include the word Error.
    private String errorMessage;

    public ErrorMessage(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.serverMessageType = ServerMessageType.ERROR;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
