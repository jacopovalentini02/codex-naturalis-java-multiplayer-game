package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class NickAlreadyTakenMessage extends Message {
    public NickAlreadyTakenMessage(int clientID) {
        super(clientID, MessageType.NICK_ALREADY_TAKEN);
    }
}
