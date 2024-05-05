package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Deck;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class GoldDeckMessage extends Message {
    private Deck goldDeck;

    public GoldDeckMessage(int clientID, Deck deck) {
        super(clientID, MessageType.GOLD_DECK);
        this.goldDeck=deck;
    }

    public Deck getGoldDeck() {
        return goldDeck;
    }
}
