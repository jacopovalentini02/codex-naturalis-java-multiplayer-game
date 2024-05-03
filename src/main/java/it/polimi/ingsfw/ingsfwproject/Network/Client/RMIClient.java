package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.io.IOException;

public class RMIClient extends Client{

    public RMIClient(String ip, int port, String nickname) {
        super(ip, port, nickname);
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

    @Override
    public void receiveMessage() throws IOException, ClassNotFoundException {

    }
}
