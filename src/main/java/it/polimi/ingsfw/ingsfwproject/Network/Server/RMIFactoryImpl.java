package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.FirstMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The RMIFactoryImpl class implements the RMIFactory interface to provide
 * RMI factory functionalities for creating and managing client-server communication handlers.
 */
public class RMIFactoryImpl extends UnicastRemoteObject implements RMIFactory {

    private final Server server;

    /**
     * Constructs an RMIFactoryImpl object with the specified Server instance.
     *
     * @param s The Server instance used for managing client-server communication.
     * @throws RemoteException If there is an issue with RMI communication.
     */
    protected RMIFactoryImpl(Server s) throws RemoteException {
        this.server = s;
    }

    /**
     * Retrieves a new server-side handler for client-server communication.
     *
     * @return The Handler object representing the new server-side handler.
     * @throws RemoteException If there is an issue with RMI communication.
     */
    @Override
    public synchronized Handler getServerHandler() throws RemoteException {
        int newClientID = server.getClientsCounter();
        RMIHandlerServer newHandler = new RMIHandlerServer(newClientID, server);
        server.addUnistancedHandler(newClientID, newHandler);
        return newHandler;
    }

    /**
     * Sets the client-side handler for client-server communication on the server.
     * Adds the client handler to the server's handler map and sends a FirstMessage to the client.
     *
     * @param clientHandler The Handler object representing the client-side handler to set.
     * @throws RemoteException If there is an issue with RMI communication.
     */
    @Override
    public synchronized void setClientHandler(Handler clientHandler) throws RemoteException {
        int clientID = clientHandler.getClientID();
        server.addHandler(clientID, clientHandler);
        server.sendResponse(new FirstMessage(clientID));
    }
}
