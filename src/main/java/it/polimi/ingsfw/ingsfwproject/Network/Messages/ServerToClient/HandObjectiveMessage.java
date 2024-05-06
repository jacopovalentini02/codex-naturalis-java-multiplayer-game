package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Card;
import it.polimi.ingsfw.ingsfwproject.Model.ObjectiveCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.util.ArrayList;

public class HandObjectiveMessage extends Message {
    private ArrayList<ObjectiveCard> cards;
    public HandObjectiveMessage(int clientID, ArrayList<ObjectiveCard> cards) {
        super(clientID, MessageType.HAND_OBJECTIVE);
        this.cards=cards;
    }

    public ArrayList<ObjectiveCard> getCards() {
        return cards;
    }
}
