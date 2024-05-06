package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.CardNotInHandException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class Player implements Serializable {
    private String username;
    private PlayerColor token;
    private PlayerGround ground;
    private ArrayList<PlayableCard> handCard;
    private final GameServerInstance gameServerInstance;

    public ArrayList<ObjectiveCard> getHandObjective() {
        return handObjective;
    }

    public void setHandObjective(ArrayList<ObjectiveCard> handObjective) {
        this.handObjective = handObjective;
    }

    private ArrayList<ObjectiveCard> handObjective;


    public Player(String username, GameServerInstance gameServerInstance) {
        this.username = username;
        this.gameServerInstance = gameServerInstance;
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

        //TODO MESSAGGIO

//        try { //updating client's hand
//            client.updateHand(this.getHandCard());
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
    }
    public void pick(Card card){
        handCard.add((PlayableCard) card);
        //TODO MESSAGGIO

//        try {
//            this.client.updateHand(this.getHandCard());
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
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
        //TODO MESSAGGIO
//
//        try {
//            this.client.updateAvailablePositions(this.ground.getAvailablePositions()); solo per me
//            this.client.updateHand(this.getHandCard()); per me
//            this.client.updateGrid(this.ground.getGrid()); per me - il client se è il suo nickname setta il suo player
//            this.client.updateResources(generateContentMap());
//        } catch (RemoteException e) { //updating clients
//            throw new RuntimeException(e);
//        }

        return points;
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
    @Override
    public String toString(){
        return this.username;
    }


    private HashMap<Content, Integer> generateContentMap(){
        HashMap<Content, Integer> resources = new HashMap<>();
        resources.put(Content.FUNGI_KINGDOM, this.ground.getContentCount(Content.FUNGI_KINGDOM));
        resources.put(Content.ANIMAL_KINGDOM, this.ground.getContentCount(Content.ANIMAL_KINGDOM));
        resources.put(Content.INSECT_KINGDOM, this.ground.getContentCount(Content.INSECT_KINGDOM));
        resources.put(Content.PLANT_KINGDOM, this.ground.getContentCount(Content.PLANT_KINGDOM));
        resources.put(Content.QUILL, this.ground.getContentCount(Content.QUILL));
        resources.put(Content.INKWELL, this.ground.getContentCount(Content.INKWELL));
        resources.put(Content.MANUSCRIPT, this.ground.getContentCount(Content.MANUSCRIPT));
        return resources;
    }


}
