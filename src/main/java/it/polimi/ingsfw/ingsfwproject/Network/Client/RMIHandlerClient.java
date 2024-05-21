package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Server.AbstractHandler;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import it.polimi.ingsfw.ingsfwproject.Network.Server.Handler;
import it.polimi.ingsfw.ingsfwproject.Network.Server.Server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIHandlerClient extends UnicastRemoteObject implements Handler {
    private final RMIClient client;
    private final int clientID;


    protected RMIHandlerClient(RMIClient c, int clientID) throws RemoteException {
        this.clientID = clientID;
        this.client = c;
    }

    @Override
    public void sendMessage(Message message) {
        if (message.getClientID() == clientID || message.getClientID() == -10){
            //System.out.println("Arrivato messaggio dal server");
            client.receiveMessage(message);
        }
    }

    @Override
    public int getClientID() {
        return this.clientID;
    }


}
