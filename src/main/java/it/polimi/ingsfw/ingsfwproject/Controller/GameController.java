package it.polimi.ingsfw.ingsfwproject.Controller;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import it.polimi.ingsfw.ingsfwproject.Network.Server.Server;


public class GameController implements Controller {
    private final Game model;

    public Queue<ChatMessage> globalChat;

    private int starterCardsPlayed;
    private int colorChosen;

    private final GameServerInstance serverInstance;


    public GameController(Game model, GameServerInstance serverInstance){
        this.model = model;
        starterCardsPlayed = 0;
        colorChosen=0;
        this.serverInstance = serverInstance;
        this.globalChat = new LinkedBlockingQueue<>();
    }

    public void chooseObjectiveCard(String username, int cardID){

        Player player = null;
        Card card = null;
        boolean moveSuccesful = false;

        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }
        assert player != null;

        for (ObjectiveCard pc: player.getHandObjective()){
            if (pc.getIdCard() == cardID)
                card = pc;
        }

        if (model.getCurrentPlayer() != player) {
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(), "Not your turn"));
            return;
        }

        if (model.getState() != GameState.CHOOSING_OBJECTIVES) {
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(), "Phase exception"));
            return;
        }

        synchronized (model){
            moveSuccesful = model.chooseObjectiveCard(player, card);

            if (!moveSuccesful)
                return;

            if(model.getObjectiveCardsChosen()!=model.getNumOfPlayers())
                model.nextTurn();
        }
    }
    public void playCard(String username, int cardID, boolean upwards, Coordinate coord){
        int pointsMade = 0;
        Player player = null;
        PlayableCard card = null;
        boolean moveSuccessful = false;
        System.out.println(username);
        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }
        assert player != null;
        for (PlayableCard pc: player.getHandCard()){
            if (pc.getIdCard() == cardID)
                card = pc;
        }

        if (model.getState() == GameState.WAITING_FOR_PLAYERS || model.getState() == GameState.CHOOSING_OBJECTIVES || model.getState() == GameState.ENDED || model.getState() == GameState.CHOOSING_COLORS) {
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(), "You can't play a card now"));
            return;
        }


        if (model.getCurrentPlayer() != player) {
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(), "Not your turn"));
            return;
        }

        if(model.getifCurrentPlayerhasPlayed()) {
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(), "You have already played, it's time to draw"));
            return;
        }
        synchronized (model){
            pointsMade = player.playCard(card, upwards, coord);

            if(pointsMade == -1) {
                return;
            } else {
                model.updatePoints(pointsMade, player);
            }

            if (card instanceof StarterCard)
                starterCardPlayed();

            if (card instanceof StarterCard || model.getState() == GameState.ENDING){
                model.nextTurn();
            } else {
                model.setCurrentPlayerhasPlayed(true);
                serverInstance.sendUpdateToAll(new CurrentPlayerHasPlayedMessage(player.getClientID(),true));
            }
        }
    }

    public void drawDisplayedPlayableCard(String username, int cardID){

        Player player = null;
        PlayableCard card = null;
        boolean moveSuccesful = false;

        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }
        for (PlayableCard pc: model.getDisplayedPlayableCard()){
            if (pc.getIdCard() == cardID)
                card = pc;
        }

        moveSuccesful = checkIfDrawPossible(player);
        if (!moveSuccesful)
            return;

        synchronized (model){
             moveSuccesful = model.drawDisplayedPlayableCard(card, player);

             if (!moveSuccesful)
                 return;

            if (model.getCurrentPlayer().equals(model.getPotentialWinner())) {
                model.setState(GameState.ENDING);
            }
            model.nextTurn();
        }


    }
    //cambiato Deck in boolean alrimenti era difficile capire da che mazzo volesse pescare e quindi che mazzo aggiornare
    //true: resource deck, false: gold deck
    public void draw(String username,boolean resourceDeck)  {
        Deck deck;
        Player player = null;
        boolean moveSuccessfull;

        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }

        if (resourceDeck){
            deck = model.getResourceDeck();
        } else {
            deck = model.getGoldDeck();
        }

        moveSuccessfull = checkIfDrawPossible(player);

        if (!moveSuccessfull)
            return;


        if (deck.equals(model.getObjectiveDeck())){
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),"You can't draw from objective deck!"));
            return;
        }

        synchronized (model){
            moveSuccessfull=player.draw(deck);//

            if(moveSuccessfull){
                if (resourceDeck){
                    serverInstance.sendUpdateToAll(new ResourceDeckMessage(-10, deck));
                } else {
                    serverInstance.sendUpdateToAll(new GoldDeckMessage(-10, deck));
                }

                if (model.getCurrentPlayer().equals(model.getPotentialWinner())) {
                    model.setState(GameState.ENDING);
                }
                model.nextTurn();
            }


        }

    }

    public void chooseColor(String username, PlayerColor color) {
        boolean moveSuccessfull=true;

        Player player = null;
        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }

        if (model.getCurrentPlayer() != player){
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),"Not your turn"));
            return;
        }

        if (model.getState() != GameState.CHOOSING_COLORS){
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),"You can't choose a color now"));
            return;
        }

        synchronized (model){
            moveSuccessfull=model.chooseColor(player, color);
            if(moveSuccessfull){
                colorChosenCounter();
                model.nextTurn();
                if(colorChosen== getModel().getNumOfPlayers())
                    model.setupHandsAndObjectives();
            }

        }
    }

    private boolean checkIfDrawPossible(Player player){
        if (model.getState() == GameState.WAITING_FOR_PLAYERS || model.getState() == GameState.CHOOSING_STARTER_CARDS || model.getState() == GameState.CHOOSING_OBJECTIVES || model.getState() == GameState.ENDING || model.getState() == GameState.CHOOSING_COLORS) {
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(), "You can't draw now"));
            return false;
        }

        if (model.getCurrentPlayer() != player){
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),"It's not your turn"));
            return false;
        }

        if (!(model.getifCurrentPlayerhasPlayed())) {
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(), "You should play a card before drawing"));
            return false;
        }

        return true;
    }

    private void starterCardPlayed() {
        starterCardsPlayed++;
        if (starterCardsPlayed == model.getNumOfPlayers()){
            model.setState(GameState.CHOOSING_COLORS);
        }

    }

    private void colorChosenCounter() {
        colorChosen++;
        if (colorChosen == model.getNumOfPlayers()){
            model.setState(GameState.CHOOSING_OBJECTIVES);

        }

    }

    public void addMessageToGlobalChat(ChatMessage message){
        this.globalChat.add(message);
        serverInstance.sendUpdateToAll(new RecieveChatMessage(-10, message.getSender(), message.getRecipient(), message.getMessage()));
    }

    public void forwardPrivateChatMessage(ChatMessage message){
        int recipientClientID = serverInstance.getClientIDbyNickname(message.getRecipient());
        if (recipientClientID == -1) {
            serverInstance.sendUpdateToAll(new ExcpetionMessage(serverInstance.getClientIDbyNickname(message.getSender()), "There's no player with nick " + message.getRecipient()));
            return;
        } else {
            serverInstance.sendUpdateToAll(new RecieveChatMessage(recipientClientID, message.getSender(), message.getRecipient(), message.getMessage()));
        }
    }

    public void clientDisconnected(){
        this.model.clientDisconnected();
    }

    public Game getModel() {
        return model;
    }
    public Player getPlayer(String username){
       synchronized (model){
           return model.getPlayer(username);
       }
    }

    public void sendTokenAvailable(int clientID){
        serverInstance.sendUpdateToAll(new ColorAvailableMessage(clientID, model.getTokenAvailable()));
    }
}

