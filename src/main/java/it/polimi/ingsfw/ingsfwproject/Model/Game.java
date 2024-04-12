package it.polimi.ingsfw.ingsfwproject.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Game {
    private int idGame;
    private List<Player> listOfPlayers;
    private int numOfPlayers;
    private Map<Player, Integer> scores;
    private Player firstPlayer;
    private Player currentPlayer;
    private Deck resourceDeck;
    private Deck goldDeck;
    private Deck objectiveDeck;
    private Deck starterDeck;
    private ArrayList<PlayableCard> displayedPlayableCard;
    private ArrayList<ObjectiveCard> displayedObjectiveCard;

    public Deck getStarterDeck() {
        return starterDeck;
    }

    public void setStarterDeck(Deck starterDeck) {
        this.starterDeck = starterDeck;
    }

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

    public void setupGame() {
        setUpCards();
    }


    public void setUpCards(){
        resourceDeck = new Deck();
        goldDeck = new Deck();
        starterDeck =new Deck();
        objectiveDeck=new Deck();

        try {
            // Percorso al file JSON
            String filePath = "src/main/java/it/polimi/ingsfw/ingsfwproject/cards.json";

            // Lettura del file JSON
            FileReader reader = new FileReader(filePath);

            // Parsa il file JSON
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            // Ottieni l'array di carte dal JSON
            JSONArray cardsArray = jsonObject.getJSONArray("cards");

            // Itera su ogni oggetto nel JSONArray
            for (int i = 0; i < cardsArray.length(); i++) {
                JSONObject cardObject = cardsArray.getJSONObject(i);

                // Estrai i dati dalla carta JSON
                int id = cardObject.getInt("id");

                //Resource card 0-40
                if(i<=39){
                    resourceDeck.createResourceCard(cardObject, id);
                }else if(i<=79){
                    goldDeck.createGoldCard(cardObject, id, jsonObject);
                }else if(i<=85){
                    starterDeck.createStarterCard(cardObject, id);
                }else if(i<=93){
                    objectiveDeck.createStructObjective(cardObject, id);
                }else{
                    objectiveDeck.createNotStructObjective(cardObject, id);
                }
            }

            // Chiudi il lettore
            reader.close();

            //Shuffle
            goldDeck.shuffle();
            starterDeck.shuffle();
            objectiveDeck.shuffle();
            resourceDeck.shuffle();

            goldDeck.printCardsDeck();

        } catch (IOException | JSONException e) {
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