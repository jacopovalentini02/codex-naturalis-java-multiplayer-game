package it.polimi.ingsfw.ingsfwproject.View;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class View implements Runnable{
    public static Client client;
    //public BlockingQueue<Message> messages;

    /**
     * Handle the choice between RMI and Socket.
     */
    public abstract void chooseConnection();

    /**
     * Notifies the user of a game-related exception by displaying the error.
     *
     * @param message The error message to display in the alert.
     */
    public abstract void notifyException(String message);

    /**
     * Displays the first message upon successful connection.
     *
     * @param clientID The ID of the client for which the connection was established.
     */
    public abstract void displayFirstMessage(int clientID);

    /**
     * Displays the list of games.
     *
     * @param gameList A HashMap containing the list of game IDs and their respective player counts.
     */
    public abstract void displayGameList(HashMap<Integer, Integer> gameList);

    /**
     * Notifies the view that the client has joined a game.
     *
     * @param idGame The ID of the game that the client has joined.
     */
    public abstract void notifyGameJoined(int idGame);

    /**
     * Notifies the view that new players have joined the game.
     *
     * @param nicknames The list of nicknames of the new players who have joined.
     */
    public abstract void notifyNewPlayerJoined(ArrayList<String> nicknames);

    /**
     * Notifies the view that the starter card has been chosen.
     */
    public abstract void notifyStarterCard();

    /**
     * Notifies the view that the available player colors have been updated.
     *
     * @param colors The list of available player colors.
     */
    public abstract void notifyColorsAvailable(List<PlayerColor> colors);

    /**
     * Notifies the view that the gold deck has been updated.
     */
    public abstract void notifyGoldDeckUpdate();

    /**
     * Notifies the view that the resource deck has been updated.
     */
    public abstract void notifyResourceDeckUpdate();

    /**
     * Notifies the view that the displayed cards have been updated.
     *
     * @param displayedCards The list of playable cards to be displayed.
     */
    public abstract void notifyDisplayedCardsUpdate(ArrayList<PlayableCard> displayedCards);

    /**
     * Notifies the view of the current player's turn.
     *
     * @param nickname The nickname of the current player.
     */
    public abstract void notifyCurrentPlayer(String nickname);

    public abstract void notifyAvailablePositions(ArrayList<Coordinate> coord);

    /**
     * Notifies the view about updates to the hand of objective cards.
     *
     * @param cards The list of objective cards to be displayed in the GUI.
     */
    public abstract void notifyHandObjectives(ArrayList<ObjectiveCard> cards);

    /**
     * Notifies the view about changes in the game state.
     * *
     * @param state The current state of the game.
     */
    public abstract void notifyGameState(GameState state);

    /**
     * Notifies the view about updates to the grid for a specific player.
     *
     * @param nickname The nickname of the player whose grid is being updated.
     * @param grid     The updated grid
     */
    public abstract void notifyGridUpdate(String nickname, Map<Coordinate, Face> grid);

    public abstract void notifyResourcesUpdate(String nickname, HashMap<Content, Integer> resources);

    /**
     * Notifies the view about the winner of the game.
     *
     * @param nickname The nickname of the player who has won the game.
     */
    public abstract void notifyWinnerUpdate(String nickname);

    /**
     * Notifies the view about updates to the hand cards of the current player.
     *
     * @param cards The list of playable cards in the current player's hand.
     */
    public abstract void notifyHandCardsUpdate(ArrayList<PlayableCard> cards);

    public abstract void notifyDisplayedObjectives(List<ObjectiveCard> cards);

    /**
     * Notifies the view about updates to the scores of players in the game.
     *
     * @param scores A map containing player nicknames as keys and their corresponding scores as values.
     */
    public abstract void notifyScores(Map<String, Integer> scores);

    public abstract void notifyColorChosen(PlayerColor color);

    /**
     * Notifies the view about a new chat message received.
     *
     * @param message The chat message to be displayed.
     */
    public abstract void notifyChatMessage(ChatMessage message);

    public abstract void notifyCurrentPlayerHasPlayed(boolean bool);


}
