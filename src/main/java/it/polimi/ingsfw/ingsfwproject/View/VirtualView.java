package it.polimi.ingsfw.ingsfwproject.View;

import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class VirtualView {
    private int gameID;
    private ArrayList<String> listOfPlayers;
    private LinkedHashMap<String, Map<Coordinate, Face>> grids; //tutte le grid, compresa quella di player
    private HashMap<String, HashMap<Content, Integer>> resources;

    public ArrayList<Coordinate> getAvailablePositions() {
        return availablePositions;
    }

    private ArrayList<Coordinate> availablePositions;
    private ArrayList<PlayableCard> handCards;
    private Deck resourceDeck;
    private Deck goldDeck;
    private ArrayList<ObjectiveCard> handObjectives;
    private ArrayList<PlayableCard>  displayedCards;
    private PlayerColor color;
    private Map<String, Integer> scores;
    private GameState state;
    private String currentPlayer;
    private List<ObjectiveCard> displayedObjectiveCards;
    private boolean currentPlayerhasPlayed;
    private String winner;

    private Map<String, PlayerColor> playerColorMap;

    private Queue<ChatMessage> globalChat;

    private HashMap<String, Queue<ChatMessage>> privateChats;

    public VirtualView() {
        this.listOfPlayers = new ArrayList<String>();
        this.grids = new LinkedHashMap<>();
        this.handCards=new ArrayList<>();
        this.resources=new HashMap<>();
        this.globalChat = new LinkedBlockingQueue<>();
        this.playerColorMap=new HashMap<>();
        this.privateChats = new HashMap<>();
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


    public boolean isCurrentPlayerhasPlayed() {
        return currentPlayerhasPlayed;
    }



    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getWinner() {
        return winner;
    }

    public void setCurrentPlayerhasPlayed(boolean currentPlayerhasPlayed) {
        this.currentPlayerhasPlayed = currentPlayerhasPlayed;
    }


    public void setListOfPlayers(ArrayList<String> listOfPlayers) {
        this.listOfPlayers = listOfPlayers;

        for (String nickname : this.listOfPlayers)
            privateChats.computeIfAbsent(nickname, key -> new LinkedBlockingQueue<>()); //adding private chats to the map

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

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void addMessageToGlobalChat(ChatMessage message){
        this.globalChat.add(message);
    }

    public Queue<ChatMessage> getGlobalChat(){
        return globalChat;
    }

    public Queue<ChatMessage> getPrivateChat(String nickname){
        return privateChats.getOrDefault(nickname, null);
    }

    public void addMessageToPrivateChat(ChatMessage message){
        String senderNick = message.getSender();
        if (privateChats.containsKey(senderNick)){
            privateChats.get(senderNick).add(message);
        }
    }

    public void sendPrivateMessage(ChatMessage message){
        String recipientNick = message.getRecipient();
        if (privateChats.containsKey(recipientNick))
            privateChats.get(recipientNick).add(message);
    }

    public void setPlayerColorMap(Map<String, PlayerColor> playerColorMap) {
        this.playerColorMap = playerColorMap;
    }

    public Map<String, PlayerColor> getPlayerColorMap() {
        return playerColorMap;
    }

    public HashMap<String, Map<Coordinate, Face>> getGrids() {
        return grids;
    }

    public ArrayList<String> getListOfPlayers(){
        return listOfPlayers;
    }
}
