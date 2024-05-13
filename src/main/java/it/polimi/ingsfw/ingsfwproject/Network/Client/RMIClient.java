package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import it.polimi.ingsfw.ingsfwproject.Network.Server.Handler;
import it.polimi.ingsfw.ingsfwproject.Network.Server.RMIFactory;
import it.polimi.ingsfw.ingsfwproject.Network.Server.RMIHandlerServer;
import it.polimi.ingsfw.ingsfwproject.View.View;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient extends Client implements Serializable {

    private Handler handler;

    public RMIClient(String ip, int port, View view){
        super(ip, port, view);
    }

    @Override
    public void startConnection() throws Exception {
        Registry registry = LocateRegistry.getRegistry(this.getIp(), this.getPort());
        RMIFactory serverFactory = (RMIFactory) registry.lookup("Factory");

        if (serverFactory == null){
            System.out.println("Error while obtaining RMI Factory Object");
            this.setConnected(false);
            throw new RuntimeException();
        } else {
            System.out.println("Successfully connected to RMI Factory Object");
        }

        Handler serverHandler = (Handler) serverFactory.getServerHandler();

        this.handler = serverHandler;

        int clientID = serverHandler.getClientID();

        super.setClientID(clientID);

        //creazione dell'handler client
        Handler clientHandler = new RMIHandlerClient(this, clientID);


        //passaggio al server del client handler
        serverFactory.setClientHandler(clientHandler);
        System.out.println("pippo");
        this.setConnected(true);
    }


    @Override
    public void sendMessage(Message message) throws IOException {
        handler.sendMessage(message);
    }

    @Override
    public void disconnect() throws Exception {

    }

    public void receiveMessage(Message m) {
        handleMessage(m);
    }
}
