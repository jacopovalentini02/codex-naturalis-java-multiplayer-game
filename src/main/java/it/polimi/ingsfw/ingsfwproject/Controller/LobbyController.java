package it.polimi.ingsfw.ingsfwproject.Controller;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.Game;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Model.GameState;
import it.polimi.ingsfw.ingsfwproject.Model.Player;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExcpetionMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.GameJoinedMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.SendGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import it.polimi.ingsfw.ingsfwproject.Network.Server.Server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LobbyController implements Controller {
    private final GameManager lobby;
    private final Server server;

    public LobbyController(GameManager lobby, Server server){
        this.lobby = lobby;
        this.server = server;
    }

    public void createGame(int numOfPlayers, String thisPlayer, int clientID){
        if(numOfPlayers < 2 || numOfPlayers > 4)
            server.sendResponse(new ExcpetionMessage(clientID, "The number of players must be between 2 and 4, but you entered " + numOfPlayers));
        int gameID;
        synchronized (lobby){
            gameID = lobby.createGame(numOfPlayers, thisPlayer, clientID);
        }
        server.setHandlersAndInstance(getGameServerInstance(gameID), clientID, thisPlayer);
        server.sendResponse(new GameJoinedMessage(clientID, gameID, thisPlayer));
    }

    public void joinExistingGame(String nick, int idGame, int clientID) {
        synchronized (lobby) {

            if (!lobby.getGameIDs().contains(idGame))
                server.sendResponse(new ExcpetionMessage(clientID, "There is no game with ID " + idGame));

            Game gameToJoin = lobby.getGameList().get(idGame);

            if(gameToJoin.getListOfPlayers().size() == gameToJoin.getNumOfPlayers())
                server.sendResponse(new ExcpetionMessage(clientID, "Game " + idGame + " is full"));

            for(Player player : gameToJoin.getListOfPlayers()) {
                if(player.getUsername().equals(nick)) {
                    server.sendResponse(new ExcpetionMessage(clientID, "Nickname " + nick + " is already taken"));
                }}
            lobby.joinGame(nick, idGame, clientID);
            server.setHandlersAndInstance(getGameServerInstance(idGame),clientID, nick);
            server.sendResponse(new GameJoinedMessage(clientID, idGame, nick));
        }
    }

    public void deleteGame(int idGame, int clientID){
        synchronized (lobby) {
            if (!lobby.getGameIDs().contains(idGame))
                server.sendResponse(new ExcpetionMessage(clientID, "there's no game with ID:" + idGame));
            lobby.deleteGame(idGame);
        }
    }

    public GameController getGameController(int idGame){
        synchronized (lobby) {
            return lobby.getGameList().get(idGame).getController();
        }
    }

    public void getGameList(int ClientID){
        HashMap<Integer, Integer> games = new HashMap<>();

        for (Map.Entry<Integer, Game> entry: lobby.getGameList().entrySet()) {
            if (entry.getValue().getState() == GameState.WAITING_FOR_PLAYERS) {
                games.put(entry.getKey(), entry.getValue().getListOfPlayers().size());
            }
        }
        server.sendResponse(new SendGameListMessage(ClientID, games));
    }


    public GameServerInstance getGameServerInstance(int idGame){
        synchronized (lobby){
            return lobby.getGameList().get(idGame).getGameServerInstance();
        }
    }

    public void checkIfGameNeedsToBeStarted(int gameID){
        synchronized (lobby){
            Game game = lobby.getGameList().get(gameID);
            if (game.getListOfPlayers().size() == game.getNumOfPlayers())
                lobby.startGame(gameID);
        }
    }

}
