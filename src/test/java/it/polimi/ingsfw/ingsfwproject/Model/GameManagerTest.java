package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    GameManager gameManager;
    private GameServerInstance gameServerInstance;
    private Player player1;
    private Game game;
    @BeforeEach
    void setUp() {
        gameManager = new GameManager();
        gameServerInstance = new GameServerInstance();
        player1 = new Player("user1", gameServerInstance, 0);

        int gameID = gameManager.createGame(4, "Player1", 1); // Esempio di creazione di un gioco con 4 giocatori
        game = gameManager.getGameList().get(gameID);
    }

    @Test
    void testCreateGame() {
    }

    @Test
    void testJoinGame() {
        String nick = "TestPlayer";
        int clientID = 123;

        gameManager.joinGame(nick, game.getIdGame(), clientID);

        assertNotNull(game.getPlayer(nick));
        assertEquals(clientID, game.getPlayer(nick).getClientID());

    }

    @Test
    void testDeleteGame() {
        List<Integer> gameIDsBeforeDelete = gameManager.getGameIDs();
        assertEquals(1, gameIDsBeforeDelete.size());

        int gameIDToDelete = gameIDsBeforeDelete.getFirst();

        gameManager.deleteGame(gameIDToDelete);

        List<Integer> gameIDsAfterDelete = gameManager.getGameIDs();
        assertEquals(0, gameIDsAfterDelete.size());
    }

    @Test
    void testStartGame() {
        int gameID = game.getIdGame();

        assertEquals(GameState.WAITING_FOR_PLAYERS, game.getState());

        gameManager.startGame(gameID);

        assertEquals(GameState.CHOOSING_STARTER_CARDS, game.getState());
    }
}