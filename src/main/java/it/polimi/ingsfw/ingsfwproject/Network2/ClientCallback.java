package it.polimi.ingsfw.ingsfwproject.Network2;

import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientCallback extends UnicastRemoteObject implements ClientCallbackInterface {
    protected ClientCallback(GameClientModel player) throws RemoteException {
        this.model=player;
    }

    private GameClientModel model;

    @Override
    public void update(String update) throws RemoteException {
        System.out.println(update);
    }

    @Override
    public void updateHand(ArrayList<PlayableCard> newHand) throws RemoteException {
        model.setHandCards(newHand);
        System.out.println("Hand updated");
    }

    @Override
    public void updateGoldDeck(Deck deck) throws RemoteException {
        model.setGoldDeck(deck);
        System.out.println("Gold deck updated");
    }

    @Override
    public void updateResourceDeck(Deck deck) throws RemoteException {
        model.setResourceDeck(deck);
        System.out.println("Resource Deck updated");
    }


    @Override
    public void updateAvailablePositions(ArrayList<Coordinate> positions) throws RemoteException {
        model.setAvailablePositions(positions);
        System.out.println("Available positions updated");
    }

    @Override
    public void updateGrid(Map<Coordinate, Face> grid) throws RemoteException {
        model.setGrid(grid);
        System.out.println("Grid updated");
    }

    @Override
    public void updateHandObjecive(ArrayList<ObjectiveCard> handObjectives) throws RemoteException {
        model.setHandObjectives(handObjectives);
        System.out.println("Hand objectives updated");
    }

    @Override
    public void updateDisplayedPlayableCards(List<PlayableCard> displayedCards) throws RemoteException {
        model.setDisplayedCards((ArrayList<PlayableCard>) displayedCards);
        System.out.println("Displayed cards updated");
    }

    @Override
    public void updateDisplayedObjectiveCards(List<ObjectiveCard> displayedCards) throws RemoteException {
        model.setDisplayedObjectiveCards(displayedCards);
        System.out.println("Displayed objective cards updated");
    }

    @Override
    public void updateColor(PlayerColor color) throws RemoteException {
        model.setColor(color);
        System.out.println("Token updated");
    }

    @Override
    public void updateScores(Map<String, Integer> scores) throws RemoteException {
        model.setScores(scores);
        System.out.println("Scores updated");
    }

    @Override
    public void updateState(GameState state) throws RemoteException {
        model.setState(state);
        System.out.println("State updated: now it is " + state.toString());
    }

    public void setPlayer(Player p){
        model.setPlayer(p);
        System.out.println("Player successfully set");
    }

    @Override
    public void updateResources(HashMap<Content, Integer> resources) throws RemoteException {
        model.setResources(resources);
    }

}
