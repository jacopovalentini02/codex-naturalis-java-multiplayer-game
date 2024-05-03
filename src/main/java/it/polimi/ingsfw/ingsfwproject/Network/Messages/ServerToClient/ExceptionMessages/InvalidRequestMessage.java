package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class InvalidRequestMessage extends Message {

    public InvalidRequestMessage(int clientID) {
        super(clientID, MessageType.INVALID_REQUEST);
    }
}
