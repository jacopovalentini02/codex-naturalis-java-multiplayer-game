package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;

import java.util.*;

/**
 * Class GameManager
 *
 * Description: This class manages the lifecycle of games. It is responsible for creating, joining, starting, and deleting games.
 */
public class GameManager {
    private HashMap<Integer, Game> gameList;
    int firstAvailableGameID; // Indicates the first available number to be used as a Game ID

    /**
     * Constructor for the GameManager class.
     * Initializes the game list and the first available game ID.
     */
    public GameManager() {
        this.gameList = new HashMap<>();
        this.firstAvailableGameID = 0;
    }

    /**
     * Creates a new game and returns its ID.
     *
     * @param numOfPlayers The number of players in the new game.
     * @param username The username of the first player.
     * @param clientID The client ID of the first player.
     * @return The ID of the newly created game.
     */
    public int createGame(int numOfPlayers, String username, int clientID) {
        GameServerInstance gameServerInstance = new GameServerInstance();
        Game newGame = new Game(gameServerInstance, this, firstAvailableGameID, numOfPlayers, new Player(username, gameServerInstance, clientID));
        gameList.put(firstAvailableGameID, newGame);
        int gameID = firstAvailableGameID;
        firstAvailableGameID++;
        return gameID;
    }

    /**
     * Adds a new player to an existing game.
     *
     * @param nick The nickname of the new player.
     * @param idGame The ID of the game to join.
     * @param clientID The client ID of the new player.
     */
    public void joinGame(String nick, int idGame, int clientID) {
        Game gameToJoin = gameList.get(idGame);
        GameServerInstance gameServerInstance = gameToJoin.getGameServerInstance();

        Player newPlayer = new Player(nick, gameServerInstance, clientID);
        gameToJoin.addPlayer(newPlayer);
    }

    /**
     * Deletes a game from the game list.
     *
     * @param idGame The ID of the game to be deleted.
     */
    public void deleteGame(int idGame) {
        gameList.remove(idGame);
    }

    /**
     * Retrieves the list of game IDs.
     *
     * @return A list of game IDs.
     */
    public List<Integer> getGameIDs() {
        return new ArrayList<>(gameList.keySet());
    }

    /**
     * Retrieves the game list.
     *
     * @return A map of game IDs to Game objects.
     */
    public HashMap<Integer, Game> getGameList() {
        return gameList;
    }

    /**
     * Starts a game by setting up its field.
     *
     * @param gameID The ID of the game to be started.
     */
    public void startGame(int gameID) {
        Game gameToStart = gameList.get(gameID);
        gameToStart.setupField();
    }
}
