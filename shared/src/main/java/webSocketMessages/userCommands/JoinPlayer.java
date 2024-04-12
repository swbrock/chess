package webSocketMessages.userCommands;

import chess.ChessGame;
import model.GameData;

public class JoinPlayer extends UserGameCommand{
        
        //JOIN_PLAYER	Integer gameID, ChessGame.TeamColor playerColor	Used for a user to request to join a game.
        Integer gameID;
        ChessGame.TeamColor playerColor;
        GameData game;
        public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, GameData game) {
            super(authToken);
            this.gameID = gameID;
            this.playerColor = playerColor;
            this.commandType = CommandType.JOIN_PLAYER;
            this.game = game;
        }

        public int getGameID() {
            return this.gameID;
        }
    
        public ChessGame.TeamColor getPlayerColor() {
            return this.playerColor;
        }
    
        public GameData getGame() { return this.game; }
}
