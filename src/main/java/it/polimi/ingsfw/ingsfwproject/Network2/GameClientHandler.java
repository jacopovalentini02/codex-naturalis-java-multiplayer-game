package it.polimi.ingsfw.ingsfwproject.Network2;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GameClientHandler extends UnicastRemoteObject implements GameClientHandlerInterface {

    private final GameController gameController;

    protected GameClientHandler(GameController gc) throws RemoteException {
        this.gameController = gc;
    }
    @Override
    public String sum(Player a, int b) throws RemoteException{
        return "a + b";
    }

    @Override
    public String chooseObjectiveCard(Player player, Card card) throws java.rmi.RemoteException, TurnException, GamePhaseException, CardNotPresentException {
        gameController.chooseObjectiveCard(player, card);
        return "Objective card successfully chosen";
    }

    @Override
    public void registerClient(ClientCallbackInterface client, String username) throws RemoteException {
        gameController.addClient(username, client);
    }

    @Override
    public String playCard(Player player, PlayableCard card, boolean upwards, Coordinate coord) throws RemoteException, TurnException, GamePhaseException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException {
        gameController.playCard(player, card, upwards, coord);
        return "Card succesfully played";
    }

    @Override
    public String DrawDisplayedPlayableCard(Player player, PlayableCard card) throws RemoteException, TurnException, GamePhaseException, CardNotPresentException, DeckEmptyException {
        gameController.DrawDisplayedPlayableCard(player, card);
        return "Card drawn successfully";
    }

    @Override
    public String draw(Player player, Deck deck) throws RemoteException, TurnException, GamePhaseException, DeckEmptyException, DeckException {
        gameController.draw(player, deck);
        return "Card successfully drawn";
    }

    @Override
    public String  chooseColor(Player player, PlayerColor color) throws RemoteException, TurnException, GamePhaseException, ColorNotAvailableException, DeckEmptyException {
        gameController.chooseColor(player, color);
        return "Color successfully chosen";
    }
}
