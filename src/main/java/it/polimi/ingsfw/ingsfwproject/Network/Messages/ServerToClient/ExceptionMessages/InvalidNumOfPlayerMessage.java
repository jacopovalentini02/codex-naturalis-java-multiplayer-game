package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.io.Serializable;

public class InvalidNumOfPlayerMessage extends Message implements Serializable {

    public InvalidNumOfPlayerMessage(int clientID) {
        super(clientID,MessageType.INVALID_NUM_OF_PLAYERS);
    }


}
