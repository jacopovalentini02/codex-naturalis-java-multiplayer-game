package it.polimi.ingsfw.ingsfwproject.Controller;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;

/**
 * The {@code GameController} class implements the {@code Controller} interface to manage game-related logic and interactions.
 *
 * This controller is responsible for coordinating actions and behaviors within the game, handling player input,
 * updating the game state, and interacting with the game model and view components.
 *
 */

public class GameController implements Controller {
    private final Game model;

    public Queue<ChatMessage> globalChat;

    private int starterCardsPlayed;
    private int colorChosen;

    private final GameServerInstance serverInstance;

    /**
     * Constructs a new {@code GameController}.
     * @param model the {@code Game} that this controller is managing.
     * @param serverInstance the {@code GameServerInstance} that manages.
     */
    public GameController(Game model, GameServerInstance serverInstance){
        this.model = model;
        starterCardsPlayed = 0;
        colorChosen=0;
        this.serverInstance = serverInstance;
        this.globalChat = new LinkedBlockingQueue<>();
    }

    /**
     * This method allows a player to choose an {@code ObjectiveCard} during the initial phase of the game.
     * It sends an {@code ExceptionMessage} if it's not the turn of the player that called this method, or if the
     * {@code Gamestate} is not equal to {@code CHOOSING_OBJECTIVE}.
     * @param username the username of the player that want to choose a {@code ObjectiveCard}.
     * @param cardID the {@code Card} ID of the card that the player wants to choose.
     */
    public void chooseObjectiveCard(String username, int cardID){

        Player player = null;
        Card card = null;
        boolean moveSuccesful = false;

        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }
        assert player != null;

        for (ObjectiveCard pc: player.getHandObjective()){
            if (pc.getIdCard() == cardID)
                card = pc;
        }

        if (model.getCurrentPlayer() != player) {
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "Not your turn"));
            return;
        }

        if (model.getState() != GameState.CHOOSING_OBJECTIVES) {
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "Phase exception"));
            return;
        }

        synchronized (model){
            moveSuccesful = model.chooseObjectiveCard(player, card);

            if (!moveSuccesful)
                return;

            if(model.getObjectiveCardsChosen()!=model.getNumOfPlayers())
                model.nextTurn();
        }
    }

    /**
     * Allows a player to play a card in their {@code Playerground}.
     *
     * This method performs several checks before allowing the action:
     * <ul>
     *   <li>Sends an error message to the client if it's not the player's turn.</li>
     *   <li>Sends an error message to the client if the player doesn't have available positions.</li>
     *   <li>Sends an error message to the client if the {@code GameState} is one of the following:
     *       {@code WAITING_FOR_PLAYERS}, {@code CHOOSING_OBJECTIVES}, {@code ENDED}, or {@code CHOOSING_COLOR}.</li>
     *   <li>Sends an error message to the client if the player has already played a card this turn.</li>
     *   <li>Sends an error message to the client if the chosen {@code Coordinate} is not available.</li>
     * </ul>
     *
     * If all checks pass, the method allows the player to play the card and then enables them to draw/pick a card.
     *
     * @param username the username of the player who wants to play a card.
     * @param cardID the ID of the card the player wants to play.
     * @param upwards {@code true} if the player wants to play the card upwards, {@code false} if downwards.
     * @param coord the {@code Coordinate} where the player wants to play the card.
     */
    public void playCard(String username, int cardID, boolean upwards, Coordinate coord){
        int pointsMade = 0;
        Player player = null;
        PlayableCard card = null;
        boolean moveSuccessful = false;
        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }
        assert player != null;

        if (model.getCurrentPlayer() != player) {
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "Not your turn"));
            return;
        }

        if(!player.canPlay()){
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "You don't have available position! Wait for the game to end"));
            return;
        }


        for (PlayableCard pc: player.getHandCard()){
            if (pc.getIdCard() == cardID)
                card = pc;
        }

        if (model.getState() == GameState.WAITING_FOR_PLAYERS || model.getState() == GameState.CHOOSING_OBJECTIVES || model.getState() == GameState.ENDED || model.getState() == GameState.CHOOSING_COLORS) {
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "You can't play a card now"));
            return;
        }

        if(model.getifCurrentPlayerhasPlayed()) {
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "You have already played, it's time to draw"));
            return;
        }
        synchronized (model){
            pointsMade = player.playCard(card, upwards, coord);

            if(pointsMade == -1) {
                return;
            } else {
                model.updatePoints(pointsMade, player);
            }

            if(player.getGround().getAvailablePositions().isEmpty()){
                serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(),"There are no more available position! Wait for the game to end"));
                player.setCanPlay(false);
            }

            if (card instanceof StarterCard)
                starterCardPlayed();

            if (card instanceof StarterCard || model.getState() == GameState.ENDING){
                model.nextTurn();
            } else {
                model.setCurrentPlayerhasPlayed(true);
                serverInstance.sendUpdateToAll(new CurrentPlayerHasPlayedMessage(player.getClientID(),true));
            }
        }
    }

    /**
     * Allows a player to draw one of the displayed card.
     *
     * @param username the player who wants to draw a displayed card
     * @param cardID the id of the card that he wants to draw.
     */
    public void drawDisplayedPlayableCard(String username, int cardID){

        Player player = null;
        PlayableCard card = null;
        boolean moveSuccesful = false;

        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }


        for (PlayableCard pc: model.getDisplayedPlayableCard()){
            if (pc.getIdCard() == cardID)
                card = pc;
        }

        moveSuccesful = checkIfDrawPossible(player);
        if (!moveSuccesful)
            return;


        synchronized (model){
             moveSuccesful = model.drawDisplayedPlayableCard(card, player);

             if (!moveSuccesful)
                 return;

            if (model.getCurrentPlayer().equals(model.getPotentialWinner())) {
                model.setState(GameState.ENDING);
            }
            model.nextTurn();
        }


    }

    /**
     * Allows a player to draw from a deck.
     *
     * @param username the player who wants to draw from a deck.
     * @param resourceDeck {@code true} to draw from {@code ResourceDeck}, {@code false} to draw from {@code GoldDeck}.
     */
    public void draw(String username,boolean resourceDeck)  {
        Deck deck;
        Player player = null;
        boolean moveSuccessfull;

        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }
        assert player != null;


        if (resourceDeck){
            deck = model.getResourceDeck();
        } else {
            deck = model.getGoldDeck();
        }

        moveSuccessfull = checkIfDrawPossible(player);

        if (!moveSuccessfull)
            return;


        if (deck.equals(model.getObjectiveDeck())){
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(),"You can't draw from objective deck!"));
            return;
        }

        synchronized (model){
            moveSuccessfull=player.draw(deck);//

            if(moveSuccessfull){
                if (resourceDeck){
                    serverInstance.sendUpdateToAll(new ResourceDeckMessage(-10, deck));
                } else {
                    serverInstance.sendUpdateToAll(new GoldDeckMessage(-10, deck));
                }

                if (model.getCurrentPlayer().equals(model.getPotentialWinner())) {
                    model.setState(GameState.ENDING);
                }
                model.nextTurn();
            }


        }

    }

    /**
     * Allows a player to choose a color.
     * It sends an {@code ExceptionMessage} if it's not the player's turn or if the {@code GameState} isn't equal to {@code CHOOSING_COLOR}.
     * @param username the player who wants to choose a color.
     * @param color the color that the player wants to pick.
     */
    public void chooseColor(String username, PlayerColor color) {
        boolean moveSuccessfull=true;

        Player player = null;
        for (Player p: model.getListOfPlayers()){
            if (Objects.equals(p.getUsername(), username))
                player = p;
        }

        if (model.getCurrentPlayer() != player){
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(),"Not your turn"));
            return;
        }

        if (model.getState() != GameState.CHOOSING_COLORS){
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(),"You can't choose a color now"));
            return;
        }

        synchronized (model){
            moveSuccessfull=model.chooseColor(player, color);
            if(moveSuccessfull){
                colorChosenCounter();
                model.nextTurn();
                if(colorChosen== getModel().getNumOfPlayers())
                    model.setupHandsAndObjectives();
            }

        }
    }

    /**
     * This method performs several checks:
     * <ul>
     *   <li>Sends an error message to the client if it's not the player's turn.</li>
     *   <li>Sends an error message to the client if the player doesn't have available positions.</li>
     *   <li>Sends an error message to the client if the {@code GameState} is one of the following:
     *       {@code WAITING_FOR_PLAYERS}, {@code CHOOSING_STARTER_CARD}, {@code CHOOSING_OBJECTIVES}, {@code ENDING}, or {@code CHOOSING_COLOR}.</li>
     *   <li>Sends an error message to the client if the player has not played a card in this turn yet.</li>
     * </ul>
     * @param player The player who wants to draw
     * @return {@code true} if the player can draw, {@code false} otherwise.
     */
    private boolean checkIfDrawPossible(Player player){
        if (model.getState() == GameState.WAITING_FOR_PLAYERS || model.getState() == GameState.CHOOSING_STARTER_CARDS || model.getState() == GameState.CHOOSING_OBJECTIVES || model.getState() == GameState.ENDING || model.getState() == GameState.CHOOSING_COLORS) {
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "You can't draw now"));
            return false;
        }

        if (model.getCurrentPlayer() != player){
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(),"It's not your turn"));
            return false;
        }

        if(!player.canPlay()){
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "You don't have available position! Wait for the game to end"));
            return false;
        }

        if (!(model.getifCurrentPlayerhasPlayed())) {
            serverInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "You should play a card before drawing"));
            return false;
        }

        return true;
    }

    /**
     * This method counts how many players have played the starter card. If this count matches the number of players,
     * the {@code GameState} is set to {@code CHOOSING_COLOR}.
     */
    private void starterCardPlayed() {
        starterCardsPlayed++;
        if (starterCardsPlayed == model.getNumOfPlayers()){
            model.setState(GameState.CHOOSING_COLORS);
        }
    }

    /**
     * This method counts how many players have chosen the color. If this count matches the number of players,
     * the {@code GameState} is set to {@code CHOOSING_ OBJECTIVES}.
     */
    private void colorChosenCounter() {
        colorChosen++;
        if (colorChosen == model.getNumOfPlayers()){
            model.setState(GameState.CHOOSING_OBJECTIVES);
        }
    }

    /**
     * This method sends the global message passed as a parameter to all players in the game.
     * @param message the {@code ChatMessage} that needs to be sent.
     */
    public void addMessageToGlobalChat(ChatMessage message){
        this.globalChat.add(message);
        serverInstance.sendUpdateToAll(new RecieveChatMessage(-10, message.getSender(), message.getRecipient(), message.getMessage()));
    }

    /**
     * This method sends the message passed as a parameter to the recipient contained in the message.
     * @param message the {@code ChatMessage} that needs to be sent.
     */
    public void forwardPrivateChatMessage(ChatMessage message){
        int recipientClientID = serverInstance.getClientIDbyNickname(message.getRecipient());
        if (recipientClientID == -1) {
            serverInstance.sendUpdateToAll(new ExceptionMessage(serverInstance.getClientIDbyNickname(message.getSender()), "There's no player with nick " + message.getRecipient()));
        } else {
            serverInstance.sendUpdateToAll(new RecieveChatMessage(recipientClientID, message.getSender(), message.getRecipient(), message.getMessage()));
        }
    }

    /**
     * Returns the {@code Game} to which this controller is connected.
     * @return the {@code Game} to which this controller is connected.
     */
    public Game getModel() {
        return model;
    }

    /**
     * Retrieves the player object associated with the given username.
     * @param username the username of the player to retrieve.
     * @return the {@code Player} object associated with the given username.
     */
    public Player getPlayer(String username){
       synchronized (model){
           return model.getPlayer(username);
       }
    }
}

