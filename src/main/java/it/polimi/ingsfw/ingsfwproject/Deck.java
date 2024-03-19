package it.polimi.ingsfw.ingsfwproject;

import java.util.*;

public class Deck {


    private ArrayList<Card> cardList;

    /*public Card draw(){

    }*/

    public void shuffle(){

    }

    public void addCard(Card card){
        cardList.add(card);
    }

    public ArrayList<Card> getCardList() {
        return cardList;
    }
}
