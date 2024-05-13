package it.polimi.ingsfw.ingsfwproject.Network.Messages;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;

import java.io.Serializable;

public abstract class ClientToServerMessage extends Message implements Serializable {
    int clientID;
    private MessageType type;

    private boolean isForServer;

    public ClientToServerMessage(int clientID, MessageType messageType, Boolean isForServer) {
        this.type=messageType;
        this.clientID=clientID;
        this.isForServer=isForServer;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getClientID() {
        return clientID;
    }

    public abstract void execute(Controller controller);

    public boolean isForServer() {
        return isForServer;
    }
}
