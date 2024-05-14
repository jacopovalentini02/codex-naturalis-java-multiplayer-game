package it.polimi.ingsfw.ingsfwproject.Controller;
import java.rmi.RemoteException;
import java.util.*;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExcpetionMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.GoldDeckMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ResourceDeckMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import it.polimi.ingsfw.ingsfwproject.Network.Server.Server;


public class GameController implements Controller {
    private final Game model;

    private int starterCardsPlayed;

    private final GameServerInstance serverInstance;


    public GameController(Game model, GameServerInstance serverInstance){
        this.model = model;
        starterCardsPlayed = 0;
        this.serverInstance = serverInstance;
    }

    public void chooseObjectiveCard(String username, int cardID){

        Player player = null;
        Card card = null;
        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }
        assert player != null;

        for (ObjectiveCard pc: player.getHandObjective()){
            if (pc.getIdCard() == cardID)
                card = pc;
        }

        if (model.getCurrentPlayer() != player)
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),"Not your turn"));

        if (model.getState() != GameState.CHOOSING_OBJECTIVES)
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),"Phase exception"));

        synchronized (model){
            model.chooseObjectiveCard(player, card);
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
            }
        }
    }

    public void drawDisplayedPlayableCard(String username, int cardID){

        Player player = null;
        PlayableCard card = null;
        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }
        for (PlayableCard pc: model.getDisplayedPlayableCard()){
            if (pc.getIdCard() == cardID)
                card = pc;
        }

        checkIfDrawPossible(player);

        synchronized (model){
            model.drawDisplayedPlayableCard(card, player);

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

        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }

        if (player == null) //null control
            throw new RuntimeException();

        if (resourceDeck){
            deck = model.getResourceDeck();
        } else {
            deck = model.getGoldDeck();
        }

        checkIfDrawPossible(player);

        if (deck.equals(model.getObjectiveDeck()))
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),"You can't draw from objective deck!"));

        synchronized (model){
            player.draw(deck);

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

    public void chooseColor(String username, PlayerColor color) {

        Player player = null;
        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }

        if (player == null)
            throw new RuntimeException();

        if (model.getCurrentPlayer() != player)
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),"Not your turn"));

        if (model.getState() != GameState.CHOOSING_COLORS)
            serverInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),"You must choose a color now"));

        synchronized (model){
            model.chooseColor(player, color);
            model.nextTurn();
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
            model.getGameServerInstance().sendGameStateUpdate(GameState.CHOOSING_COLORS);
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
}

