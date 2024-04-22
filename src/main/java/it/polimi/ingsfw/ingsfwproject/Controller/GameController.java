package it.polimi.ingsfw.ingsfwproject.Controller;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Server.NetworkController;

public class GameController {
    private Game model;

    private Map<Player, NetworkController> clients = new HashMap<>();


    public GameController(Game model){
        this.model = model;
    }

    public void playCard(Player player, PlayableCard card, boolean upwards, Coordinate coord) throws TurnException, GamePhaseException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException {
        //TODO: chiamare effettvamente PlayerGround.playCard(...).
        // con il valore di ritorno, chiamare game.updatePoints(...) per aggiungere i punti di questa giocata al player

        //TODO: logica di turno
        int pointsMade = 0;

        if (model.getCurrentPlayer() != player)
            throw new TurnException("Not your turn");

        if(model.getifCurrentPlayerhasPlayed())
            throw new GamePhaseException("You have already played, it's time to draw");

        synchronized (model){
            pointsMade = player.playCard(card, upwards, coord);
            model.updatePoints(pointsMade, player);
            if (card instanceof StarterCard || model.getState() == GameState.ENDING){
                model.nextTurn();
            } else {
                model.setCurrentPlayerhasPlayed(true);
            }
        }
    }


    public void DrawDisplayedPlayableCard(Player player, PlayableCard card) throws RemoteException, TurnException, CardNotPresentException, DeckEmptyException, GamePhaseException{

        if (model.getCurrentPlayer() != player)
            throw new TurnException("It's not your turn");

        if (!(model.getifCurrentPlayerhasPlayed()))
            throw new GamePhaseException("You should play a card before drawing");

        synchronized (model){
            model.drawDisplayedPlayableCard(card, player);

            if (model.getCurrentPlayer().equals(model.getPotentialWinner()))
                model.setState(GameState.ENDING);

            model.nextTurn();
        }


    }



    public void draw(Player player, Deck deck) throws TurnException, GamePhaseException, DeckEmptyException {

        if (model.getCurrentPlayer() != player)
            throw new TurnException("It's not your turn");

        if (!(model.getifCurrentPlayerhasPlayed()))
            throw new GamePhaseException("You should play a card before drawing");

        synchronized (model){
            player.draw(deck);

            if (model.getCurrentPlayer().equals(model.getPotentialWinner()))
                model.setState(GameState.ENDING);


            model.nextTurn();
        }

    }
}

//TODO: ultimo turno impedire il pescaggio
//TODO: controllare starter flow

