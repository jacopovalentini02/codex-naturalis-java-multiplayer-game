package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private LobbyController lobbyController;

    //todo che tipo di coda scegliere. Candidata: ConcurredLinkedQueue oppure array list che va sincronizzata

    private ArrayList<Handler> handlers; //Handler ha dentro client associato

    private ArrayList<GameServerInstance> games;

    private int clientsCounter;

    public void startsServers(){
        //Socket server e rmi server partono
        this.startRMIServer();
        this.startSocketServer();

    }

    public void readQueue(){

    }

    public void addToQueue(){

    }

    public synchronized int getClientsCounter(){
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

    public void startSocketServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(1337); } catch (IOException e) {
            System.err.println(e.getMessage()); // porta non disponibile
            return; }
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Connessione stabilita con client: "+socket.getInetAddress());
                SocketHandler socketHandler=new SocketHandler(socket, getClientsCounter(), this);
                handlers.add(socketHandler);
                executor.submit(socketHandler);
            } catch(IOException e) {
                break; // entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }
}
