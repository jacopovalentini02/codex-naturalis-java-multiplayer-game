package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private LobbyController lobbyController;

    //todo che tipo di coda scegliere. Candidata: ConcurredLinkedQueue oppure array list che va sincronizzata

    private ArrayList<Handler> handlers; //clientID-handler

    private ArrayList<GameServerInstance> games;

    private int clientsCounter;

    public void startsServers(){
        //Socket server e rmi server partono
        this.startRMIServer();

    }

    public void readQueue(){

    }

    public void addToQueue(){

    }

    public int getClientsCounter(){
        int counter = clientsCounter;
        clientsCounter++;
        return counter;
    }

    public void addHandler(Handler h){
        this.handlers.add(h);
    }

    private void startRMIServer(){
        try {
            LocateRegistry.createRegistry(1099);
            RMIFactoryImpl factory = new RMIFactoryImpl(this);
            Naming.rebind("Factory", factory);
            System.out.println("Factory has been successfully registered");
        } catch (RemoteException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


}
