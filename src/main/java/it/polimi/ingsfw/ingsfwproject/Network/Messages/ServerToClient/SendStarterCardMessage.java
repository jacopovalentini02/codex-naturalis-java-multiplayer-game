package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.StarterCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class SendStarterCardMessage extends Message {
    private StarterCard starterCard;

    public SendStarterCardMessage(int clientID, StarterCard starterCard) {
        super(clientID, MessageType.STARTER_CARD);
        this.starterCard=starterCard;
    }

    public StarterCard getStarterCard() {
        return starterCard;
    }
}
