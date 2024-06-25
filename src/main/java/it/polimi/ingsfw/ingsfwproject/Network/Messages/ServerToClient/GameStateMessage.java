package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.GameState;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

/**
 * Message sent from server to client to communicate the current game state.
 */
public class GameStateMessage extends ServerToClientMessage implements Serializable {
    GameState gameState;

    /**
     * Constructs a GameStateMessage with the specified client ID and game state.
     *
     * @param clientID the ID of the client receiving the message
     * @param gameState the current game state
     */
    public GameStateMessage(int clientID, GameState gameState) {
        super(clientID);
        this.gameState=gameState;
    }

    /**
     * Notifies the client's view about the updated game state
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setState(gameState);

        client.getView().notifyGameState(gameState);
    }
}
