package it.polimi.ingsfw.ingsfwproject;

import java.io.FileReader;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.IOException;

public class Game {
    private int idGame;
    private List<Player> listOfPlayers;
    private int numOfPlayers;
    private Map<Player, Integer> scores;
    private Player firstPlayer;
    private Deck resourceDeck;
    private Deck goldDeck;
    private Deck objectiveDeck;
    private ArrayList<PlayableCard> displayedPlayableCard;
    private ArrayList<ObjectiveCard> displayedObjectiveCard;
    private Player currentPlayer;

    public int getIdGame() {
        return idGame;
    }

    public List<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public Map<Player, Integer> getScores() {
        return scores;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Deck getResourceDeck() {
        return resourceDeck;
    }

    public Deck getGoldDeck() {
        return goldDeck;
    }

    public Deck getObjectiveDeck() {
        return objectiveDeck;
    }

    public ArrayList<PlayableCard> getDisplayedPlayableCard() {
        return displayedPlayableCard;
    }

    public ArrayList<ObjectiveCard> getDisplayedObjectiveCard() {
        return displayedObjectiveCard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public void setListOfPlayers(List<Player> listOfPlayers) {
        this.listOfPlayers = listOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void setScores(Map<Player, Integer> scores) {
        this.scores = scores;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void setResourceDeck(Deck resourceDeck) {
        this.resourceDeck = resourceDeck;
    }

    public void setGoldDeck(Deck goldDeck) {
        this.goldDeck = goldDeck;
    }

    public void setObjectiveDeck(Deck objectiveDeck) {
        this.objectiveDeck = objectiveDeck;
    }

    public void setDisplayedPlayableCard(ArrayList<PlayableCard> displayedPlayableCard) {
        this.displayedPlayableCard = displayedPlayableCard;
    }

    public void setDisplayedObjectiveCard(ArrayList<ObjectiveCard> displayedObjectiveCard) {
        this.displayedObjectiveCard = displayedObjectiveCard;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setupGame(){
        resourceDeck = new Deck();
        goldDeck = new Deck();




        try{
//            Gson gson = new Gson();
//            ResourceCard preRes=new ResourceCard();
//            JsonElement json = null;
//            Card[] cards = gson.fromJson(new FileReader("/Users/beatricespazzadeschi/IdeaProjects/ing-sw-2024-zanoni-valentini-spazzadeschi-spandri/src/main/java/it/polimi/ingsfw/ingsfwproject/cards.json"), Card[].class);
//
//
//            for(int i=0; i<40; i++){
//                resourceDeck.addCard(cards[i]);
//
//                JsonObject jsonObject = json.getAsJsonObject();
//                int idCard = jsonObject.get("id").getAsInt();
//                String centerS = jsonObject.get("center").getAsString();
//                Content center=Content.valueOf(centerS);
//                int points=jsonObject.get("points").getAsInt();
//
//                String[] cornersS = new String[0];
//                Content[] corners = new Content[0];
//
//                System.out.println(idCard);
//                System.out.println(center);
//                System.out.println(points);
//
//                for(int j=0; j<4; j++){
//                    cornersS[j]=jsonObject.get("corners").getAsString();
//                    corners[j]=Content.valueOf(cornersS[j]);
//
//                    System.out.println(cornersS[j]);
//
//                }
//
//                preRes.createCard(idCard, center, points, corners);


//            }



        }catch (IOException e) {
            // Handle file-related exceptions
            e.printStackTrace();
        } catch (JsonParseException e) {
            // Handle JSON parsing exceptions
            e.printStackTrace();
        }

    }

    public void endGame(){

    }

    /*public Player addPlayer(String nick){

    }*/

    public void nextTurn(){

    }

    public void lastTurn(){

    }

    public void playCard(Player player, Card card, Coordinate coord, Boolean upwards){

    }

    public void drawDisplayedPlayableCard(Player player, PlayableCard card){

    }

    public void draw(Player player, Deck deck){

    }

    public void updatePoints(Player player, int score){

    }

    public void finalScoreCheck(){

    }
}