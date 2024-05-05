package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Deck;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class ResourceDeckMessage extends Message {
    private Deck resourceDeck;
    public ResourceDeckMessage(int clientID, Deck deck) {
        super(clientID, MessageType.RESOURCE_DECK);
        this.resourceDeck=deck;
    }

    public Deck getResourceDeck() {
        return resourceDeck;
    }
}
