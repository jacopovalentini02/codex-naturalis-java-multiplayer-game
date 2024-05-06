package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Model.Card;
import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class PickMessage extends Message {
    Card card;
    public PickMessage(int clientID, Card card) {
        super(clientID, MessageType.PICK);
        this.card=card;
    }
    public Card getCard() {
        return card;
    }
}
