package it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.ExceptionMessages;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.MessageType;

public class NotExistingGameMessage extends Message {
    public NotExistingGameMessage(){
        super(MessageType.GAME_NOT_EXISTS);
    }
}
