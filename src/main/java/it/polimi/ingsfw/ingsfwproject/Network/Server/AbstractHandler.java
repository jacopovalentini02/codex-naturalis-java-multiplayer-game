package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The AbstractHandler class provides a base implementation of the Handler interface and extends UnicastRemoteObject.
 * It manages communication between the server and a client identified by a unique client ID.
 */
abstract public class AbstractHandler extends UnicastRemoteObject implements Handler {
    private final int clientID;
    private Server server;
    private GameServerInstance gameServerInstance;

    /**
     * Constructs an AbstractHandler with a client ID and a reference to the server.
     *
     * @param clientID The unique ID assigned to the client.
     * @param server   The Server instance managing this handler.
     * @throws RemoteException If there is a communication-related exception.
     */
    protected AbstractHandler(int clientID, Server server) throws RemoteException {
        this.clientID = clientID;
        this.server = server;
    }

//    protected AbstractHandler(int clientID) throws RemoteException{
//        this.clientID = clientID;
//    }

    /**
     * Sends a message to the client.
     *
     * @param message The Message object to send.
     */
    public abstract void sendMessage(Message message);

    /**
     * Handles incoming messages from the client.
     *
     * @param message The Message object received from the client.
     */
    void handleMessageIn(Message message){
        ClientToServerMessage messFromClient=(ClientToServerMessage) message;
        if(messFromClient.isForServer())
            server.addToQueue(message);
        else
            if (gameServerInstance != null) //TODO: forse inutile, rimuovere
                gameServerInstance.addToQueue(message);
    }

    /**
     * Sets the GameServerInstance associated with this handler.
     *
     * @param instance The GameServerInstance to set.
     */
    public void setGameServerInstance(GameServerInstance instance){
        this.gameServerInstance = instance;
    }

    /**
     * Retrieves the client ID associated with this handler.
     *
     * @return The client ID.
     */
    public int getClientID(){
        return this.clientID;
    }



}
