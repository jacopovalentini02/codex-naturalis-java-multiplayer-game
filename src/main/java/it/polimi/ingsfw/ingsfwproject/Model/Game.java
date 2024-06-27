package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class Game
 *
 * Description: This class manages the state and behavior of the game. It handles
 * the initialization, setup, and progression of the game, including managing players,
 * decks, and game states.
 */
public class Game {
    private int idGame;
    private GameState state;
    private GameController controller;
    private List<Player> listOfPlayers;
    private List<PlayerColor> tokenAvailable;
    private int numOfPlayers;
    private Map<Player, Integer> scores;
    private Player firstPlayer;
    private Deck resourceDeck;
    private Deck goldDeck;
    private Deck objectiveDeck;
    private Deck starterDeck;
    private ArrayList<PlayableCard> displayedPlayableCard;
    private List<ObjectiveCard> displayedObjectiveCard;
    private Player currentPlayer;
    private Player potentialWinner;
    private boolean currentPlayerhasPlayed;
    private Player winner;
    private final GameManager gameManager;
    int lastRoundsplayed;
    int colorChosen;
    private final GameServerInstance gameServerInstance;
    int objectiveCardsChosen;

    /**
     * @return The number of objective cards chosen.
     */
    public int getObjectiveCardsChosen() {
        return objectiveCardsChosen;
    }

    /**
     * @return Whether the current player has played.
     */
    public boolean getifCurrentPlayerhasPlayed() {return currentPlayerhasPlayed;}

    /**
     * Sets whether the current player has played.
     * @param bool True if the current player has played, otherwise false.
     */
    public void setCurrentPlayerhasPlayed(boolean bool){
        this.currentPlayerhasPlayed = bool;
    }

    /**
     * Constructor for the Game class.
     * Initializes a new game instance with the specified parameters.
     *
     * @param gameServerInstance The instance of the game server.
     * @param gameManager The game manager.
     * @param idGame The ID of the game.
     * @param numOfPlayers The number of players.
     * @param player1 The first player.
     */
    public Game(GameServerInstance gameServerInstance, GameManager gameManager, int idGame, int numOfPlayers, Player player1) {
        this.idGame = idGame;
        this.numOfPlayers = numOfPlayers;
        this.state=GameState.WAITING_FOR_PLAYERS;
        this.gameManager = gameManager;
        resourceDeck = new Deck();
        goldDeck = new Deck();
        starterDeck =new Deck();
        objectiveDeck=new Deck();
        listOfPlayers = new ArrayList<>();
        listOfPlayers.add(player1);
        controller = new GameController(this, gameServerInstance);
        displayedPlayableCard = new ArrayList<PlayableCard>();
        displayedObjectiveCard = new ArrayList<>();
        scores = new HashMap<>();
        tokenAvailable = new ArrayList<>();
        tokenAvailable.add(PlayerColor.GREEN);
        tokenAvailable.add(PlayerColor.RED);
        tokenAvailable.add(PlayerColor.BLUE);
        tokenAvailable.add(PlayerColor.YELLOW);
        currentPlayerhasPlayed = false;
        lastRoundsplayed = 0;
        objectiveCardsChosen = 0;
        colorChosen = 0;
        gameServerInstance.setGameController(this.controller);
        this.gameServerInstance=gameServerInstance;
        setCurrentPlayer(player1);
    }

    /**
     * Sets up the game field by initializing and shuffling the decks,
     * and dealing initial cards to players.
     */
    public synchronized void setupField() {
        setUpCards();
        resourceDeck.shuffle();
        try {
            displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
            displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
        } catch (DeckEmptyException ignore) {}

        goldDeck.shuffle();
        try {
            displayedPlayableCard.add((PlayableCard) goldDeck.draw());
            displayedPlayableCard.add((PlayableCard) goldDeck.draw());
        } catch (DeckEmptyException ignore) {}

        starterDeck.shuffle();
        this.setState(GameState.CHOOSING_STARTER_CARDS);

        for (Player p : listOfPlayers) {
            StarterCard starter = null;
            try {
                starter = (StarterCard) starterDeck.draw();
            } catch (DeckEmptyException ignore) {}
            p.addToHand(starter);
        }
        ArrayList<Coordinate> initialCoordinates = new ArrayList<Coordinate>();
        initialCoordinates.add(new Coordinate(0,0));
        gameServerInstance.sendUpdateToAll(new CoordinatesAvailableMessage(-10, initialCoordinates));

        gameServerInstance.sendUpdateToAll(new GoldDeckMessage(-10, this.goldDeck));
        gameServerInstance.sendUpdateToAll(new ResourceDeckMessage(-10, this.resourceDeck));
        gameServerInstance.sendUpdateToAll(new DisplayedPlayableCardsMessage(-10, this.displayedPlayableCard));
        gameServerInstance.sendUpdateToAll(new CurrentPlayerMessage(-10, currentPlayer.getUsername()));
    }

    /**
     * Allows a player to choose a color token if it is available.
     *
     * @param player The player choosing the color.
     * @param color The color chosen by the player.
     * @return True if the color is successfully chosen, otherwise false.
     */
    public boolean chooseColor(Player player, PlayerColor color)  {
        if (!tokenAvailable.contains(color)) {
            gameServerInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "This color is already taken"));
            return false;
        }
        player.setToken(color);
        tokenAvailable.remove(color);
        return true;
    }

    /**
     * Allows a player to choose an objective card from their hand.
     *
     * @param player The player choosing the objective card.
     * @param card The objective card chosen by the player.
     * @return True if the objective card is successfully chosen, otherwise false.
     */
    public boolean chooseObjectiveCard(Player player, Card card) {
        if (!player.getHandObjective().contains((ObjectiveCard) card)) {
            gameServerInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "You can't choose this card"));
            return false;
        }

        int indexToRemove = player.getHandObjective().getFirst().equals(card) ? 1 : 0;
        ObjectiveCard cardToPutBack = player.getHandObjective().remove(indexToRemove);

        gameServerInstance.sendUpdateToAll(new HandObjectiveMessage(player.getClientID(), player.getHandObjective()));
        this.objectiveDeck.addCard(cardToPutBack);
        objectiveCardsChosen++;

        if (objectiveCardsChosen == listOfPlayers.size()) {
            randomizeFirstPlayer();
        }
        return true;
    }

    /**
     * Sets up the hands and objectives for each player by dealing cards
     * and shuffling the objective deck.
     */
    public synchronized void setupHandsAndObjectives() {
        for (Player p : listOfPlayers) {
            scores.put(p, 0);
            try {
                ArrayList<PlayableCard> newHand = new ArrayList<PlayableCard>();
                newHand.add((PlayableCard) resourceDeck.draw());
                newHand.add((PlayableCard) resourceDeck.draw());
                newHand.add((PlayableCard) goldDeck.draw());
                p.addToHand(newHand);
            } catch (DeckEmptyException ignore) {}
        }
        objectiveDeck.shuffle();
        try {
            displayedObjectiveCard.add((ObjectiveCard) objectiveDeck.draw());
            displayedObjectiveCard.add((ObjectiveCard) objectiveDeck.draw());
        } catch (DeckEmptyException ignore) {}

        gameServerInstance.sendUpdateToAll(new DisplayedObjectiveMessage(-10, displayedObjectiveCard));
        for (Player p : listOfPlayers) {
            try {
                ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
                objectiveCards.add((ObjectiveCard) objectiveDeck.draw());
                objectiveCards.add((ObjectiveCard) objectiveDeck.draw());
                p.addToHandObjective(objectiveCards);
            } catch (DeckEmptyException ignore) {}
        }

        gameServerInstance.sendUpdateToAll(new GoldDeckMessage(-10, this.goldDeck));
        gameServerInstance.sendUpdateToAll(new ResourceDeckMessage(-10, this.resourceDeck));
    }

    /**
     * Randomizes and sets the first player for the game.
     */
    public void randomizeFirstPlayer() {
        Random rand = new Random();
        int index = rand.nextInt(listOfPlayers.size());
        setFirstPlayer(listOfPlayers.get(index));
        this.setState(GameState.STARTED);
        setCurrentPlayer(getFirstPlayer());
        updatePoints(19, getFirstPlayer());
    }

    /**
     * Sets up the cards by reading from a JSON file and creating
     * the respective decks.
     */
    public void setUpCards() {
        try {
            String filePath = "src/main/java/it/polimi/ingsfw/ingsfwproject/Model/cards.json";
            FileReader reader = new FileReader(filePath);
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            JSONArray cardsArray = jsonObject.getJSONArray("cards");

            for (int i = 0; i < cardsArray.length(); i++) {
                JSONObject cardObject = cardsArray.getJSONObject(i);
                int id = cardObject.getInt("id");

                if (i <= 39) {
                    resourceDeck.createResourceCard(cardObject, id);
                } else if (i <= 79) {
                    goldDeck.createGoldCard(cardObject, id);
                } else if (i <= 85) {
                    starterDeck.createStarterCard(cardObject, id);
                } else if (i <= 93) {
                    objectiveDeck.createStructObjective(cardObject, id);
                } else {
                    objectiveDeck.createNotStructObjective(cardObject, id);
                }
            }
            reader.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ends the game by deleting the game instance.
     */
    public void endGame() {
        gameManager.deleteGame(this.idGame);
    }

    /**
     * Adds a new player to the game.
     *
     * @param newPlayer The new player to be added.
     */
    public void addPlayer(Player newPlayer) {
        listOfPlayers.add(newPlayer);
        ArrayList<String> nicknames = new ArrayList<>();
        for (Player player : listOfPlayers) {
            nicknames.add(player.getUsername());
        }
        gameServerInstance.sendUpdateToAll(new PlayersListMessage(-10, nicknames));
    }

    /**
     * Advances the game to the next turn, updating the current player and handling end-of-game logic.
     */
    public void nextTurn() {
        currentPlayerhasPlayed = false;
        gameServerInstance.sendUpdateToAll(new CurrentPlayerHasPlayedMessage(currentPlayer.getClientID(), false));

        if (this.state == GameState.ENDING)
            lastRoundsplayed++;

        if (lastRoundsplayed == listOfPlayers.size() + 1)
            finalScoreCheck();
        else {
            int newIndex = (listOfPlayers.indexOf(currentPlayer) + 1) % listOfPlayers.size();
            this.setCurrentPlayer(listOfPlayers.get(newIndex));

            if (!currentPlayer.canPlay())
                nextTurn();
        }
    }

    /**
     * Updates the score of a player and notifies all clients.
     *
     * @param score The score to be added.
     * @param player The player whose score is to be updated.
     */
    public void updatePoints(int score, Player player) {
        scores.merge(player, score, Integer::sum);
        Map<String, Integer> playerScoresMap = new HashMap<>();
        for (Map.Entry<Player, Integer> entry : scores.entrySet()) {
            playerScoresMap.put(entry.getKey().getUsername(), entry.getValue());
        }
        gameServerInstance.sendUpdateToAll(new ScoreMessage(-10, playerScoresMap));

        if (scores.get(player) >= 20) {
            potentialWinner = currentPlayer;
        }
    }

    /**
     * Draws a displayed playable card for a player, handling card replacement and notifying clients.
     *
     * @param card The card to be drawn.
     * @param player The player drawing the card.
     * @return True if the card is successfully drawn, otherwise false.
     */
    public boolean drawDisplayedPlayableCard(PlayableCard card, Player player) {
        if (!displayedPlayableCard.contains(card)) {
            gameServerInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), "Card is not within the displayed playable cards"));
            return false;
        }
        player.pick(card);
        displayedPlayableCard.remove(card);

        if (card instanceof GoldCard) {
            try {
                displayedPlayableCard.add((PlayableCard) goldDeck.draw());
            } catch (DeckEmptyException e) {
                gameServerInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), e.getMessage()));
                return false;
            }
            gameServerInstance.sendUpdateToAll(new GoldDeckMessage(-10, goldDeck));
        } else if (card instanceof ResourceCard) {
            try {
                displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
            } catch (DeckEmptyException e) {
                gameServerInstance.sendUpdateToAll(new ExceptionMessage(player.getClientID(), e.getMessage()));
                return false;
            }
            gameServerInstance.sendUpdateToAll(new ResourceDeckMessage(-10, resourceDeck));
        }
        gameServerInstance.sendUpdateToAll(new DisplayedPlayableCardsMessage(-10, displayedPlayableCard));
        return true;
    }

    /**
     * Determines the winner of the game based on player statistics.
     *
     * @param playerStats A map containing player statistics, where the key is the player and the value is another map with the player's stats.
     * @return The player who has won the game, or null if there is a tie.
     */
    private Player determineWinner(Map<Player, Map<String, Integer>> playerStats) {
        Player winner = null;
        int maxPoints = -1;
        int maxObjectives = -1;

        // Iterate through each player's stats to determine the winner
        for (Map.Entry<Player, Map<String, Integer>> entry : playerStats.entrySet()) {
            Player player = entry.getKey();
            int points = entry.getValue().get("points");
            int objectives = entry.getValue().get("objectives");

            // Check if the current player has more points than the current max
            if (points > maxPoints) {
                winner = player;
                maxPoints = points;
                maxObjectives = objectives;
            } else if (points == maxPoints) {
                // If points are the same, check the number of objectives
                if (objectives > maxObjectives) {
                    winner = player;
                    maxObjectives = objectives;
                } else if (objectives == maxObjectives) {
                    // If both points and objectives are the same, there is no winner (tie)
                    winner = null;
                }
            }
        }
        return winner;
    }


    /**
     * Checks the final scores and determines the winner.
     */
    public void finalScoreCheck() {
        Map<Player, Map<String, Integer>> playerStats = new HashMap<>();
        //check for additional objective points, from both common and secret objective cards
        for (Player p: this.listOfPlayers){
            int pointsToAdd = 0;
            int objectivesCompleted = 0;
            for (ObjectiveCard card: this.getDisplayedObjectiveCard()){ //common objective cards
                int pointsGained = card.verifyObjective(p.getGround());
                if (pointsGained > 0)
                    objectivesCompleted++;
                pointsToAdd += pointsGained;
            }

            int pointsGained = p.getHandObjective().getFirst().verifyObjective(p.getGround()); //secret objective card
            if (pointsGained > 0)
                objectivesCompleted++;
            pointsToAdd += pointsGained;
            updatePoints(pointsToAdd, p); //points update
            Map<String, Integer> stats = new HashMap<>();
            stats.put("points", scores.get(p));
            stats.put("objectives", objectivesCompleted);
            playerStats.put(p, stats);
        }

        winner = determineWinner(playerStats);
        if (winner == null) {
            gameServerInstance.sendUpdateToAll(new WinnerMessage(-10, "tie"));
        } else {
            gameServerInstance.sendUpdateToAll(new WinnerMessage(-10, winner.getUsername()));
        }
        gameServerInstance.setInGame(false);
        setState(GameState.ENDED);
    }

    /**
     * @return The potential winner of the game.
     */
    public Player getPotentialWinner() {
        return this.potentialWinner;
    }

    /**
     * @return The game controller.
     */
    public GameController getController() {
        return this.controller;
    }

    @Override
    public String toString() {
        return this.getIdGame() + this.getListOfPlayers().toString();
    }

    /**
     * Handles client disconnection and ends the game.
     */
    public void clientDisconnected() {
        this.endGame();
    }

    /**
     * @return The game server instance.
     */
    public GameServerInstance getGameServerInstance() {
        return gameServerInstance;
    }

    /**
     * Retrieves a player by their nickname.
     *
     * @param nickname The nickname of the player.
     * @return The player with the specified nickname.
     */
    public Player getPlayer(String nickname) {
        Player toReturn = null;
        for (Player p : this.listOfPlayers)
            if (Objects.equals(p.getUsername(), nickname))
                toReturn = p;
        return toReturn;
    }

    /**
     * @return The current state of the game.
     */
    public GameState getState() {
        return state;
    }

    /**
     * Sets the current state of the game.
     *
     * @param state The new state of the game.
     */
    public void setState(GameState state) {
        this.state = state;
        gameServerInstance.sendUpdateToAll(new GameStateMessage(-10, state));
    }

    /**
     * @return The starter deck.
     */
    public Deck getStarterDeck() {
        return starterDeck;
    }

    /**
     * @return The ID of the game.
     */
    public int getIdGame() {
        return idGame;
    }

    /**
     * @return The list of players in the game.
     */
    public List<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    /**
     * @return The number of players in the game.
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * @return The scores of the players.
     */
    public Map<Player, Integer> getScores() {
        return scores;
    }

    /**
     * @return The first player of the game.
     */
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * @return The resource deck.
     */
    public Deck getResourceDeck() {
        return resourceDeck;
    }

    /**
     * @return The gold deck.
     */
    public Deck getGoldDeck() {
        return goldDeck;
    }

    /**
     * @return The objective deck.
     */
    public Deck getObjectiveDeck() {
        return objectiveDeck;
    }

    /**
     * @return The displayed playable cards.
     */
    public ArrayList<PlayableCard> getDisplayedPlayableCard() {
        return (ArrayList<PlayableCard>) displayedPlayableCard;
    }

    /**
     * @return The displayed objective cards.
     */
    public ArrayList<ObjectiveCard> getDisplayedObjectiveCard() {
        return (ArrayList<ObjectiveCard>) displayedObjectiveCard;
    }

    /**
     * @return The current player of the game.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the first player of the game.
     *
     * @param firstPlayer The first player.
     */
    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    /**
     * Sets the current player of the game and notifies all clients.
     *
     * @param currentPlayer The current player.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        gameServerInstance.sendUpdateToAll(new CurrentPlayerMessage(-10, currentPlayer.getUsername()));
    }

    /**
     * @return The available tokens for players to choose.
     */
    public List<PlayerColor> getTokenAvailable() {
        return tokenAvailable;
    }

    /**
     * Sets the number of rounds played after the game enters the ending state.
     *
     * @param lastRoundsplayed The number of rounds played.
     */
    public void setLastRoundsplayed(int lastRoundsplayed) {
        this.lastRoundsplayed = lastRoundsplayed;
    }

    /**
     * @return The winner of the game.
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Sets the game controller.
     *
     * @param controller The game controller.
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }
}