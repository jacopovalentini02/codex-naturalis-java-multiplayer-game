package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

//Message sent from server after the connection is established to set clientID
public class FirstMessage extends ServerToClientMessage implements Serializable {
    public FirstMessage(int clientID) {
        super(clientID);
    }

    @Override
    public void execute(Client client) {
        client.setClientID(this.getClientID());
        client.getView().displayFirstMessage(getClientID());
    }
}
