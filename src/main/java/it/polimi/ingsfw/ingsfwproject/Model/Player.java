package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

public class Player {
    private String username;
    private PlayerColor token;
    private PlayerGround ground;
    private ArrayList<PlayableCard> handCard;
    private ObjectiveCard handObjective;


    public Player(String username, PlayerColor token) {
        this.username = username;
        this.token = token;
        this.ground = new PlayerGround();
        this.handCard = new ArrayList<>();
        this.handObjective = null;
    }

    public void draw(Deck deck){
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
