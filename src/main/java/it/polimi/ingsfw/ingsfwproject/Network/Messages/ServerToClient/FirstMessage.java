package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.io.Serializable;

//Message sent from server after the connection is established to set clientID
public class FirstMessage extends Message implements Serializable {
    int clientID;
    public FirstMessage(int clientID, MessageType messageType) {
        super(clientID, messageType);
    }

    public int getClientID() {
        return clientID;
    }
}
