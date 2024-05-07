package it.polimi.ingsfw.ingsfwproject.Network.Server;

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
        return new RMIHandlerServer(server.getClientsCounter(), server);
    }

    @Override
    public synchronized void setClientHandler(Handler clientHandler) throws RemoteException {
        int clientID = clientHandler.getClientID();
        server.addHandler(clientID, clientHandler);
    }
}
