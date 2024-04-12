package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
        
        //JOIN_PLAYER	Integer gameID, ChessGame.TeamColor playerColor	Used for a user to request to join a game.
        private int gameID;
        private ChessGame.TeamColor playerColor;
        public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
            super(authToken);
            this.gameID = gameID;
            this.playerColor = playerColor;
            this.commandType = CommandType.JOIN_PLAYER;
        }

        public int getGameID() {
            return gameID;
        }
    
        public ChessGame.TeamColor getPlayerColor() {
            return playerColor;
        }
    
        @Override
        public String toString() {
            return "JoinPlayer{" +
                    "gameID=" + gameID +
                    ", playerColor=" + playerColor +
                    '}';
        }
}
