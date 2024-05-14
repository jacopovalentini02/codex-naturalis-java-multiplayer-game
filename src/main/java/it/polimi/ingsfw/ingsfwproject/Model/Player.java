package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.CardNotInHandException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class Player implements Serializable {
    private int clientID;
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


    public Player(String username, GameServerInstance gameServerInstance, int clientID) {
        this.username = username;
        this.gameServerInstance = gameServerInstance;
        this.token = null;
        this.ground = new PlayerGround();
        this.handCard = new ArrayList<>();
        this.handObjective = new ArrayList<>();
        this.clientID = clientID;
    }

    public boolean draw(Deck deck) {
        Card drawnCard = null;
        try{
            drawnCard = deck.draw();
        }catch (DeckEmptyException e){
            gameServerInstance.sendUpdateToAll(new ExcpetionMessage(this.clientID,e.getMessage()));
            return false;
        }

        if (drawnCard != null) {
            addToHand((PlayableCard)drawnCard);
            return true;
        } else {
            System.out.println("Deck is empty");
            return false;
        }

    }
    public void pick(Card card){
        addToHand((PlayableCard) card);
    }

    public int playCard(PlayableCard cardPlayed, boolean upwards, Coordinate coord){
        int points = 0;
        if (handCard.contains(cardPlayed)) {
            try{
                points = ground.playCard(cardPlayed, upwards, coord);
            }catch(PositionNotAvailableException | NotEnoughResourcesException e){
                gameServerInstance.sendUpdateToAll(new ExcpetionMessage(this.clientID,e.getMessage()));
            }

            // remove card from player's hand
            handCard.remove(cardPlayed);
        } else {
            gameServerInstance.sendUpdateToAll(new ExcpetionMessage(this.clientID,"The card chosen is not in the player's hand"));
        }

        gameServerInstance.sendUpdateToAll(new CoordinatesAvailableMessage(clientID, this.ground.getAvailablePositions()));
        gameServerInstance.sendUpdateToAll(new HandCardsMessage(clientID, this.getHandCard()));
        gameServerInstance.sendUpdateToAll(new GridMessage(-10, this.ground.getGrid(), this.username));
        gameServerInstance.sendUpdateToAll(new ResourcesMessage(-10, generateContentMap(), username));
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
        gameServerInstance.sendUpdateToAll(new ColorChosenMessage(clientID, token));
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


    //todo mettere priavate
    public HashMap<Content, Integer> generateContentMap(){
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

    public void addToHand(PlayableCard card){
        this.handCard.add(card);
        //TODO: notify clients
        gameServerInstance.sendUpdateToAll(new HandCardsMessage(clientID, handCard));
    }

    public void addToHand(ArrayList<PlayableCard> cards){
        this.handCard.addAll(cards);
        gameServerInstance.sendUpdateToAll(new HandCardsMessage(clientID, handCard));
    }

    public void addToHandObjective(ArrayList<ObjectiveCard> cards){
        this.handObjective.addAll(cards);
        gameServerInstance.sendUpdateToAll(new HandObjectiveMessage(clientID, handObjective));
    }

    public int getClientID() {
        return clientID;
    }
}
