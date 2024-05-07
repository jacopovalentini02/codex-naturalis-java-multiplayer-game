package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        readQueue();
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
        switch (message.getType()){
            case DRAW:
                DrawMessage drawMessage=(DrawMessage) message;
                try{
                    gameController.draw(drawMessage.getNickname(), drawMessage.isResourceDeck());
                }catch (TurnException | GamePhaseException | DeckEmptyException | DeckException e){
                    sendException(drawMessage.getClientID(), e.getMessage());
                }
                break;

            case PLAY_CARD:
                PlayCardMessage playCardMessage=(PlayCardMessage) message;
                try{
                    gameController.playCard(playCardMessage.getNickname(),playCardMessage.getCardID(),playCardMessage.isFace(),playCardMessage.getCoordinate());
                }catch (TurnException | GamePhaseException | PositionNotAvailableException | NotEnoughResourcesException | CardNotInHandException e){
                    sendException(playCardMessage.getClientID(), e.getMessage());
                }
                break;

            case SKIP_TURN:
                //todo gestirlo
                break;

            case PICK:
                PickMessage drawDispPlayableCard=(PickMessage) message;
                try{
                    gameController.drawDisplayedPlayableCard(drawDispPlayableCard.getNickname(),drawDispPlayableCard.getCardID());
                }catch(TurnException | CardNotPresentException | DeckEmptyException | GamePhaseException e){
                    sendException(drawDispPlayableCard.getClientID(), e.getMessage());
                }
                break;

            case WANTED_COLOR:
                WantThatColorMessage wantThatColorMessage=(WantThatColorMessage) message;
                try{
                    gameController.chooseColor(wantThatColorMessage.getNickname(),wantThatColorMessage.getColor());
                }catch(ColorNotAvailableException| DeckEmptyException| GamePhaseException| TurnException e){
                    sendException(wantThatColorMessage.getClientID(), e.getMessage());
                }
                break;
            case ASK_FOR_COLORS:
                sendColorsAvailable(message.getClientID(), gameController.getModel().getTokenAvailable());
                break;

            case CHOSEN_OBJECTIVE_CARD:
                ObjectiveCardChosenMessage objectiveCardChosenMessage=(ObjectiveCardChosenMessage) message;
                try{
                    gameController.chooseObjectiveCard(objectiveCardChosenMessage.getNickname(),objectiveCardChosenMessage.getCardID());
                }catch(TurnException| GamePhaseException| CardNotPresentException e){
                    sendException(objectiveCardChosenMessage.getClientID(), e.getMessage());
                }
                break;
        }

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
                if (message.getClientID() == -10 || message.getClientID() == handler.getClientID()) { //se messaggio in broadcast oppure per il client associato
                    handler.sendMessage(message);
                }
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
        DispayedPlayableCardMessage dispPlayCardMsg = new DispayedPlayableCardMessage(sendBroadcast, new ArrayList<>(displayedPlayableCard));
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
}
