package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NoMoreAvailablePosition;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Class Player
 *
 * Description: This class represents a player in the game. It manages the player's state, including their hand of cards, objectives, ground, and other properties.
 */
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 8578669925170943251L;
    private int clientID;
    private String username;
    private PlayerColor token;
    private PlayerGround ground;
    private ArrayList<PlayableCard> handCard;
    private final GameServerInstance gameServerInstance;
    private boolean canPlay;
    private ArrayList<ObjectiveCard> handObjective;

    /**
     * @return The list of objective cards in the player's hand.
     */
    public ArrayList<ObjectiveCard> getHandObjective() {
        return handObjective;
    }

    /**
     * Constructor for the Player class.
     *
     * @param username The username of the player.
     * @param gameServerInstance The game server instance.
     * @param clientID The client ID of the player.
     */
    public Player(String username, GameServerInstance gameServerInstance, int clientID) {
        this.username = username;
        this.gameServerInstance = gameServerInstance;
        this.token = null;
        this.ground = new PlayerGround();
        this.handCard = new ArrayList<>();
        this.handObjective = new ArrayList<>();
        this.clientID = clientID;
        this.canPlay = true;
    }

    /**
     * Draws a card from the specified deck and adds it to the player's hand.
     *
     * @param deck The deck from which to draw a card.
     * @return True if a card is successfully drawn, otherwise false.
     */
    public boolean draw(Deck deck) {
        Card drawnCard = null;
        try {
            drawnCard = deck.draw();
        } catch (DeckEmptyException e) {
            gameServerInstance.sendUpdateToAll(new ExceptionMessage(this.clientID, e.getMessage()));
            return false;
        }

        if (drawnCard != null) {
            addToHand((PlayableCard) drawnCard);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the specified card to the player's hand.
     *
     * @param card The card to add to the hand.
     */
    public void pick(Card card) {
        addToHand((PlayableCard) card);
    }

    /**
     * Plays the specified card on the player's ground at the given coordinates.
     *
     * @param cardPlayed The card to play.
     * @param upwards Whether the card is placed upwards.
     * @param coord The coordinates where the card is placed.
     * @return The points awarded for playing the card, or -1 if an error occurs.
     */
    public int playCard(PlayableCard cardPlayed, boolean upwards, Coordinate coord) {
        int points = 0;
        if (handCard.contains(cardPlayed)) {
            try {
                points = ground.playCard(cardPlayed, upwards, coord);
            } catch (PositionNotAvailableException | NotEnoughResourcesException e) {
                gameServerInstance.sendUpdateToAll(new ExceptionMessage(this.clientID, e.getMessage()));
                return -1;
            }

            // Remove card from player's hand
            handCard.remove(cardPlayed);
        } else {
            gameServerInstance.sendUpdateToAll(new ExceptionMessage(this.clientID, "The card chosen is not in the player's hand"));
            return -1;
        }

        gameServerInstance.sendUpdateToAll(new CoordinatesAvailableMessage(clientID, this.ground.getAvailablePositions()));
        gameServerInstance.sendUpdateToAll(new HandCardsMessage(clientID, this.getHandCard()));
        gameServerInstance.sendUpdateToAll(new GridMessage(-10, this.ground.getGrid(), this.username));
        gameServerInstance.sendUpdateToAll(new ResourcesMessage(-10, generateContentMap(), username));
        return points;
    }

    /**
     * @return The username of the player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's token to the specified color and notifies all clients.
     *
     * @param token The color of the token.
     */
    public void setToken(PlayerColor token) {
        this.token = token;
        gameServerInstance.sendUpdateToAll(new ColorChosenMessage(-10, token, username));
    }

    /**
     * @return The player's ground.
     */
    public PlayerGround getGround() {
        return ground;
    }

    /**
     * @return The list of playable cards in the player's hand.
     */
    public ArrayList<PlayableCard> getHandCard() {
        return handCard;
    }

    /**
     * Generates a map of the resources and their counts in the player's ground.
     *
     * @return A map of resources and their counts.
     */
    public HashMap<Content, Integer> generateContentMap() {
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

    /**
     * Adds the specified card to the player's hand and notifies all clients.
     *
     * @param card The card to add to the hand.
     */
    public void addToHand(PlayableCard card) {
        this.handCard.add(card);
        gameServerInstance.sendUpdateToAll(new HandCardsMessage(clientID, handCard));
    }

    /**
     * Adds the specified list of cards to the player's hand and notifies all clients.
     *
     * @param cards The list of cards to add to the hand.
     */
    public void addToHand(ArrayList<PlayableCard> cards) {
        this.handCard.addAll(cards);
        gameServerInstance.sendUpdateToAll(new HandCardsMessage(clientID, handCard));
    }

    /**
     * Adds the specified list of objective cards to the player's hand and notifies all clients.
     *
     * @param cards The list of objective cards to add to the hand.
     */
    public void addToHandObjective(ArrayList<ObjectiveCard> cards) {
        this.handObjective.addAll(cards);
        gameServerInstance.sendUpdateToAll(new HandObjectiveMessage(clientID, handObjective));
    }

    /**
     * @return The player's token color.
     */
    public PlayerColor getToken() {
        return token;
    }

    /**
     * @return The client's ID.
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * @return Whether the player can play.
     */
    public boolean canPlay() {
        return canPlay;
    }

    /**
     * Sets whether the player can play.
     *
     * @param canPlay True if the player can play, otherwise false.
     */
    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }
}
