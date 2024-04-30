package it.polimi.ingsfw.ingsfwproject.Network2.Messages;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private MessageType type;

    public Message(MessageType messageType) {
        this.type=messageType;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
