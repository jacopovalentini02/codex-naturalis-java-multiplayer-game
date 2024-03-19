package it.polimi.ingsfw.ingsfwproject;

import java.util.*;

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

    public void setupGame(){

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