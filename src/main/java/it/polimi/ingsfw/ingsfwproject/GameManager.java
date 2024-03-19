package it.polimi.ingsfw.ingsfwproject;

import java.util.*;

public class GameManager {
    private ArrayList<Game> gameList;

    public void createGame(){

    }

    public void joinGame(String nick, int idGame){

    }

    public void deleteGame(int idGame){

    }

    public static void main(String[] args){
        Game game1= new Game();
        Deck prova=new Deck();

        game1.setupGame();

        prova=game1.getGoldDeck();

        for (Card card : prova.getCardList())
        {
            System.out.println(card.getIdCard()); // Customize this based on your card properties
        }




    }
}
