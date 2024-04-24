package it.polimi.ingsfw.ingsfwproject.Network2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallbackInterface extends Remote {
    void update(String update) throws RemoteException;
}
