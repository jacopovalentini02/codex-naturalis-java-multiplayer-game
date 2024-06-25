package it.polimi.ingsfw.ingsfwproject.Network.Messages;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.View.View;
import it.polimi.ingsfw.ingsfwproject.View.VirtualView;

import java.io.Serializable;

/**
 * Represents a message sent from the server to the client.
 * This abstract class is extended by specific server-to-client message types.
 */
public abstract class ServerToClientMessage extends Message implements Serializable {
    int clientID;

    /**
     * Constructs a ServerToClientMessage with the specified client ID.
     *
     * @param clientID the ID of the client to whom the message is sent
     */
    public ServerToClientMessage(int clientID){
        this.clientID = clientID;
    }

    /**
     * Executes the message on the specified client.
     *
     * @param client the client on which to execute the message
     */
    public abstract void execute(Client client);

    /**
     * Gets the client ID associated with this message.
     *
     * @return the client ID
     */
    public int getClientID(){
        return this.clientID;
    }

}
