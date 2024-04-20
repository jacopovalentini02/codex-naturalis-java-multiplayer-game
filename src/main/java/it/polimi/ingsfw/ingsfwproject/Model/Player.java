package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;

import java.util.*;

public class Player {
    private String username;
    private int points;
    private PlayerColor token;
    private PlayerGround ground;
    private ArrayList<PlayableCard> handCard;
    private ObjectiveCard handObjective;


    public Player(String username) {
        this.username = username;
        this.points = 0; // At the beginning points are equal to 0
        this.token = null;
        this.ground = new PlayerGround();
        this.handCard = new ArrayList<>();
        this.handObjective = null;
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

    public ObjectiveCard getHandObjective() {
        return handObjective;
    }

    public void setHandObjective(ObjectiveCard handObjective) {
        this.handObjective = handObjective;
    }
}
