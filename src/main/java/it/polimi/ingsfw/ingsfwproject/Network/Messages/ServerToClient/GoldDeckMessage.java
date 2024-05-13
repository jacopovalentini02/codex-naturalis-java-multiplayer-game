package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Deck;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

public class GoldDeckMessage extends ServerToClientMessage implements Serializable {
    private final Deck goldDeck;

    public GoldDeckMessage(int clientID, Deck deck) {
        super(clientID);
        this.goldDeck=deck;
    }

    public Deck getGoldDeck() {
        return goldDeck;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setGoldDeck(goldDeck);
        client.getView().notifyGoldDeckUpdate();
    }
}
