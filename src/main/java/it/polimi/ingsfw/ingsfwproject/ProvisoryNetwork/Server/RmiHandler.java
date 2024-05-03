package it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiHandler extends UnicastRemoteObject implements GameInterface, Handler {

    protected RmiHandler() throws RemoteException {
    }

    @Override
    public Message sendMessage(Message m) {
        return null;
    }
}
