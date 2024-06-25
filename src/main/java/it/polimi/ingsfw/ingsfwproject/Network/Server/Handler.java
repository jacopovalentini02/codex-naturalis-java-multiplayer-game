package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The Handler interface defines the methods that must be implemented by any remote object
 * acting as a handler for communication with clients via RMI.
 */
public interface Handler extends Remote {

    /**
     * Sends a message to the client associated with this handler.
     *
     * @param m The Message object to be sent.
     * @throws RemoteException If there is an issue with RMI communication.
     */
    public void sendMessage(Message m) throws RemoteException;

    /**
     * Retrieves the client ID associated with this handler.
     *
     * @return The client ID integer value.
     * @throws RemoteException If there is an issue with RMI communication.
     */
    public int getClientID() throws RemoteException;

}
