package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.util.ArrayList;

public class HandCardsMessage extends Message {
    private ArrayList<PlayableCard> handCards;
    public HandCardsMessage(int clientID, ArrayList<PlayableCard> handCards) {
        super(clientID, MessageType.HAND_CARDS);
        this.handCards=handCards;
    }

    public ArrayList<PlayableCard> getHandCards() {
        return handCards;
    }
}
