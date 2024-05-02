package it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LobbyHandler  extends UnicastRemoteObject implements GameInterface{
    protected LobbyHandler() throws RemoteException {
    }
}
