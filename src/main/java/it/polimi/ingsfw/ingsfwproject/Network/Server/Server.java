package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * The Server class manages communication between clients and game instances through both
 * socket and RMI connections. It handles message processing, client connections,
 * and message distribution among clients.
 */
public class Server {
    private Lobby lobby;
    private BlockingQueue<Message> queue;
    private HashMap<Integer, Handler> handlers;//clientID-handler
    private HashMap<Integer, AbstractHandler> unistancedHandlers; //temporary list used to give an instance to handlers
    private HashMap<Integer, GameServerInstance> games; //gameID-serverinstance
    private int clientsCounter;

    /**
     * Constructor for the Server class. Initializes the GameManager,
     * Lobby, message queue, and various data structures.
     * Starts both RMI and socket servers.
     */
    public Server(){
        //Socket server e rmi server partono
        GameManager manager = new GameManager();
        lobby = new Lobby(manager, this);
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

    /**
     * Reads messages from the queue and processes them using the Lobby.
     */
    public void readQueue(){//read and process messages in the queue
        System.out.println("Reader Thread started");
        while (true){
            try {
                Message toProcess = queue.take();
                //processMessage((ClientToServerMessage) toProcess);
                ((ClientToServerMessage) toProcess).execute(lobby);
            } catch (InterruptedException e){
                System.out.println("Reader thread interrotto");
                return;
            }
        }
    }

    /**
     * This method links the client's handler to the specific game instance they are part of.
     *
     * @param gameInstance The GameServerInstance to associate the handler with.
     * @param clientID     The client's ID.
     * @param nickname     The client's nickname.
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

    /**
     * Sends a response message to clients based on the recipient client ID.
     * If clientID is -10, broadcasts the message to all clients.
     *
     * @param m The ServerToClientMessage to send.
     */
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

    /**
     * Adds a message to the server's message queue for processing.
     *
     * @param message The Message to add to the queue.
     */
    public void addToQueue(Message message){
        System.out.println("Adding message to queue");
        this.queue.add(message);
    }

    /**
     * Retrieves and increments the client counter for assigning client IDs.
     *
     * @return The current client counter value.
     */
    public synchronized int getClientsCounter(){
        int counter = clientsCounter;
        clientsCounter++;
        return counter;
    }

    /**
     * Adds a client handler to the handlers map.
     *
     * @param clientID The client's ID.
     * @param handler  The Handler instance for the client.
     */
    public void addHandler(Integer clientID, Handler handler){
        this.handlers.put(clientID, handler);
    }

    /**
     * Starts the RMI server by creating the RMI registry and binding the RMIFactoryImpl instance.
     */
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

    /**
     * Starts the socket server to accept incoming client connections.
     * Uses an ExecutorService for handling multiple client connections concurrently.
     */
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
                System.out.println("Connection established with client: "+socket.getInetAddress());
                int id=getClientsCounter();
                SocketHandler socketHandler=new SocketHandler(socket,id, this);
                this.addHandler(id, socketHandler);
                this.addUnistancedHandler(id, socketHandler);
                executor.submit(socketHandler);
            } catch(IOException e) {
                break;
            }
        }
        executor.shutdown();
    }

    /**
     * Adds an AbstractHandler instance to the unistancedHandlers map.
     * This method is used to temporarily store handlers before linking them to game instances.
     *
     * @param clientID The client's ID.
     * @param h        The AbstractHandler instance.
     */
    public void addUnistancedHandler(int clientID, AbstractHandler h){
        this.unistancedHandlers.put(clientID, h);
    }

}
