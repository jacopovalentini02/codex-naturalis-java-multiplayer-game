package it.polimi.ingsfw.ingsfwproject.Controller;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.Map;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Server.NetworkController;

public class GameController {
    private Game model;

    private int starterCardsPlayed;

    private Map<Player, NetworkController> clients = new HashMap<>();


    public GameController(Game model){
        this.model = model;
        starterCardsPlayed = 0;
    }

    public void chooseObjectiveCard(Player player, Card card) throws TurnException, GamePhaseException, CardNotPresentException, DeckEmptyException {
        if (model.getCurrentPlayer() != player)
            throw new TurnException("Not your turn");

        if (model.getState() != GameState.CHOOSING_OBJECTIVES)
            throw new GamePhaseException("Phase exception");

        synchronized (model){
            model.chooseObjectiveCard(player, card);
            model.nextTurn();
        }
    }

    public void playCard(Player player, PlayableCard card, boolean upwards, Coordinate coord) throws TurnException, GamePhaseException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException, DeckEmptyException {
        int pointsMade = 0;

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

    public void DrawDisplayedPlayableCard(Player player, PlayableCard card) throws RemoteException, TurnException, CardNotPresentException, DeckEmptyException, GamePhaseException{

        checkIfDrawPossible(player);

        synchronized (model){
            model.drawDisplayedPlayableCard(card, player);

            if (model.getCurrentPlayer().equals(model.getPotentialWinner()))
                model.setState(GameState.ENDING);

            model.nextTurn();
        }


    }

    public void draw(Player player, Deck deck) throws TurnException, GamePhaseException, DeckEmptyException, DeckException {

        checkIfDrawPossible(player);

        if (deck.equals(model.getObjectiveDeck()))
            throw new DeckException("You can't draw from objective deck!");

        synchronized (model){
            player.draw(deck);

            if (model.getCurrentPlayer().equals(model.getPotentialWinner()))
                model.setState(GameState.ENDING);

            model.nextTurn();
        }

    }

    public void chooseColor(Player player, PlayerColor color) throws ColorNotAvailableException, DeckEmptyException, GamePhaseException, TurnException {

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

    private void starterCardPlayed() throws DeckEmptyException {
        starterCardsPlayed++;
        //TODO: deadlock?
        if (starterCardsPlayed == model.getNumOfPlayers())
            model.setState(GameState.CHOOSING_COLORS);
    }
}

