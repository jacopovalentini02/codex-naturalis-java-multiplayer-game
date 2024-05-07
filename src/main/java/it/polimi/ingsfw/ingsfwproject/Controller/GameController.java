package it.polimi.ingsfw.ingsfwproject.Controller;
import java.rmi.RemoteException;
import java.util.*;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;


public class GameController {
    private Game model;

    private int starterCardsPlayed;


    public GameController(Game model){
        this.model = model;
        starterCardsPlayed = 0;
    }

    public void chooseObjectiveCard(String username, int cardID) throws TurnException, GamePhaseException, CardNotPresentException {

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
            throw new TurnException("Not your turn");

        if (model.getState() != GameState.CHOOSING_OBJECTIVES)
            throw new GamePhaseException("Phase exception");

        synchronized (model){
            model.chooseObjectiveCard(player, card);
            model.nextTurn();
        }
    }
    public void playCard(String username, int cardID, boolean upwards, Coordinate coord) throws TurnException, GamePhaseException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException{
        int pointsMade = 0;
        Player player = null;
        PlayableCard card = null;
        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }
        assert player != null;
        for (PlayableCard pc: player.getHandCard()){
            if (pc.getIdCard() == cardID)
                card = pc;
        }

        if (model.getState() == GameState.WAITING_FOR_PLAYERS || model.getState() == GameState.CHOOSING_OBJECTIVES || model.getState() == GameState.ENDED || model.getState() == GameState.CHOOSING_COLORS)
            throw new GamePhaseException("You can't play a card now");

        if (model.getCurrentPlayer() != player)
            throw new TurnException("Not your turn");

        if(model.getifCurrentPlayerhasPlayed())
            throw new GamePhaseException("You have already played, it's time to draw");

        synchronized (model){
            pointsMade = player.playCard(card, upwards, coord);
            model.updatePoints(pointsMade, player);

            if (card instanceof StarterCard)
                starterCardPlayed();

            if (card instanceof StarterCard || model.getState() == GameState.ENDING){
                model.nextTurn();
            } else {
                model.setCurrentPlayerhasPlayed(true);
            }
        }
    }

    public void drawDisplayedPlayableCard(String username, int cardID) throws TurnException, CardNotPresentException, DeckEmptyException, GamePhaseException{

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
                model.getGameServerInstance().sendGameStateUpdate(model.getState());
            }
            model.nextTurn();
        }


    }
    //cambiato Deck in boolean alrimenti era difficile capire da che mazzo volesse pescare e quindi che mazzo aggiornare
    //true: resource deck, false: gold deck
    public void draw(String username,boolean resourceDeck) throws TurnException, GamePhaseException, DeckEmptyException, DeckException {
        Deck deck;
        Player player = null;

        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }

        if (resourceDeck){
            deck = model.getResourceDeck();
        } else {
            deck = model.getGoldDeck();
        }

        checkIfDrawPossible(player);

        if (deck.equals(model.getObjectiveDeck()))
            throw new DeckException("You can't draw from objective deck!");

        synchronized (model){
            player.draw(deck);

            if (resourceDeck){
                model.getGameServerInstance().sendResourceDeckUpdate(deck);
            } else {
                model.getGameServerInstance().sendGoldDeckUpdate(deck);
            }

            if (model.getCurrentPlayer().equals(model.getPotentialWinner())) {
                model.setState(GameState.ENDING);
                model.getGameServerInstance().sendGameStateUpdate(GameState.ENDING);
            }
            model.nextTurn();
        }

    }

    public void chooseColor(String username, PlayerColor color) throws ColorNotAvailableException, DeckEmptyException, GamePhaseException, TurnException {

        Player player = null;
        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }

        if (model.getCurrentPlayer() != player)
            throw new TurnException("Not your turn");

        if (model.getState() != GameState.CHOOSING_COLORS)
            throw new GamePhaseException("You must choose a color now");

        synchronized (model){
            model.chooseColor(player, color);
            model.nextTurn();
        }
    }

    private void checkIfDrawPossible(Player player) throws GamePhaseException, TurnException {
        if (model.getState() == GameState.WAITING_FOR_PLAYERS || model.getState() == GameState.CHOOSING_STARTER_CARDS || model.getState() == GameState.CHOOSING_OBJECTIVES || model.getState() == GameState.ENDING || model.getState() == GameState.CHOOSING_COLORS)
            throw new GamePhaseException("You can't draw now");

        if (model.getCurrentPlayer() != player)
            throw new TurnException("It's not your turn");

        if (!(model.getifCurrentPlayerhasPlayed()))
            throw new GamePhaseException("You should play a card before drawing");
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

