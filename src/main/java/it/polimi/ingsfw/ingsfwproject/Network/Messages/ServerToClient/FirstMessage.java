package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

/**
 * Message sent from server after the connection is established to set clientID.
 * This message initializes the client with its assigned clientID.
 */
public class FirstMessage extends ServerToClientMessage implements Serializable {
    /**
     * Constructs a FirstMessage with the specified client ID.
     *
     * @param clientID the ID assigned to the client by the server
     */
    public FirstMessage(int clientID) {
        super(clientID);
    }

    /**
     * Sets the client's ID and notifies the view to display the first message.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.setClientID(this.getClientID());
        client.getView().displayFirstMessage(getClientID());
    }
}
