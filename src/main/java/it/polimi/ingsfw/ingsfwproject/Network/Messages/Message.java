package it.polimi.ingsfw.ingsfwproject.Network.Messages;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;

import java.io.Serializable;

public abstract class Message implements Serializable {
    int clientID;
    private MessageType type;

    public Message(int clientID, MessageType messageType) {
        this.type=messageType;
        this.clientID=clientID;
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
}
