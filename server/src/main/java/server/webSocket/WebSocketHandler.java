package server.webSocket;

import com.google.gson.Gson;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataAccess.*;
import model.GameData;

import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.RedrawBoard;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_OBSERVER:
                joinObserver(session, message);
            case LEAVE:
                leaveGame(session, message);
            case JOIN_PLAYER:
                joinPlayer(session, message);
            case MAKE_MOVE:
                makeMove(session, message);
            case REDRAW:
                redraw(session, message);
        }
    }
    
    private void joinPlayer (Session session, String action) throws Exception {
        try {
            JoinPlayer joinPlayer = new Gson().fromJson(action, JoinPlayer.class);
            String authString = joinPlayer.getAuthString();
            GameData game = joinPlayer.getGame();
            ChessGame.TeamColor playerColor = joinPlayer.getPlayerColor();
            var message = String.format(authString + " has joined " + playerColor.toString());
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.add(authString, session);
            connections.broadcast(authString, notification);
            var loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGame.game = game;
            connections.sendMessage(authString, loadGame);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    private void joinObserver (Session session, String action) throws Exception {
        try {
            JoinPlayer joinPlayer = new Gson().fromJson(action, JoinPlayer.class);
            String authString = joinPlayer.getAuthString();
            GameData game = joinPlayer.getGame();
            var message = String.format(authString + " has joined as an observer");
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.add(authString, session);
            connections.broadcast(authString, notification);
            var loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGame.game = game;
            connections.sendMessage(authString, loadGame);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    private void leaveGame (Session session, String action) throws Exception {
        try {
            JoinPlayer joinPlayer = new Gson().fromJson(action, JoinPlayer.class);
            String authString = joinPlayer.getAuthString();
            var message = String.format(authString + " has left the game");
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.remove(authString);
            connections.broadcast("", notification);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


    public void makeMove(Session session, String action) throws Exception {
        try {
            MakeMove makeMove = new Gson().fromJson(action, MakeMove.class);
            int gameID = makeMove.getGameID();
            ChessPosition startPos = makeMove.getStartPos();
            ChessPosition endPos = makeMove.getEndPos();
            ChessGame.TeamColor color = makeMove.getPlayerColor();

            String username = makeMove.getAuthToken();
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(gameID);

            if (color != game.game().getTeamTurn()) {
                throw new Exception("Error: Not your turn");
            }

            ChessMove move = new ChessMove(startPos, endPos, null);
            game.game().makeMove(move);

            gameDAO.updateGame(gameID, game);

            LoadGame newGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME);
            newGame.game = game;
            connections.broadcast("", newGame);
            String message = (username + " has made a move: " + startPos.toString() + " to " + endPos.toString());
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(username, serverMessage);
        } catch (InvalidMoveException ex) {
            System.out.print("Error - InvalidMoveException: " + ex.getMessage());
        } catch (DataAccessException ex) {
            System.out.print("Error - DataAccessException: " + ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void redraw(Session session, String action) throws Exception {
        try {
            RedrawBoard redraw = new Gson().fromJson(action, RedrawBoard.class);
            String username = redraw.getAuthToken();
            int gameID = redraw.getGameID();
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(gameID);
            var loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGame.game = game;
            connections.sendMessage(username, loadGame);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


}