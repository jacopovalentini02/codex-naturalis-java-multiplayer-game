package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.MessageType;

public class InvalidRequestMessage extends Message {

    public InvalidRequestMessage() {
        super(MessageType.INVALID_REQUEST);
    }
}
