package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Deck;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

/**
 * Message sent from server to client to update the golden deck.
 */
public class GoldDeckMessage extends ServerToClientMessage implements Serializable {
    private final Deck goldDeck;

    /**
     * Constructs a GoldDeckMessage with the specified client ID and golden deck.
     *
     * @param clientID the ID of the client receiving the message
     * @param deck the updated golden deck
     */
    public GoldDeckMessage(int clientID, Deck deck) {
        super(clientID);
        this.goldDeck=deck;
    }

    /**
     * Sets the golden deck in the client's virtual view.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setGoldDeck(goldDeck);
        client.getView().notifyGoldDeckUpdate();
    }
}
