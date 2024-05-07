package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class ExcpetionMessage extends Message {
    String description;
    public ExcpetionMessage(int clientID, String description) {
        super(clientID, MessageType.EXCEPTION_MESSAGE);
        this.description=description;
    }

    public String getDescription() {
        return description;
    }
}
