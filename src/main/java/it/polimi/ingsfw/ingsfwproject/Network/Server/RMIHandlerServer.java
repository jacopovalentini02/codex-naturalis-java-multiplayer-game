package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.rmi.RemoteException;

/**
 * The RMIHandlerServer class extends AbstractHandler and represents a server-side handler
 * for managing RMI-based client-server communication.
 */
public class RMIHandlerServer extends AbstractHandler{
    /**
     * Constructs an RMIHandlerServer object with the specified client ID and Server instance.
     *
     * @param clientID The unique identifier of the client associated with this handler.
     * @param server   The Server instance managing the client-server communication.
     * @throws RemoteException If there is an issue with RMI communication.
     */
    protected RMIHandlerServer(int clientID, Server server) throws RemoteException {
        super(clientID, server);
    }

    /**
     * Sends a message to the client by processing it through the handleMessageIn method.
     *
     * @param message The Message object to be sent to the client.
     */
    @Override
    public void sendMessage(Message message) {
        handleMessageIn(message);
    }


}
