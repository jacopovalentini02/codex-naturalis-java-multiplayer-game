package it.polimi.ingsfw.ingsfwproject.Network.Messages;

import java.io.Serializable;

/**
 * Represents a generic message in the network communication.
 * This abstract class is extended by specific message types.
 */
public abstract class Message implements Serializable {
    int clientID;

    /**
     * Gets the client ID associated with this message.
     *
     * @return the client ID
     */
    public int getClientID(){
        return this.clientID;
    }
}
