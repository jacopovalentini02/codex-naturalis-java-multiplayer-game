package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Handler extends Remote {

    public void sendMessage(Message m) throws RemoteException;

    public int getClientID() throws RemoteException;
}
