package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Deck;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

/**
 * Message sent from server to client to update the resource deck.
 */
public class ResourceDeckMessage extends ServerToClientMessage implements Serializable {
    private final Deck resourceDeck;

    /**
     * Constructs a ResourceDeckMessage with the specified client ID and resource deck.
     *
     * @param clientID     the ID of the client receiving the message
     * @param deck the updated resource deck
     */
    public ResourceDeckMessage(int clientID, Deck deck) {
        super(clientID);
        this.resourceDeck=deck;
    }

    /**
     * Sets the resource deck in the client's virtual view.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setResourceDeck(resourceDeck);
        client.getView().notifyResourceDeckUpdate();
    }
}
