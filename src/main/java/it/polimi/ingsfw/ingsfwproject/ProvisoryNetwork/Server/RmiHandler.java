package it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiHandler extends UnicastRemoteObject implements GameInterface, Handler {

    protected RmiHandler() throws RemoteException {
    }
}
