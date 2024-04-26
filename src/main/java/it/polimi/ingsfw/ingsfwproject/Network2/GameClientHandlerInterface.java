package it.polimi.ingsfw.ingsfwproject.Network2;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameClientHandlerInterface extends Remote {
    public String sum(Player a, int b) throws RemoteException;

    public String chooseObjectiveCard(String username, int cardID) throws RemoteException, TurnException, GamePhaseException, CardNotPresentException;

    public void registerClient(ClientCallbackInterface client, String username) throws RemoteException;

    public String playCard(String username, int cardID, boolean upwards, Coordinate coord) throws RemoteException, TurnException, GamePhaseException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException;

    public String DrawDisplayedPlayableCard(String username, int cardID) throws RemoteException, TurnException, GamePhaseException, CardNotPresentException, DeckEmptyException;

    public String draw(String username, boolean resourceDeck) throws RemoteException, TurnException, GamePhaseException, DeckEmptyException, DeckException;

    public String chooseColor(String username, PlayerColor color) throws RemoteException, TurnException, GamePhaseException, ColorNotAvailableException, DeckEmptyException;

}