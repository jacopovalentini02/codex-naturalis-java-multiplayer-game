package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class NotExistingGameMessage extends Message {
    public NotExistingGameMessage(int clientID){
        super(clientID, MessageType.GAME_NOT_EXISTS);
    }
}
