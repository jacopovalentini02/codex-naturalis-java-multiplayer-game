package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.io.IOException;

public class RMIClient extends Client{

    public RMIClient(String ip, int port) {
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

    public void receiveMessage(Message m) throws IOException, ClassNotFoundException {
        handleMessage(m);
    }
}
