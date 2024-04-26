package it.polimi.ingsfw.ingsfwproject.Network2;

import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientCallback extends UnicastRemoteObject implements ClientCallbackInterface {
    protected ClientCallback() throws RemoteException {
    }

    private ArrayList<PlayableCard> handCards;
    private Deck resourceDeck;
    private Deck goldDeck;
    private ArrayList<Coordinate> availablePositions;
    private Map<Coordinate, Face> grid;

    private ArrayList<ObjectiveCard> handObjectives;

    private ArrayList<PlayableCard>  displayedCards;

    private PlayerColor color;

    private Map<String, Integer> scores;

    private GameState state;

    private List<ObjectiveCard> displayedObjectiveCards;

    public HashMap<Content, Integer> getResources() {
        return resources;
    }

    private HashMap<Content, Integer> resources;

    public Player getPlayer() {
        return player;
    }

    private Player player;


    public ArrayList<PlayableCard> getHandCards() {
        return handCards;
    }

    public Deck getResourceDeck() {
        return resourceDeck;
    }

    public Deck getGoldDeck() {
        return goldDeck;
    }

    public ArrayList<Coordinate> getAvailablePositions() {
        return availablePositions;
    }

    public Map<Coordinate, Face> getGrid() {
        return grid;
    }

    public ArrayList<ObjectiveCard> getHandObjectives() {
        return handObjectives;
    }

    public ArrayList<PlayableCard> getDisplayedCards() {
        return displayedCards;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public GameState getState() {
        return state;
    }




    @Override
    public void update(String update) throws RemoteException {
        System.out.println(update);
    }

    @Override
    public void updateHand(ArrayList<PlayableCard> newHand) throws RemoteException {
        this.handCards = newHand;
        System.out.println("Hand updated");
    }

    @Override
    public void updateGoldDeck(Deck deck) throws RemoteException {
        this.goldDeck = deck;
        System.out.println("Gold deck updated");
    }

    @Override
    public void updateResourceDeck(Deck deck) throws RemoteException {
        this.resourceDeck = deck;
        System.out.println("Resource Deck updated");
    }


    @Override
    public void updateAvailablePositions(ArrayList<Coordinate> positions) throws RemoteException {
        this.availablePositions = positions;
        System.out.println("Available positions updated");
    }

    @Override
    public void updateGrid(Map<Coordinate, Face> grid) throws RemoteException {
        this.grid = grid;
        System.out.println("Grid updated");
    }

    @Override
    public void updateHandObjecive(ArrayList<ObjectiveCard> handObjectives) throws RemoteException {
        this.handObjectives = handObjectives;
        System.out.println("Hand objectives updated");
    }

    @Override
    public void updateDisplayedPlayableCards(List<PlayableCard> displayedCards) throws RemoteException {
        this.displayedCards = (ArrayList<PlayableCard>) displayedCards;
        System.out.println("Displayed cards updated");
    }

    @Override
    public void updateDisplayedObjectiveCards(List<ObjectiveCard> displayedCards) throws RemoteException {
        this.displayedObjectiveCards = displayedCards;
        System.out.println("Displayed objective cards updated");
    }

    @Override
    public void updateColor(PlayerColor color) throws RemoteException {
        this.color = color;
        System.out.println("Token updated");
    }

    @Override
    public void updateScores(Map<String, Integer> scores) throws RemoteException {
        this.scores = scores;
        System.out.println("Scores updated");
    }

    @Override
    public void updateState(GameState state) throws RemoteException {
        this.state = state;
        System.out.println("State updated: now it is " + state.toString());
    }

    public void setPlayer(Player player){
        this.player = player;
        System.out.println("Player successfully set");
    }

    @Override
    public void updateResources(HashMap<Content, Integer> resources) throws RemoteException {
        this.resources = resources;
    }

}
