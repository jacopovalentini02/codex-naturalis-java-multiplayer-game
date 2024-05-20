package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;


public class Game {
    private int idGame;
    private GameState state;
    private final GameController controller;
    private List<Player> listOfPlayers;

    public List<PlayerColor> getTokenAvailable() {
        return tokenAvailable;
    }

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

    private Player winner; //todo settarlo prima o poi

    private final GameManager gameManager;

    int lastRoundsplayed;

    int colorChosen;

    private final GameServerInstance gameServerInstance;

    int objectiveCardsChosen;


    public int getObjectiveCardsChosen() {
        return objectiveCardsChosen;
    }

    public void setObjectiveCardsChosen(int objectiveCardsChosen) {
        this.objectiveCardsChosen = objectiveCardsChosen;
    }

    public boolean getifCurrentPlayerhasPlayed() {return currentPlayerhasPlayed;}
    public void setCurrentPlayerhasPlayed(boolean bool){
        this.currentPlayerhasPlayed = bool;
    }


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

    public synchronized void setupField() {
        //invoking the instantiation of all game's cards
        setUpCards();
        //Shuffle the Resource cards and place them facedown in the center of the table. Draw 2 cards and place them faceup.
        resourceDeck.shuffle();
        try{
            displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
            displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
        }catch (DeckEmptyException ignore){}


        //Shuffle the Gold cards and place them facedown in the center of the table. Draw 2 cards and place them faceup.
        goldDeck.shuffle();
        try{
            displayedPlayableCard.add((PlayableCard) goldDeck.draw());
            displayedPlayableCard.add((PlayableCard) goldDeck.draw());
        }catch (DeckEmptyException ignore){}

        //Each player randomly takes one Starter card and choose the face to be played
        starterDeck.shuffle();

        //Send new: starterCard, goldDeck, resourceDeck, displayedPlayableCards, currentPlayer
        this.setState(GameState.CHOOSING_STARTER_CARDS);

        for(Player p: listOfPlayers){
            StarterCard starter = null;
            try{
                starter = (StarterCard) starterDeck.draw();
            }catch (DeckEmptyException ignore){}
            p.addToHand(starter);
        }
        ArrayList<Coordinate> initialCoordinates = new ArrayList<Coordinate>(); //sending initial available positions to players
        initialCoordinates.add(new Coordinate(0,0));
        gameServerInstance.sendUpdateToAll(new CoordinatesAvailableMessage(-10, initialCoordinates));

        gameServerInstance.sendUpdateToAll(new GoldDeckMessage(-10, this.goldDeck));
        gameServerInstance.sendUpdateToAll(new ResourceDeckMessage(-10, this.resourceDeck));
        gameServerInstance.sendUpdateToAll(new DisplayedPlayableCardsMessage(-10, this.displayedPlayableCard));
        gameServerInstance.sendUpdateToAll(new CurrentPlayerMessage(-10, currentPlayer.getUsername()));
    }

    public boolean chooseColor(Player player, PlayerColor color)  {
        if(!tokenAvailable.contains(color)) {
            gameServerInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(), "This color is already taken"));
            return false;
        }
        player.setToken(color);

        tokenAvailable.remove(color);

        colorChosen++;

        if (colorChosen == this.numOfPlayers)
            setupHandsAndObjectives();
        return true;

    }

    public boolean chooseObjectiveCard(Player player, Card card) {


        if (!player.getHandObjective().contains((ObjectiveCard) card)) {
            gameServerInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(), "You can't choose this card"));
            return false;
        }

        int indexToRemove = player.getHandObjective().getFirst().equals(card) ? 1 : 0;

        ObjectiveCard cardToPutBack = player.getHandObjective().remove(indexToRemove);

        gameServerInstance.sendUpdateToAll(new HandObjectiveMessage(player.getClientID(), player.getHandObjective()));

        this.objectiveDeck.addCard(cardToPutBack);

        objectiveCardsChosen++;


        if(objectiveCardsChosen==listOfPlayers.size()){
            randomizeFirstPlayer();
        }
        return true;
    }


    public synchronized void setupHandsAndObjectives() {
        for(Player p : listOfPlayers) {
            scores.put(p, 0);
            //draw 2 resourceCard e 1 goldCard
            try{
                ArrayList<PlayableCard> newHand = new ArrayList<PlayableCard>();
                newHand.add((PlayableCard) resourceDeck.draw());
                newHand.add((PlayableCard) resourceDeck.draw());
                newHand.add((PlayableCard) goldDeck.draw());
                p.addToHand(newHand);
            }catch (DeckEmptyException ignore){}
        }
        //shuffling the objectiveDeck
        objectiveDeck.shuffle();
        //placing the 2 common objective on the table
        try{
            displayedObjectiveCard.add((ObjectiveCard) objectiveDeck.draw());
            displayedObjectiveCard.add((ObjectiveCard) objectiveDeck.draw());
        }catch (DeckEmptyException ignore){}

        gameServerInstance.sendUpdateToAll(new DisplayedObjectiveMessage(-10, displayedObjectiveCard));
        this.setState(GameState.CHOOSING_OBJECTIVES);
        for(Player p : listOfPlayers){
            try{
               ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
               objectiveCards.add((ObjectiveCard)objectiveDeck.draw());
               objectiveCards.add((ObjectiveCard)objectiveDeck.draw());
               p.addToHandObjective(objectiveCards);
            }catch (DeckEmptyException ignore){}
        }

        gameServerInstance.sendUpdateToAll(new GoldDeckMessage(-10, this.goldDeck));
        gameServerInstance.sendUpdateToAll(new ResourceDeckMessage(-10, this.resourceDeck));

    }

    public void randomizeFirstPlayer(){
        //choosing randomly the first player
        Random rand = new Random();
        int index = rand.nextInt(listOfPlayers.size());
        setFirstPlayer(listOfPlayers.get(index));
        this.setState(GameState.STARTED);
        setCurrentPlayer(getFirstPlayer());

    }


    public void setUpCards(){


        try {
            // path to JSON file
            String filePath = "src/main/java/it/polimi/ingsfw/ingsfwproject/Model/cards.json";

            // reading of JSON file
            FileReader reader = new FileReader(filePath);

            // Parsing of JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            // Obtaining array of cards from JSON
            JSONArray cardsArray = jsonObject.getJSONArray("cards");

            // Cycling on every object of the JSONArray
            for (int i = 0; i < cardsArray.length(); i++) {
                JSONObject cardObject = cardsArray.getJSONObject(i);

                // Extracting cards data
                int id = cardObject.getInt("id");

                //Resource card 0-40
                if(i<=39){
                    resourceDeck.createResourceCard(cardObject, id);
                }else if(i<=79){
                    goldDeck.createGoldCard(cardObject, id);
                }else if(i<=85){
                    starterDeck.createStarterCard(cardObject, id);
                }else if(i<=93){
                    objectiveDeck.createStructObjective(cardObject, id);
                }else{
                    objectiveDeck.createNotStructObjective(cardObject, id);
                }
            }

            // close reader
            reader.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void endGame(){
        gameManager.deleteGame(this.idGame);
    }

    public void addPlayer(Player newPlayer){
        listOfPlayers.add(newPlayer);

        ArrayList<String> nicknames = new ArrayList<>();
        for (Player player : listOfPlayers) {
            nicknames.add(player.getUsername());
        }
        gameServerInstance.sendUpdateToAll(new PlayersListMessage(-10, nicknames));

    }

    public void nextTurn() {

        if (this.state == GameState.ENDING) //counting the number of rounds played after a player reaches 20 points
            lastRoundsplayed++;

        if (lastRoundsplayed == listOfPlayers.size() + 1) // if everybody has made its last turn, end the game
            finalScoreCheck();

        int newIndex = (listOfPlayers.indexOf(currentPlayer) + 1) % listOfPlayers.size();
        this.setCurrentPlayer(listOfPlayers.get(newIndex));


        currentPlayerhasPlayed = false;

    }

    public void updatePoints(int score, Player player){

        scores.merge(player, score, Integer::sum); //sum the old score with the new score

        Map<String, Integer> playerScoresMap = new HashMap<>(); //creating a new map to send clients
        for (Map.Entry<Player, Integer> entry: scores.entrySet()){
            playerScoresMap.put(entry.getKey().getUsername(), entry.getValue());
        }

       gameServerInstance.sendUpdateToAll(new ScoreMessage(-10, playerScoresMap));

        if (scores.get(player) >= 20) {
            potentialWinner=currentPlayer;
        }
    }

    public boolean drawDisplayedPlayableCard(PlayableCard card, Player player)  {

        if (!(displayedPlayableCard.contains(card))){
            gameServerInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),"Card is not within the displayed playable cards"));
            return false;
        }

        player.pick(card);

        displayedPlayableCard.remove(card);

        if (card instanceof GoldCard) {
            try {
                displayedPlayableCard.add((PlayableCard) goldDeck.draw());
            } catch (DeckEmptyException e) {
                gameServerInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),e.getMessage()));
                return false;
            }
            gameServerInstance.sendUpdateToAll(new GoldDeckMessage(-10, goldDeck));
        } else if (card instanceof ResourceCard) {
            try {
                displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
            } catch (DeckEmptyException e) {
                gameServerInstance.sendUpdateToAll(new ExcpetionMessage(player.getClientID(),e.getMessage()));
                return false;
            }
            gameServerInstance.sendUpdateToAll(new ResourceDeckMessage(-10, resourceDeck));
        }


        gameServerInstance.sendUpdateToAll(new DisplayedPlayableCardsMessage(-10, displayedPlayableCard));
        return true;
    }

    public void finalScoreCheck(){

        //check for additional objective points, from both common and secret objective cards
        for (Player p: this.listOfPlayers){
            int pointsToAdd = 0;

            for (ObjectiveCard card: this.getDisplayedObjectiveCard()){ //common objective cards
                pointsToAdd += card.verifyObjective(p.getGround());
            }

            pointsToAdd += p.getHandObjective().getFirst().verifyObjective(p.getGround()); //secret objective card

            updatePoints(pointsToAdd, p); //points update
        }
        //l'update dei points è gia mandato da updatePoints
        winner = Collections.max(scores.entrySet(), Map.Entry.comparingByValue()).getKey();
        gameServerInstance.sendUpdateToAll(new WinnerMessage(-10, winner.getUsername()));
        setState(GameState.ENDED);


        this.endGame();
    }

    public Player getPotentialWinner(){
        return this.potentialWinner;
    }

    public GameController getController(){
        return this.controller;
    }

    @Override
    public String toString(){return this.getIdGame() + this.getListOfPlayers().toString();}



    public void clientDisconnected() {
        //TODO MESSAGGIO


//        for (ClientCallbackInterface c: this.listeners.values()){
//            try {
//                c.update("Client disconnected. Game ended.");
//                c.updateState(GameState.ENDED);
//            } catch (RemoteException ignored) {}
//        }
        this.endGame();
    }

    public GameServerInstance getGameServerInstance() {
        return gameServerInstance;
    }

    public Player getPlayer(String nickname){
        Player toReturn = null;
        for (Player p: this.listOfPlayers)
            if (Objects.equals(p.getUsername(), nickname))
                toReturn = p;
        return toReturn;
    }


    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
        gameServerInstance.sendUpdateToAll(new GameStateMessage(-10, state));
    }

    public Deck getStarterDeck() {
        return starterDeck;
    }

    public void setStarterDeck(Deck starterDeck) {
        this.starterDeck = starterDeck;
    }

    public int getIdGame() {
        return idGame;
    }

    public List<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public Map<Player, Integer> getScores() {
        return scores;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Deck getResourceDeck() {
        return resourceDeck;
    }

    public Deck getGoldDeck() {
        return goldDeck;
    }

    public Deck getObjectiveDeck() {
        return objectiveDeck;
    }

    public ArrayList<PlayableCard> getDisplayedPlayableCard() {
        return (ArrayList<PlayableCard>) displayedPlayableCard;
    }

    public ArrayList<ObjectiveCard> getDisplayedObjectiveCard() {
        return (ArrayList<ObjectiveCard>) displayedObjectiveCard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public void setListOfPlayers(List<Player> listOfPlayers) {
        this.listOfPlayers = listOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void setScores(Map<Player, Integer> scores) {
        this.scores = scores;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void setResourceDeck(Deck resourceDeck) {
        this.resourceDeck = resourceDeck;
    }

    public void setGoldDeck(Deck goldDeck) {
        this.goldDeck = goldDeck;
    }

    public void setObjectiveDeck(Deck objectiveDeck) {
        this.objectiveDeck = objectiveDeck;
    }

    public void setDisplayedPlayableCard(ArrayList<PlayableCard> displayedPlayableCard) {
        this.displayedPlayableCard = displayedPlayableCard;
    }

    public void setDisplayedObjectiveCard(ArrayList<ObjectiveCard> displayedObjectiveCard) {
        this.displayedObjectiveCard = displayedObjectiveCard;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        gameServerInstance.sendUpdateToAll(new CurrentPlayerMessage(-10, currentPlayer.getUsername()));
    }

    public int getLastRoundsplayed() {
        return lastRoundsplayed;
    }


}

