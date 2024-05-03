package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.MessageType;

public class NickAlreadyTakenMessage extends Message {
    public NickAlreadyTakenMessage() {
        super(MessageType.NICK_ALREADY_TAKEN);
    }
}
