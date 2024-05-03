package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtualView {
    private int clientID;
    private ArrayList<Game> avaibleGames;
    private int gameID;
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
    public void setHandCards(ArrayList<PlayableCard> handCards) {
        this.handCards = handCards;
    }
    public Deck getResourceDeck() {
        return resourceDeck;
    }
    public void setResourceDeck(Deck resourceDeck) {
        this.resourceDeck = resourceDeck;
    }
    public Deck getGoldDeck() {
        return goldDeck;
    }
    public void setGoldDeck(Deck goldDeck) {
        this.goldDeck = goldDeck;
    }

    public ArrayList<Coordinate> getAvailablePositions() {
        return availablePositions;
    }

    public void setAvailablePositions(ArrayList<Coordinate> availablePositions) {
        this.availablePositions = availablePositions;
    }

    public Map<Coordinate, Face> getGrid() {
        return grid;
    }

    public void setGrid(Map<Coordinate, Face> grid) {
        this.grid = grid;
    }

    public ArrayList<ObjectiveCard> getHandObjectives() {
        return handObjectives;
    }

    public void setHandObjectives(ArrayList<ObjectiveCard> handObjectives) {
        this.handObjectives = handObjectives;
    }

    public ArrayList<PlayableCard> getDisplayedCards() {
        return displayedCards;
    }

    public void setDisplayedCards(ArrayList<PlayableCard> displayedCards) {
        this.displayedCards = displayedCards;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
        if (state.equals(GameState.ENDED))
            System.exit(0);
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public List<ObjectiveCard> getDisplayedObjectiveCards() {
        return displayedObjectiveCards;
    }

    public void setDisplayedObjectiveCards(List<ObjectiveCard> displayedObjectiveCards) {
        this.displayedObjectiveCards = displayedObjectiveCards;
    }

    public void setResources(HashMap<Content, Integer> resources) {
        this.resources = resources;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void prova(String prova){
        System.out.println(prova);
    }
}
