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

    //METODI PER L'UPDATE AL CLIENT
    public void sendStarterCardMessage(int clientID, StarterCard starter) {
        SendStarterCardMessage starterMessage = new SendStarterCardMessage(clientID, starter);
        sendUpdateToAll(starterMessage);
    }

    public void sendGoldDeckUpdate(Deck goldDeck) {
        GoldDeckMessage goldDeckMsg = new GoldDeckMessage(sendBroadcast, goldDeck);
        sendUpdateToAll(goldDeckMsg);
    }

    public void sendResourceDeckUpdate(Deck resourceDeck){
        ResourceDeckMessage resourceDeckMessage=new ResourceDeckMessage(sendBroadcast,resourceDeck);
        sendUpdateToAll(resourceDeckMessage);
    }

    public void sendDisplayedPlayableCardUpdate(List<PlayableCard> displayedPlayableCard) {
        DisplayedPlayableCardsMessage dispPlayCardMsg = new DisplayedPlayableCardsMessage(sendBroadcast, new ArrayList<>(displayedPlayableCard));
        sendUpdateToAll(dispPlayCardMsg);
    }

    public void sendCurrentPlayerUpdate(String currentPlayer) {
        CurrentPlayerMessage currentPlayerMsg = new CurrentPlayerMessage(sendBroadcast, currentPlayer);
        sendUpdateToAll(currentPlayerMsg);
    }

    public void sendAvaiblePositionUpdate(int clientID, ArrayList<Coordinate> coords){
        CoordinatesAvailableMessage coordMsg=new CoordinatesAvailableMessage(clientID, coords);
        sendUpdateToAll(coordMsg);

    }

//    public void sendColorChosen(int clientID, PlayerColor color){
//        ColorChosenMessage colorMsg=new ColorChosenMessage(clientID, color);
//        sendUpdateToAll(colorMsg);
//    }

    public void sendHandObjectiveUpdate(int clientID, ArrayList<ObjectiveCard> card){
        HandObjectiveMessage handObjectiveMsg=new HandObjectiveMessage(clientID, card);
        sendUpdateToAll(handObjectiveMsg);
    }

    public void sendHandCardsUpdate(int clientID, ArrayList<PlayableCard> handCards){
        HandCardsMessage handCardsMsg=new HandCardsMessage(clientID, handCards);
        sendUpdateToAll(handCardsMsg);
    }

    public void sendGameStateUpdate(GameState gameState){
        GameStateMessage gameStateMsg=new GameStateMessage(sendBroadcast, gameState);
        sendUpdateToAll(gameStateMsg);
    }

    public void sendDisplayedObjectiveCardUpdate(List<ObjectiveCard> displayedObjectiveCard){
        DisplayedObjectiveMessage displayedObjectiveMsg=new DisplayedObjectiveMessage(sendBroadcast, displayedObjectiveCard);
        sendUpdateToAll(displayedObjectiveMsg);
    }

    public void sendScoreUpdate(Map<String, Integer> scores){
        ScoreMessage scoreMessage=new ScoreMessage(sendBroadcast, scores);
        sendUpdateToAll(scoreMessage);
    }

    public void sendResourcesUpdate(HashMap<Content, Integer> resources,String nickname){
        ResourcesMessage resourcesMessage=new ResourcesMessage(sendBroadcast, resources, nickname);
        sendUpdateToAll(resourcesMessage);
    }

    public void sendPlayersListUpdate(ArrayList<String> nicknames){
        PlayersListMessage playersListMsg=new PlayersListMessage(sendBroadcast, nicknames);
        sendUpdateToAll(playersListMsg);
    }

    public void sendGridUpdate(Map<Coordinate, Face> grid, String nickname){
        GridMessage gridMessage=new GridMessage(sendBroadcast, grid, nickname);
        sendUpdateToAll(gridMessage);
    }

    public void sendWinner(String winner){
        WinnerMessage winnerMsg=new WinnerMessage(sendBroadcast, winner);
        sendUpdateToAll(winnerMsg);
    }

    public void sendException(int clientID, String error){
        ExcpetionMessage excpetionMessage=new ExcpetionMessage(clientID,error);
        sendUpdateToAll(excpetionMessage);
    }

    public void sendColorsAvailable(int clientID, List<PlayerColor> tokenAvailable){
        ColorAvailableMessage colorAvailableMessage=new ColorAvailableMessage(clientID, tokenAvailable);
        sendUpdateToAll(colorAvailableMessage);
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

}
