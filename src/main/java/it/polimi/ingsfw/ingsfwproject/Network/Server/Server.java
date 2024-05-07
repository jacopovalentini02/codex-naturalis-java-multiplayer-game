package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameFullException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameNotExistingException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NickAlreadyTakenException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.JoinGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages.GameFullMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages.InvalidNumOfPlayerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages.NickAlreadyTakenMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages.NotExistingGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.GameJoinedMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.SendGameListMessage;

import java.io.IOException;
import java.io.SyncFailedException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private LobbyController lobbyController;
    private ConcurrentLinkedQueue<Message> queue;
    private ArrayList<Handler> handlers; //clientID-handler
    private ArrayList<GameServerInstance> games;
    private int clientsCounter;

    public Server(){
        //Socket server e rmi server partono
        GameManager manager = new GameManager();
        lobbyController = new LobbyController(manager);
        queue = new ConcurrentLinkedQueue<Message>();
        handlers = new ArrayList<Handler>();
        games = new ArrayList<GameServerInstance>();
        this.clientsCounter = 0;
        Thread readerThread = new Thread(this::readQueue);
        readerThread.start();
        this.startRMIServer();
        this.startSocketServer();

    }

    public void readQueue(){//read and process messages in the queue
        System.out.println("Reader Thread started");
        while (true){
            Message toProcess = queue.poll();
            if (toProcess != null){
                processMessage(toProcess);
            } else {
                //coda vuota
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    System.out.println("Interrupted Exception");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    public void processMessage(Message message){

        switch (message.getType()){
            case CREATE_GAME: {
                CreateGameMessage m = (CreateGameMessage) message;
                int createdGameID;
                try {
                    createdGameID = this.lobbyController.createGame(m.getNumPlayer(), m.getNickname());
                } catch (NotValidNumOfPlayerException e) {
                    this.sendResponse(new InvalidNumOfPlayerMessage(m.getClientID()));
                    return;
                }
                this.sendResponse(new GameJoinedMessage(m.getClientID(), createdGameID, m.getNickname()));
                break;

                //todo creo un gameserverinstance ->lo setto nell'handler
            }
            case GET_GAME_LIST: {
                GetGameListMessage m = (GetGameListMessage) message;
                this.sendResponse(new SendGameListMessage(m.getClientID(), lobbyController.getGameList()));
                break;
            }
            case JOIN_GAME: {
                JoinGameMessage m = (JoinGameMessage) message;
                int joinedGameID;

                try {
                    joinedGameID = this.lobbyController.joinExistingGame(m.getNickname(), m.getGameID());
                } catch (GameNotExistingException e){
                    this.sendResponse(new NotExistingGameMessage(m.getClientID()));
                    return;
                } catch (GameFullException e){
                    this.sendResponse(new GameFullMessage(m.getClientID()));
                    return;
                } catch (NickAlreadyTakenException e){
                    this.sendResponse(new NickAlreadyTakenMessage(m.getClientID()));
                    return;
                }

                this.sendResponse(new GameJoinedMessage(m.getClientID(), joinedGameID));
                break;
            } //aggiungere caso default

        }

    }



    public synchronized void sendResponse(Message m) {
        try {
            for (Handler handler : handlers) {
                if (m.getClientID() == -10 || m.getClientID() == handler.getClientID()) { //se messaggio in broadcast oppure per il client associato
                    handler.sendMessage(m);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }

    public void addToQueue(Message message){
        this.queue.add(message);
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
                int id=getClientsCounter();
                System.out.println("client id: "+id);
                SocketHandler socketHandler=new SocketHandler(socket,id, this);
                handlers.add(socketHandler);
                executor.submit(socketHandler);
            } catch(IOException e) {
                break; // entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }

}
