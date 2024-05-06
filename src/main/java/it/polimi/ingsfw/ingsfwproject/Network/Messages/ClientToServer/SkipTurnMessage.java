package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class SkipTurnMessage extends Message {

    public SkipTurnMessage(int clientID, MessageType messageType) {
        super(clientID, messageType.SKIP_TURN);
    }
}
