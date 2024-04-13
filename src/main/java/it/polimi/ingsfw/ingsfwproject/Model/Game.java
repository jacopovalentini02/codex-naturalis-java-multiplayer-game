package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.faceReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Game {
    private int idGame;
    private GameController controller = new GameController();
    private List<Player> listOfPlayers;
    private int numOfPlayers;
    private Map<Player, Integer> scores;
    private Player firstPlayer;
    private Deck resourceDeck;
    private Deck goldDeck;
    private Deck objectiveDeck;

    private Deck starterDeck;

    public Deck getStarterDeck() {
        return starterDeck;
    }

    public void setStarterDeck(Deck starterDeck) {
        this.starterDeck = starterDeck;
    }

    private List<PlayableCard> displayedPlayableCard;
    private List<ObjectiveCard> displayedObjectiveCard;
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

    public void setupGame() {
        //invoking the instantiation of all game's cards
        setUpCards();
        //Shuffle the Resource cards and place them facedown in the center of the table. Draw 2 cards and place them faceup.
        resourceDeck.shuffle();
        displayedPlayableCard = new ArrayList<PlayableCard>();
        displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
        displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
        //Shuffle the Gold cards and place them facedown in the center of the table. Draw 2 cards and place them faceup.
        goldDeck.shuffle();
        displayedPlayableCard.add((PlayableCard) goldDeck.draw());
        displayedPlayableCard.add((PlayableCard) goldDeck.draw());
        //Each player randomly takes one Starter card and choose the face to be played
        starterDeck.shuffle();
        //creating the arrayList representing the token available
        List<PlayerColor> tokenAvailable = new ArrayList<>();
        tokenAvailable.add(PlayerColor.GREEN);
        tokenAvailable.add(PlayerColor.RED);
        tokenAvailable.add(PlayerColor.BLUE);
        tokenAvailable.add(PlayerColor.YELLOW);
        //initializing the score track
        scores = new HashMap<>();
        //cycling on every player the beginning operations
        for (Player p : listOfPlayers){
            StarterCard starter = (StarterCard) starterDeck.draw();
            //choosing the face
            controller.playCard(p, starter, faceReader.getBoolean(), new Coordinate(0,0));
            //picking the token
            System.out.println("Puoi scegliere tra i seguenti colori: ");
            for(PlayerColor color : tokenAvailable){
                System.out.println(color);
            }
            Scanner scanner = new Scanner(System.in);
            String colorChoosen = scanner.next().toUpperCase();
            scanner.close();
            p.setToken(PlayerColor.valueOf(colorChoosen));
            tokenAvailable.remove(PlayerColor.valueOf(colorChoosen));
            //placing the token on the 0 of the score track
            scores.put(p, 0);
            //draw 2 resourceCard e 1 goldCard
            p.getHandCard().add((PlayableCard) resourceDeck.draw());
            p.getHandCard().add((PlayableCard) resourceDeck.draw());
            p.getHandCard().add((PlayableCard) goldDeck.draw());
        }
        //shuffling the objectiveDeck
        objectiveDeck.shuffle();
        //placing the 2 common objective on the table
        displayedObjectiveCard = new ArrayList<>();
        displayedObjectiveCard.add((ObjectiveCard) objectiveDeck.draw());
        displayedObjectiveCard.add((ObjectiveCard) objectiveDeck.draw());
        //Each player receives 2 Objective cards, they look at them and choose one of them.
        for(Player p : listOfPlayers){
            //draw 2 objective cards
            Deck cardsToChooseWithin = new Deck();
            cardsToChooseWithin.addCard(objectiveDeck.draw());
            cardsToChooseWithin.addCard(objectiveDeck.draw());
            //letting the player choose which card he wants
            System.out.println("Che carta vuoi tenere tra queste (indicare id):\n");
            cardsToChooseWithin.printCardsDeck();
            Scanner scanner = new Scanner(System.in);
            int cardChoosen = scanner.nextInt();
            scanner.close();
            //finding the card choosen by the player
            for(Card o : cardsToChooseWithin.getCardList()){
                if(o.getIdCard() == cardChoosen){
                    cardsToChooseWithin.getCardList().remove(o);
                    p.setHandObjective((ObjectiveCard)o);
                    break;
                }
            }
            //with the .addCard method, it should be possible adding the card directly at the bottom of the deck
            objectiveDeck.addCard(cardsToChooseWithin.getCardList().get(0));
        }
        //choosing randomly the first player
        Random rand = new Random();
        int index = rand.nextInt(listOfPlayers.size());
        setFirstPlayer(listOfPlayers.get(index));
    }


    public void setUpCards(){
        resourceDeck = new Deck();
        goldDeck = new Deck();
        starterDeck =new Deck();
        objectiveDeck=new Deck();

        try {
            // path to JSON file
            String filePath = "src/main/java/it/polimi/ingsfw/ingsfwproject/Model/cards.json";

            // reading of JSON file
            FileReader reader = new FileReader(filePath);

            // Parsing of JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            // Obtaining array of cards from JSON
            JSONArray cardsArray = jsonObject.getJSONArray("cards");

            // Cycling on every object of the JSONArray
            for (int i = 0; i < cardsArray.length(); i++) {
                JSONObject cardObject = cardsArray.getJSONObject(i);

                // Extracting cards data
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

            // close reader
            reader.close();

            //Shuffle methods that i'll insert in the main function 'setupGame'
           /* goldDeck.shuffle();
            starterDeck.shuffle();
            objectiveDeck.shuffle();
            resourceDeck.shuffle();

            //check method to see if we did correct
            goldDeck.printCardsDeck();*/

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


    public void updatePoints(Player player, int score){

    }

    public void finalScoreCheck(){

    }
}