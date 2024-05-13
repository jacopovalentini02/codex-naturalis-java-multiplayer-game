package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Deck;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

public class ResourceDeckMessage extends ServerToClientMessage implements Serializable {
    private final Deck resourceDeck;
    public ResourceDeckMessage(int clientID, Deck deck) {
        super(clientID);
        this.resourceDeck=deck;
    }

    public Deck getResourceDeck() {
        return resourceDeck;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setResourceDeck(resourceDeck);
        client.getView().notifyResourceDeckUpdate();
    }
}
