package it.polimi.ingsfw.ingsfwproject.Network.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RMIFactory interface defines the methods that must be implemented by any remote object
 * acting as an RMI factory for creating and managing client-server communication handlers.
 */
public interface RMIFactory extends Remote {

    /**
     * Retrieves the server-side handler for client-server communication.
     *
     * @return The Handler object representing the server-side handler.
     * @throws RemoteException If there is an issue with RMI communication.
     */
    public Handler getServerHandler() throws RemoteException;

    /**
     * Sets the client-side handler for client-server communication.
     *
     * @param clientHandler The Handler object representing the client-side handler.
     * @throws RemoteException If there is an issue with RMI communication.
     */
    public void setClientHandler(Handler clientHandler) throws RemoteException;

}
