package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Server.AbstractHandler;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import it.polimi.ingsfw.ingsfwproject.Network.Server.Handler;
import it.polimi.ingsfw.ingsfwproject.Network.Server.Server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The RMIHandlerClient class handles the communication between the RMI client and the server.
 * It implements the Handler interface and extends UnicastRemoteObject for remote communication.
 */
public class RMIHandlerClient extends UnicastRemoteObject implements Handler {
    private final RMIClient client;
    private final int clientID;

    /**
     * Constructs a new RMIHandlerClient.
     *
     * @param c the RMIClient associated with this handler
     * @param clientID the unique ID of the client
     * @throws RemoteException if a remote communication error occurs
     */
    protected RMIHandlerClient(RMIClient c, int clientID) throws RemoteException {
        this.clientID = clientID;
        this.client = c;
    }

    /**
     * Sends a message to the client if the message is intended for this client or for all clients.
     *
     * @param message the message to send
     */
    @Override
    public void sendMessage(Message message) {
        if (message.getClientID() == clientID || message.getClientID() == -10){
            client.receiveMessage(message);
        }
    }

    /**
     * Returns the client ID.
     *
     * @return the client ID
     */
    @Override
    public int getClientID() {
        return this.clientID;
    }


}
