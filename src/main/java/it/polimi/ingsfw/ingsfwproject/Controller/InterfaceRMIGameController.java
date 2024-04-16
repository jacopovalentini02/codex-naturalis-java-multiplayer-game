package it.polimi.ingsfw.ingsfwproject.Controller;
import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Model.Deck;
import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Model.Player;
import java.util.*;

import java.rmi.*;

public interface InterfaceRMIGameController extends Remote {
    public void playCard(Player player, PlayableCard card, boolean upwards, Coordinate coord) throws RemoteException;

    public void DrawDisplayedPlayableCard(Player player, PlayableCard card) throws RemoteException;

    public void draw(Player player, Deck deck) throws RemoteException;

    public void drawDisplayedPlayableCard(Player player, PlayableCard card) throws RemoteException;
    public ArrayList<Coordinate> getAvailablePositions(Player player) throws RemoteException;
}
