package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessage;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The GameServerInstance class represents an instance of the game server that manages interactions
 * with multiple clients (handlers) and processes messages related to the game state.
 */
public class GameServerInstance {
    private GameController gameController;
    private BlockingDeque<Message> queue;
    private HashMap<Integer, Handler> handlers;
    private HashMap<Player, Integer> players;


    private ConcurrentHashMap<Integer, Long> heartbeats;

    private boolean inGame = true;

    /**
     * Constructs a GameServerInstance initializing the message queue and starting the message processing thread.
     */
    public GameServerInstance() {
        this.queue = new LinkedBlockingDeque<>(); // Inizializzazione della coda
        this.handlers = new HashMap<>();
        this.players = new HashMap<>();
        this.heartbeats = new ConcurrentHashMap<>();
        Thread instanceReaderThread = new Thread(this::readQueue);
        startDisconnectionCheckThread();
        instanceReaderThread.start();
    }

    /**
     * Reads messages from the queue and processes them.
     */
    public void readQueue(){
        try {
            while (true) {
                Message message = queue.take(); // Rimane in attesa finché non c'è un messaggio disponibile
                processMessage(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    /**
     * Processes a message received from the queue.
     *
     * @param message The Message object to process.
     */
    private void processMessage(Message message) {
        ClientToServerMessage messToProcess=(ClientToServerMessage) message;
        messToProcess.execute(gameController);
    }

    public void heartbeat(int clientID){
        long currentDate = System.currentTimeMillis();
        heartbeats.put(clientID, currentDate);
    }

    private void startDisconnectionCheckThread(){

        Thread disconnectionCheck = new Thread(()-> {
            while (inGame) {
                for (Integer clientID : heartbeats.keySet()) {
                    try {
                        long timeSinceLastHeartBeat = System.currentTimeMillis() - heartbeats.get(clientID);
                        if (timeSinceLastHeartBeat > 1000) {
                            clientDisconnected(clientID);
                        }
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }});
        disconnectionCheck.start();
    }

    public void clientDisconnected(int clientID){
        inGame = false;
        handlers.remove(clientID);
        ExceptionMessage exceptionMessage = new ExceptionMessage(-10, "A client has disconnected. Game ended.");
        sendUpdateToAll(exceptionMessage);
        gameController.getModel().setState(GameState.ENDED);
        gameController.getModel().endGame();
    }

    /**
     * Adds a message to the queue.
     *
     * @param message The Message object to add to the queue.
     */
    public void addToQueue(Message message){
        try {
            queue.put(message);// Aggiunge il messaggio alla coda
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Gestione dell'interruzione
            System.err.println("Errore durante l'aggiunta del messaggio alla coda: " + e.getMessage());
        }
    }

    /**
     * Sends an update message to all clients.
     *
     * @param message The Message object to send to all clients.
     */
    public void sendUpdateToAll(Message message){
        try {
            for (Handler handler : handlers.values()) {
                handler.sendMessage(message);
            }
        } catch (RemoteException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the GameController instance for this game server instance.
     *
     * @param gc The GameController instance to set.
     */
    public void setGameController(GameController gc){
        this.gameController = gc;
    }

    /**
     * Sets the handler for a specific client ID.
     *
     * @param clientID The client ID associated with the handler.
     * @param handler  The Handler instance to set.
     */
    public void setHandler(int clientID, Handler handler){
        this.handlers.put(clientID, handler);
    }

    /**
     * Retrieves the client ID associated with a given Player.
     *
     * @param player The Player object whose client ID is to be retrieved.
     * @return The client ID associated with the Player.
     */
    public int getClientID(Player player){
        return players.get(player);
    }

    /**
     * Retrieves the Player object corresponding to a given nickname.
     *
     * @param nickname The nickname of the Player to retrieve.
     * @return The Player object corresponding to the nickname, or null if not found.
     */
    public Player getPlayer(String nickname){
        return this.gameController.getPlayer(nickname);
    }

    /**
     * Adds a Player and its associated client ID to the internal mapping.
     *
     * @param player   The Player object to add.
     * @param clientID The client ID associated with the Player.
     */
    public void addPlayer(Player player, int clientID){
        this.players.put(player, clientID);
        this.heartbeats.put(clientID, System.currentTimeMillis());
    }

    /**
     * Retrieves the client ID associated with a Player identified by their nickname.
     *
     * @param nickname The nickname of the Player whose client ID is to be retrieved.
     * @return The client ID associated with the Player, or -1 if not found.
     */
    public int getClientIDbyNickname(String nickname){
        for (Player p: players.keySet())
            if (nickname.equals(p.getUsername()))
                return p.getClientID();
        return -1;
    }

}
