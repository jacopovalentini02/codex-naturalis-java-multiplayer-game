package it.polimi.ingsfw.ingsfwproject.Network.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIFactory extends Remote {

    public Handler getServerHandler() throws RemoteException;

    public void setClientHandler(Handler clientHandler) throws RemoteException;

}
