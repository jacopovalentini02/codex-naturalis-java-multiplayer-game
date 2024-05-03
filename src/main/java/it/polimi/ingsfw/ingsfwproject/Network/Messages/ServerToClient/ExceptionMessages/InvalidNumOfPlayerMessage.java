package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.MessageType;

import java.io.Serializable;

public class InvalidNumOfPlayerMessage extends Message implements Serializable {

    public InvalidNumOfPlayerMessage() {
        super(MessageType.INVALID_NUM_OF_PLAYERS);
    }


}
