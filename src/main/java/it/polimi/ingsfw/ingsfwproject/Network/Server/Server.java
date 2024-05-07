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
    private HashMap<Integer, Handler> handlers; //clientID-handler
    private HashMap<Integer, GameServerInstance> games; //gameID-serverinstance
    private int clientsCounter;

    public Server(){
        //Socket server e rmi server partono
        GameManager manager = new GameManager();
        lobbyController = new LobbyController(manager);
        queue = new ConcurrentLinkedQueue<Message>();
        handlers = new HashMap<Integer, Handler>();
        games = new HashMap<Integer, GameServerInstance>();
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
                //gets the gameserverinstance, puts it in the map, puts the client handler in the gameserverinstance
                GameServerInstance gameInstance = lobbyController.getGameServerInstance(createdGameID);
                this.games.put(createdGameID, gameInstance);
                Handler requestingClientHandler = handlers.get(m.getClientID());
                gameInstance.setHandler(m.getClientID(), requestingClientHandler);
                this.sendResponse(new GameJoinedMessage(m.getClientID(), createdGameID, m.getNickname()));
                break;
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

                GameServerInstance gameInstance = games.get(joinedGameID); //gets the instance for the game that the player has joined
                Handler requestingClientHandler = handlers.get(m.getClientID()); //gets the handler of the player
                gameInstance.setHandler(m.getClientID(), requestingClientHandler);//puts the player's handler into the handlers map in GameServerInstamce

                this.sendResponse(new GameJoinedMessage(m.getClientID(), joinedGameID, m.getNickname()));
                break;
            }

        }

    }



    public synchronized void sendResponse(Message m) {
        try {
            if (m.getClientID() == -10){ //broadcast message
                for (Handler h: handlers.values())
                    h.sendMessage(m);
            } else {
                Handler recipient = handlers.get(m.getClientID());
                recipient.sendMessage(m);
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

    public void addHandler(Integer clientID, Handler handler){
        this.handlers.put(clientID, handler);
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
                this.addHandler(id, socketHandler);
                executor.submit(socketHandler);
            } catch(IOException e) {
                break; // entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }

}
