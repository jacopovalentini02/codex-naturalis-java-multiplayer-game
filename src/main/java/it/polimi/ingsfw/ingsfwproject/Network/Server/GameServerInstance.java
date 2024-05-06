package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameServerInstance {
    private GameController gameController;

    int sendBroadcast=-10;


    //coda dei messaggi in entrata
    //todo che tipo di coda scegliere. Candidata: ConcurredLinkedQueue oppure array list che va sincronizzata

    private HashMap<Integer, Handler> handlers;
    private HashMap<Player, Integer> players;

    public void readQueue(){

    }

    public void addToQueue(){

    }

    public void sendUpdateToAll(Message message){
        try {
            for (Handler handler : handlers.values()) {
                if (message.getClientID() == -10 || message.getClientID() == handler.getClientID()) { //se messaggio in broadcast oppure per il client associato
                    handler.sendMessage(message);
                }
            }
        } catch (RemoteException e){
            throw new RuntimeException(e);
        }
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

    public void sendDisplayedPlayableCardUpdate(List<PlayableCard> displayedPlayableCard) {
        DispPlayCardMessage dispPlayCardMsg = new DispPlayCardMessage(sendBroadcast, new ArrayList<>(displayedPlayableCard));
        sendUpdateToAll(dispPlayCardMsg);
    }

    public void sendCurrentPlayerUpdate(Player currentPlayer) {
        CurrentPlayerMessage currentPlayerMsg = new CurrentPlayerMessage(sendBroadcast, currentPlayer);
        sendUpdateToAll(currentPlayerMsg);
    }

    public void sendAvaiblePositionUpdate(int clientID, ArrayList<Coordinate> coords){
        CoordinatesAvailableMessage coordMsg=new CoordinatesAvailableMessage(clientID, coords);
        sendUpdateToAll(coordMsg);

    }

    public void sendColorChosen(int clientID, PlayerColor color){
        ColorChosenMessage colorMsg=new ColorChosenMessage(clientID, color);
        sendUpdateToAll(colorMsg);
    }

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
}
