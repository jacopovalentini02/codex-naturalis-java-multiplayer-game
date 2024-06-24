package it.polimi.ingsfw.ingsfwproject.Network.Messages;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;

import java.io.Serializable;

/**
 * Represents a message sent from the client to the server.
 * This abstract class should be extended by specific message types.
 */
public abstract class ClientToServerMessage extends Message implements Serializable {
    int clientID;
    private MessageType type;
    private boolean isForServer;

    /**
     * Constructs a new ClientToServerMessage.
     *
     * @param clientID the ID of the client sending the message
     * @param messageType the type of the message
     * @param isForServer a boolean indicating if the message is intended for the server
     */
    public ClientToServerMessage(int clientID, MessageType messageType, Boolean isForServer) {
        this.type=messageType;
        this.clientID=clientID;
        this.isForServer=isForServer;
    }

    /**
     * Gets the client ID.
     *
     * @return the client ID
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * Executes the action associated with this message on the given controller.
     *
     * @param controller the controller to execute the action on
     */
    public abstract void execute(Controller controller);

    /**
     * Checks if the message is intended for the server or for gameServerInstance.
     *
     * @return true if the message is for the server, false otherwise
     */
    public boolean isForServer() {
        return isForServer;
    }
}
