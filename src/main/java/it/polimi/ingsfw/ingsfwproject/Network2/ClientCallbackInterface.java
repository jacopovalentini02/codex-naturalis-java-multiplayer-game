package it.polimi.ingsfw.ingsfwproject.Network2;

import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ClientCallbackInterface extends Remote {
    void update(String update) throws RemoteException;
    void updateHand(ArrayList<PlayableCard> newHand) throws RemoteException;
    void updateGoldDeck(Deck deck) throws RemoteException;
    void updateResourceDeck(Deck deck) throws RemoteException;
    void updateAvailablePositions(ArrayList<Coordinate> positions) throws RemoteException;
    void updateGrid(Map <Coordinate, Face> grid) throws RemoteException;
    void updateHandObjecive(ArrayList <ObjectiveCard> handObjectives) throws RemoteException;
    void updateDisplayedPlayableCards(List<PlayableCard> displayedCards) throws RemoteException;

    void updateColor(PlayerColor color) throws RemoteException;
    void updateScores(Map<String, Integer> scores) throws RemoteException;

    void updateState(GameState state) throws RemoteException;

    void setPlayer(Player player) throws RemoteException;

}
