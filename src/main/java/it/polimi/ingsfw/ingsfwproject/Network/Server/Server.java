package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameFullException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameNotExistingException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NickAlreadyTakenException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Model.Player;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.JoinGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExcpetionMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.GameJoinedMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.SendGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

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
import java.util.concurrent.*;

public class Server {
    private LobbyController lobbyController;
    private BlockingQueue<Message> queue;
    private HashMap<Integer, Handler> handlers;//clientID-handler
    private HashMap<Integer, AbstractHandler> unistancedHandlers; //temporary list used to give an instance to handlers
    private HashMap<Integer, GameServerInstance> games; //gameID-serverinstance
    private int clientsCounter;

    public Server(){
        //Socket server e rmi server partono
        GameManager manager = new GameManager();
        lobbyController = new LobbyController(manager, this);
        queue = new LinkedBlockingQueue<Message>();
        handlers = new HashMap<Integer, Handler>();
        games = new HashMap<Integer, GameServerInstance>();
        unistancedHandlers = new HashMap<Integer, AbstractHandler>();
        this.clientsCounter = 0;
        Thread readerThread = new Thread(this::readQueue);
        readerThread.start();
        this.startRMIServer();
        this.startSocketServer();

    }

    public void readQueue(){//read and process messages in the queue
        System.out.println("Reader Thread started");
        while (true){
            try {
                Message toProcess = queue.take();
                //processMessage((ClientToServerMessage) toProcess);
                ((ClientToServerMessage) toProcess).execute(lobbyController);
            } catch (InterruptedException e){
                System.out.println("Reader thread interrotto");
                return;
            }
        }
    }

    /*public void processMessage(ClientToServerMessage message){
    //TODO: cambiare - fatto direttamente in readQueue

        lobbyController.handleMessage(message);


        switch (message.getType()){
            case CREATE_GAME: {
                CreateGameMessage m = (CreateGameMessage) message;
                int createdGameID;
                try {
                    createdGameID = this.lobbyController.createGame(m.getNumPlayer(), m.getNickname());
                } catch (NotValidNumOfPlayerException e) {
                    this.sendResponse(new ExcpetionMessage(m.getClientID(), e.getMessage()));
                    return;
                }
                //gets the gameserverinstance, puts it in the map, puts the client handler in the gameserverinstance
                GameServerInstance gameInstance = lobbyController.getGameServerInstance(createdGameID);
                this.games.put(createdGameID, gameInstance);
                setHandlersAndInstance(createdGameID, gameInstance, m.getClientID(), m.getNickname(), m);
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
                } catch (GameNotExistingException | GameFullException | NickAlreadyTakenException e){
                    this.sendResponse(new ExcpetionMessage(m.getClientID(), e.getMessage()));
                    return;
                }

                GameServerInstance gameInstance = games.get(joinedGameID); //gets the instance for the game that the player has joined
                setHandlersAndInstance(joinedGameID, gameInstance, m.getClientID(), m.getNickname(), m);

                //check if the game has reached the desired number of players. If so, start it
                lobbyController.checkIfGameNeedsToBeStarted(joinedGameID);

                break;
            }

        }

    }
*/
    public void setHandlersAndInstance(GameServerInstance gameInstance, int clientID, String nickname) {
        Handler handlerToPutIntoInstance = handlers.get(clientID);
        gameInstance.setHandler(clientID, handlerToPutIntoInstance);

        AbstractHandler handlerToSetInstanceInto = unistancedHandlers.get(clientID);
        handlerToSetInstanceInto.setGameServerInstance(gameInstance);

        //TODO togliere?
//        Player addedPlayer = gameInstance.getPlayer(nickname);
//
//        if (addedPlayer != null){ //adding the new player to the game server instance
//            gameInstance.addPlayer(addedPlayer, clientID);
//        } else {
//            throw new RuntimeException();
//        }

    }


    public synchronized void sendResponse(ServerToClientMessage m) {
        System.out.println("Sending response");
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
        System.out.println("Adding message to queue");
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
                this.addUnistancedHandler(id, socketHandler);
                executor.submit(socketHandler);
            } catch(IOException e) {
                break; // entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }

    public void addUnistancedHandler(int clientID, AbstractHandler h){
        this.unistancedHandlers.put(clientID, h);
    }

}
