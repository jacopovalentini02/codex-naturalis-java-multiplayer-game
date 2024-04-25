package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.CardNotInHandException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;
import it.polimi.ingsfw.ingsfwproject.Network2.ClientCallbackInterface;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private String username;
    private int points;
    private PlayerColor token;
    private PlayerGround ground;
    private ArrayList<PlayableCard> handCard;

    private ClientCallbackInterface client;

    public ArrayList<ObjectiveCard> getHandObjective() {
        return handObjective;
    }

    public void setHandObjective(ArrayList<ObjectiveCard> handObjective) {
        this.handObjective = handObjective;
    }

    private ArrayList<ObjectiveCard> handObjective;


    public Player(String username) {
        this.username = username;
        this.points = 0; // At the beginning points are equal to 0
        this.token = null;
        this.ground = new PlayerGround();
        this.handCard = new ArrayList<>();
        this.handObjective = new ArrayList<>();
    }

    public void draw(Deck deck) throws DeckEmptyException {
        Card drawnCard = deck.draw();
        if (drawnCard != null) {
            handCard.add((PlayableCard) drawnCard);
        } else {
            System.out.println("Deck is empty");
        }
    }
    public void pick(Card card){
        handCard.add((PlayableCard) card);
    }

    public int playCard(PlayableCard cardPlayed, boolean upwards, Coordinate coord) throws CardNotInHandException, PositionNotAvailableException, NotEnoughResourcesException {
        int points = 0;
        if (handCard.contains(cardPlayed)) {
            points = ground.playCard(cardPlayed, upwards, coord);
            // remove card from player's hand
            handCard.remove(cardPlayed);
        } else {
            throw new CardNotInHandException("The card chosen is not in the player's hand");
        }
        return points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public PlayerColor getToken() {
        return token;
    }

    public void setToken(PlayerColor token) {
        this.token = token;
    }

    public PlayerGround getGround() {
        return ground;
    }

    public void setGround(PlayerGround ground) {
        this.ground = ground;
    }

    public ArrayList<PlayableCard> getHandCard() {
        return handCard;
    }

    public void setHandCard(ArrayList<PlayableCard> handCard) {
        this.handCard = handCard;
    }
    @Override
    public String toString(){
        return this.username;
    }

    public void addClient(ClientCallbackInterface client){
        this.client = client;
    }

}
