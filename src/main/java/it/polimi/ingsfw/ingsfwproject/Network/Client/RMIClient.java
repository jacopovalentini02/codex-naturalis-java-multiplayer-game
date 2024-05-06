package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.io.IOException;
import java.rmi.RemoteException;

public class RMIClient extends Client{

    private RMIHandlerClient handler;

    public RMIClient(String ip, int port) throws RemoteException {
        super(ip, port);
    }

    @Override
    public void startConnection() throws Exception {

    }

    @Override
    public void sendMessage(Message message) throws IOException {

    }

    @Override
    public void disconnect() throws Exception {

    }

    public void receiveMessage(Message m) {
        handleMessage(m);
    }
}
