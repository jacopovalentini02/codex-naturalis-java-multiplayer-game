package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.ContentCounter;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class ContentCounterMessage extends Message {
    private ContentCounter contentCounter;

    public ContentCounterMessage(int clientID,  ContentCounter contentCounter) {
        super(clientID, MessageType.CONTENT_COUNTER);
        this.contentCounter=contentCounter;
    }

    public ContentCounter getContentCounter() {
        return contentCounter;
    }
}
