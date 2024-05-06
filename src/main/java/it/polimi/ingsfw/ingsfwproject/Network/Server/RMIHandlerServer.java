package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.rmi.RemoteException;

public class RMIHandlerServer extends AbstractHandler{
    protected RMIHandlerServer(int clientID, Server server) throws RemoteException {
        super(clientID, server);
    }

    @Override
    public void sendMessage(Message message) {
        handleMessageIn(message);
    }


}
