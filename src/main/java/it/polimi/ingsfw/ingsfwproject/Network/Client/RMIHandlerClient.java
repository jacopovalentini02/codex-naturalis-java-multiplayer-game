package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Server.AbstractHandler;
import it.polimi.ingsfw.ingsfwproject.Network.Server.Server;

import java.rmi.RemoteException;

public class RMIHandlerClient extends AbstractHandler {
    private final RMIClient client;
    protected RMIHandlerClient(RMIClient c, int clientID) throws RemoteException {
        super(clientID);
        this.client = c;
    }

    @Override
    public void sendMessage(Message message) {client.receiveMessage(message);}
}
