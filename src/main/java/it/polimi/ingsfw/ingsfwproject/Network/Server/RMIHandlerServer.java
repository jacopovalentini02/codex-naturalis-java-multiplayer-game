package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.rmi.RemoteException;

public class RMIHandlerServer extends AbstractHandler{
    protected RMIHandlerServer() throws RemoteException {}

    @Override
    public void sendMessage(Message message) {
        handleMessageIn(message);
    }

}
