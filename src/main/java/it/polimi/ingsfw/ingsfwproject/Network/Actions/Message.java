package it.polimi.ingsfw.ingsfwproject.Network.Actions;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = -3591419940875947796L;
    private final String senderNick;
    private final MessageType messageType;

    public Message(String senderNick, MessageType messageType) {
        this.senderNick = senderNick;
        this.messageType = messageType;
    }
}
