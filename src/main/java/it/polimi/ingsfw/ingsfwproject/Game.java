package it.polimi.ingsfw.ingsfwproject;

import java.io.FileReader;
import java.util.*;
import com.google.gson.Gson;

public class Game {
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

    public void setupGame(){
        resourceDeck = new Deck();
        goldDeck = new Deck();

        try{
            Gson gson = new Gson();
            Card[] cards = gson.fromJson(new FileReader("cards.json"), Card[].class);

            for (Card card : cards) {

                if (card instanceof ResourceCard) {
                    resourceDeck.addCard((ResourceCard) card);
                } else if (card instanceof GoldCard) {
                    goldDeck.addCard((GoldCard) card);
                }
            }

        }catch (Exception e) {
            System.out.println("Error with JSON file");
        }

    }

    public void endGame(){

    }

    public Player addPlayer(String nick){

    }

    public void nextTurn(){

    }

    public void lastTurn(){

    }

    public void playCard(Player player, Face face, Coordinates coord){

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