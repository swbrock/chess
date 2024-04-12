package webSocketMessages.serverMessages;

import model.GameData;

public class LoadGameMessage extends ServerMessage{
    //LOAD_GAME	game (can be any type, just needs to be called game)	Used by the server to send the current game state to a client. When a client receives this message, it will redraw the chess board.
    private Object game;

    public LoadGameMessage(ServerMessageType type, GameData game) {
        super(type);
        this.game = game;
        this.serverMessageType = ServerMessageType.LOAD_GAME;
    }

    public Object getGame() {
        return game;
    }

    @Override
    public String toString() {
        return "LoadGameMessage{" +
                "game=" + game +
                '}';
    }
}
