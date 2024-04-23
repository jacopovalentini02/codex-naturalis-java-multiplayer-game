package it.polimi.ingsfw.ingsfwproject.Network.Actions;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = -3591419940875947796L;
    private final int senderID;
    private final MessageType messageType;

    public Message(int senderID, MessageType messageType) {
        this.senderID = senderID;
        this.messageType = messageType;
    }
}
