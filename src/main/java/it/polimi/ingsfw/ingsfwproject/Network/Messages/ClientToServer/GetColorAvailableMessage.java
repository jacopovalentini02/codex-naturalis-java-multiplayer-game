package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class GetColorAvailableMessage extends Message {
    public GetColorAvailableMessage(int clientID, MessageType messageType) {
        super(clientID, messageType.ASK_FOR_COLORS);
    }
}
