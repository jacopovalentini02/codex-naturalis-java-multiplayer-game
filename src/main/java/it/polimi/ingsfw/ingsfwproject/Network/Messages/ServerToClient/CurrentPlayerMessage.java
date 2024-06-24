package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

/**
 * Message from server to client indicating the current player.
 */
public class CurrentPlayerMessage extends ServerToClientMessage implements Serializable {
    private final String currentPlayer;

    /**
     * Constructs a CurrentPlayerMessage with the specified parameters.
     *
     * @param clientID      the ID of the client to whom this message is directed
     * @param currentPlayer the nickname of the current player
     */
    public CurrentPlayerMessage(int clientID, String currentPlayer) {
        super(clientID);
        this.currentPlayer=currentPlayer;
    }

    /**
     * Notifies the view about the current player.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setCurrentPlayer(currentPlayer);
        client.getView().notifyCurrentPlayer(currentPlayer);
    }
}
