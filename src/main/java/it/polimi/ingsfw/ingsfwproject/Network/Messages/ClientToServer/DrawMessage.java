package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;
import it.polimi.ingsfw.ingsfwproject.Model.Deck;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class DrawMessage extends Message{
    Deck deck;
    public DrawMessage(int clientID, Deck deck) {
        super(clientID, MessageType.DRAW);
        this.deck = deck;
    }
    public Deck getDeck() {
        return deck;
    }
}
