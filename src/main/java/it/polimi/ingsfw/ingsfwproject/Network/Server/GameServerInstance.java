package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class GameServerInstance {
    private GameController gameController;

    int sendBroadcast=-10;

    //coda dei messaggi in entrata
    private BlockingDeque<Message> queue;
    private HashMap<Integer, Handler> handlers;
    private HashMap<Player, Integer> players;

    public GameServerInstance() {
        this.queue = new LinkedBlockingDeque<>(); // Inizializzazione della coda
        this.handlers = new HashMap<>();
        this.players = new HashMap<>();
        Thread instanceReaderThread = new Thread(this::readQueue);
        instanceReaderThread.start();
    }

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

    private void processMessage(Message message) {
        ClientToServerMessage messToProcess=(ClientToServerMessage) message;
        messToProcess.execute(gameController);
    }

    public void addToQueue(Message message){
        try {
            queue.put(message); // Aggiunge il messaggio alla coda
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Gestione dell'interruzione
            System.err.println("Errore durante l'aggiunta del messaggio alla coda: " + e.getMessage());
        }
    }

    public void sendUpdateToAll(Message message){
        try {
            for (Handler handler : handlers.values()) {
                handler.sendMessage(message);
            }
        } catch (RemoteException e){
            throw new RuntimeException(e);
        }
    }

    public void setGameController(GameController gc){
        this.gameController = gc;
    }

    public void setHandler(int clientID, Handler handler){
        this.handlers.put(clientID, handler);
    }


    public int getClientID(Player player){
        return players.get(player);
    }

    public Player getPlayer(String nickname){
        return this.gameController.getPlayer(nickname);
    }

    public void addPlayer(Player player, int clientID){
        this.players.put(player, clientID);
    }

    public int getClientIDbyNickname(String nickname){
        for (Player p: players.keySet())
            if (nickname.equals(p.getUsername()))
                return p.getClientID();
        return -1;
    }

    public BlockingDeque<Message> getQueue() {
        return queue;
    }
}
