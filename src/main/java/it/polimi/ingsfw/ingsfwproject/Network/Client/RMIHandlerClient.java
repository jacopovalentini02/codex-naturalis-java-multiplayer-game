package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Server.AbstractHandler;

import java.rmi.RemoteException;

public class RMIHandlerClient extends AbstractHandler {
    protected RMIHandlerClient() throws RemoteException {}

    @Override
    public void sendMessage(Message message) {

    }
}
