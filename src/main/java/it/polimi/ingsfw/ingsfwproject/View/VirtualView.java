package it.polimi.ingsfw.ingsfwproject.View;

import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtualView {
    private int gameID;
    private String nickname;
    private ArrayList<String> listOfPlayers;
    private HashMap<String, Map<Coordinate, Face>> grids; //tutte le grid, compresa quella di player
    private HashMap<String, HashMap<Content, Integer>> resources;
    private ArrayList<Coordinate> availablePositions;
    private ArrayList<PlayableCard> handCards;
    private Deck resourceDeck;
    private Deck goldDeck;
    private ArrayList<ObjectiveCard> handObjectives;
    private ArrayList<PlayableCard>  displayedCards;
    private PlayerColor color;
    private Map<String, Integer> scores;
    private GameState state;
    private Player currentPlayer;
    private List<ObjectiveCard> displayedObjectiveCards;
    private boolean currentPlayerhasPlayed;

    public VirtualView() {
        this.listOfPlayers = new ArrayList<String>();
        this.grids = new HashMap<>();
    }

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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isCurrentPlayerhasPlayed() {
        return currentPlayerhasPlayed;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCurrentPlayerhasPlayed(boolean currentPlayerhasPlayed) {
        this.currentPlayerhasPlayed = currentPlayerhasPlayed;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setListOfPlayers(ArrayList<String> listOfPlayers) {
        this.listOfPlayers = listOfPlayers;
    }


    public void setAvailablePositions(ArrayList<Coordinate> availablePositions) {
        this.availablePositions = availablePositions;
    }

    public void addPlayer(String player) {
        listOfPlayers.add(player);
    }

    public void setGridForPlayer(String nickname, Map<Coordinate, Face> grid) {
        grids.put(nickname, grid);
    }

    public void setResourcesForPlayer(String nickname, HashMap<Content, Integer> playerResources) {
        resources.put(nickname, playerResources);
    }




}
