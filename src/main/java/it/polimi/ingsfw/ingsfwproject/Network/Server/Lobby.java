package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Model.Game;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Model.GameState;
import it.polimi.ingsfw.ingsfwproject.Model.Player;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.GameJoinedMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.SendGameListMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code Lobby} class implements the {@code Controller} interface to manage lobby-related functionality and interactions.
 *
 * This controller is responsible for coordinating actions and behaviors before a player enters a game.
 */

public class Lobby implements Controller {
    private final GameManager gameManager;
    private final Server server;

    /**
     * Creates an instance of {@code Lobby} with the specified values.
     * @param gameManager the {@code GameManager} of the game.
     * @param server the {@code Server} of the game.
     */
    public Lobby(GameManager gameManager, Server server){
        this.gameManager = gameManager;
        this.server = server;
    }

    /**
     * Creates a new {@code Game} with the specified values and allows the player who initiated it to join.
     * This method ensures that the number of players specified is between 2 and 4 inclusive. If the number of players
     * is not within this range, an {@code ExceptionMessage} is sent.
     * @param numOfPlayers The number of players of the game (should be between 2 and 4).
     * @param thisPlayer The username of the player creating the game.
     * @param clientID the ID of the {@code Client} requesting to create the game.
     */
    public void createGame(int numOfPlayers, String thisPlayer, int clientID){
        if(numOfPlayers < 2 || numOfPlayers > 4) {
            server.sendResponse(new ExceptionMessage(clientID, "The number of players must be between 2 and 4, but you entered " + numOfPlayers));
            return;
        }

        int gameID;
        synchronized (gameManager){
            gameID = gameManager.createGame(numOfPlayers, thisPlayer, clientID);
        }
        server.setHandlersAndInstance(getGameServerInstance(gameID), clientID, thisPlayer);
        server.sendResponse(new GameJoinedMessage(clientID, gameID, thisPlayer));

        Player addedPlayer = getGameServerInstance(gameID).getPlayer(thisPlayer);

        if (addedPlayer != null){ //adding the new player to the game server instance
            getGameServerInstance(gameID).addPlayer(addedPlayer, clientID);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * Allows a player to join an existing game.
     * It checks several conditions before allowing the player to join:
     * <ul>
     *   <li>If the game ID is not found in the {@code GameManager}'s list.</li>
     *   <li>If the game is already full.</li>
     *   <li>If the username provided is already taken in that game.</li>
     * </ul>
     * If any of these conditions are met, an {@code ExceptionMessage} is sent to notify the client.
     * When a player joins a game that becomes full, the game is automatically set to start.
     * @param nick The username of the player wanting to join the game.
     * @param idGame The ID of the game that the player wants to join.
     * @param clientID The ID of the {@code Client} requesting to join the game.
     */
    public void joinExistingGame(String nick, int idGame, int clientID) {
        synchronized (gameManager) {

            if (!gameManager.getGameIDs().contains(idGame)){
                server.sendResponse(new ExceptionMessage(clientID, "There is no game with ID " + idGame));
                return;
            }


            Game gameToJoin = gameManager.getGameList().get(idGame);

            if(gameToJoin.getListOfPlayers().size() == gameToJoin.getNumOfPlayers()){
                server.sendResponse(new ExceptionMessage(clientID, "Game " + idGame + " is full"));
                return;
            }


            for(Player player : gameToJoin.getListOfPlayers()) {
                if(player.getUsername().equals(nick)) {
                    server.sendResponse(new ExceptionMessage(clientID, "Nickname " + nick + " is already taken"));
                    return;
                }}
            server.sendResponse(new GameJoinedMessage(clientID, idGame, nick));
            server.setHandlersAndInstance(getGameServerInstance(idGame),clientID, nick);
            gameManager.joinGame(nick, idGame, clientID);

            Player addedPlayer = getGameServerInstance(idGame).getPlayer(nick);

            if (addedPlayer != null){ //adding the new player to the game server instance
                getGameServerInstance(idGame).addPlayer(addedPlayer, clientID);
            } else {
                throw new RuntimeException();
            }
            checkIfGameNeedsToBeStarted(idGame);
        }
    }

    /**
     * Sends a {@code sendGameListMessage} containing the list of active games to the specified client.
     * @param ClientID The ID of the client requesting to receive the game list.
     */
    public void getGameList(int ClientID){
        HashMap<Integer, Integer> games = new HashMap<>();

        for (Map.Entry<Integer, Game> entry: gameManager.getGameList().entrySet()) {
            if (entry.getValue().getState() == GameState.WAITING_FOR_PLAYERS) {
                games.put(entry.getKey(), entry.getValue().getListOfPlayers().size());
            }
        }
        server.sendResponse(new SendGameListMessage(ClientID, games));
    }

    /**
     * Retrieves the {@code GameServerInstance} associated with a specified game ID.
     * @param idGame The ID of the game for which the {@code GameServerInstance} is requested.
     * @return The {@code GameServerInstance} of the specified game.
     */
    public GameServerInstance getGameServerInstance(int idGame){
        synchronized (gameManager){
            return gameManager.getGameList().get(idGame).getGameServerInstance();
        }
    }

    /**
     * Checks if a game needs to be started after a player joins.
     *
     * This method examines whether the specified game identified by {@code gameID} has reached its full capacity.
     * If the game is full, it starts the game.
     *
     * @param gameID The ID of the game to be checked.
     */
    public void checkIfGameNeedsToBeStarted(int gameID){
        synchronized (gameManager){
            Game game = gameManager.getGameList().get(gameID);
            if (game.getListOfPlayers().size() == game.getNumOfPlayers())
                gameManager.startGame(gameID);
        }
    }

}
