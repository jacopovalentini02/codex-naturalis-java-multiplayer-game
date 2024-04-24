package it.polimi.ingsfw.ingsfwproject.Network2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientCallback extends UnicastRemoteObject implements ClientCallbackInterface {
    protected ClientCallback() throws RemoteException {
    }

    @Override
    public void update(String update) throws RemoteException {
        System.out.println(update);
    }
}
