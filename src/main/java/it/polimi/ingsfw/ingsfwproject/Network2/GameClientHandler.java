package it.polimi.ingsfw.ingsfwproject.Network2;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GameClientHandler extends UnicastRemoteObject implements GameClientHandlerInterface {

    private GameController gameController;

    protected GameClientHandler(GameController gc) throws RemoteException {
        this.gameController = gc;
    }
    @Override
    public String sum(Player a, int b) throws RemoteException{
        return "a + b";
    }


    public String chooseObjectiveCard(Player player, Card card) throws java.rmi.RemoteException {
        try{
        gameController.chooseObjectiveCard(player, card);
        } catch (Exception e){
            return "Exception";
        }
        return "OK";
    }

    @Override
    public void registerClient(ClientCallbackInterface client) throws RemoteException {
        gameController.addClient(client);
    }


/*
    @Override
    public void playCard(Player player, PlayableCard card, boolean upwards, Coordinate coord) throws TurnException, GamePhaseException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException, DeckEmptyException {
        gameController.playCard(player, card, upwards, coord);
    }

    @Override
    public void DrawDisplayedPlayableCard(Player player, PlayableCard card) throws TurnException, GamePhaseException, RemoteException, CardNotPresentException, DeckEmptyException {
        gameController.DrawDisplayedPlayableCard(player, card);
    }

    @Override
    public void draw(Player player, Deck deck) throws TurnException, GamePhaseException, DeckEmptyException, DeckException {
        gameController.draw(player, deck);
    }

    @Override
    public void chooseColor(Player player, PlayerColor color) throws TurnException, GamePhaseException, ColorNotAvailableException, DeckEmptyException {
        gameController.chooseColor(player, color);
    }*/
}
