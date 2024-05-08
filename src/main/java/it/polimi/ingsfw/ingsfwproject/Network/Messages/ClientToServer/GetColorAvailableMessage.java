package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class GetColorAvailableMessage extends Message {
    public GetColorAvailableMessage(int clientID) {
        super(clientID, MessageType.ASK_FOR_COLORS);
    }
}
