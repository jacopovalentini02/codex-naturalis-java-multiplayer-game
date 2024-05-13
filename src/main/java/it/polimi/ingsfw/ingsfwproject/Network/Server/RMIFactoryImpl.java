package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.FirstMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIFactoryImpl extends UnicastRemoteObject implements RMIFactory {

    private final Server server;


    protected RMIFactoryImpl(Server s) throws RemoteException {
        this.server = s;
    }

    @Override
    public synchronized Handler getServerHandler() throws RemoteException {
        int newClientID = server.getClientsCounter();
        RMIHandlerServer newHandler = new RMIHandlerServer(newClientID, server);
        server.addUnistancedHandler(newClientID, newHandler);
        return newHandler;
    }

    @Override
    public synchronized void setClientHandler(Handler clientHandler) throws RemoteException {
        int clientID = clientHandler.getClientID();
        server.addHandler(clientID, clientHandler);
        server.sendResponse(new FirstMessage(clientID));
    }
}
