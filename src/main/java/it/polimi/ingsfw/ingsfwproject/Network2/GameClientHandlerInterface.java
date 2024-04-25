package it.polimi.ingsfw.ingsfwproject.Network2;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameClientHandlerInterface extends Remote {
    public String sum(Player a, int b) throws RemoteException;

    public String chooseObjectiveCard(Player player, Card card) throws RemoteException, TurnException, GamePhaseException, CardNotPresentException;

    public void registerClient(ClientCallbackInterface client, String username) throws RemoteException;

    public String playCard(Player player, PlayableCard card, boolean upwards, Coordinate coord) throws RemoteException, TurnException, GamePhaseException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException;

    public String DrawDisplayedPlayableCard(Player player, PlayableCard card) throws RemoteException, TurnException, GamePhaseException, CardNotPresentException, DeckEmptyException;

    public String draw(Player player, Deck deck) throws RemoteException, TurnException, GamePhaseException, DeckEmptyException, DeckException;

    public String chooseColor(Player player, PlayerColor color) throws RemoteException, TurnException, GamePhaseException, ColorNotAvailableException, DeckEmptyException;

}