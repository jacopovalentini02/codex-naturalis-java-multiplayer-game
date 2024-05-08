package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class ObjectiveCardChosenMessage extends Message {
    public String getNickname() {
        return nickname;
    }

    String nickname;
    int cardID;
    public ObjectiveCardChosenMessage(int clientID, String nickname, int cardID) {
        super(clientID, MessageType.CHOSEN_OBJECTIVE_CARD);
        this.cardID=cardID;
    }


    public int getCardID() {
        return cardID;
    }
}
