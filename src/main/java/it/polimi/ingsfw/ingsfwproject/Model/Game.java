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
    private GameController controller;
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
    private List<PlayableCard> displayedPlayableCard;
    private List<ObjectiveCard> displayedObjectiveCard;
    private Player currentPlayer;
    private Player potentialWinner;

    private boolean currentPlayerhasPlayed;

    private Player winner;

    private GameManager gameManager;

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
        //todo mando lista player
        //todo mando anche player
        controller = new GameController(this);
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

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
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
    }

    public int getLastRoundsplayed() {
        return lastRoundsplayed;
    }

    public synchronized void setupField() throws DeckEmptyException {
        //invoking the instantiation of all game's cards
        setUpCards();
        //Shuffle the Resource cards and place them facedown in the center of the table. Draw 2 cards and place them faceup.
        resourceDeck.shuffle();
        displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
        displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
        //Shuffle the Gold cards and place them facedown in the center of the table. Draw 2 cards and place them faceup.
        goldDeck.shuffle();
        displayedPlayableCard.add((PlayableCard) goldDeck.draw());
        displayedPlayableCard.add((PlayableCard) goldDeck.draw());
        //Each player randomly takes one Starter card and choose the face to be played
        starterDeck.shuffle();

        //Send new: starterCard, goldDeck, resourceDeck, displayedPlayableCards, currentPlayer
        this.setState(GameState.CHOOSING_STARTER_CARDS);
        gameServerInstance.sendGameStateUpdate(this.state);
        for(Player p: listOfPlayers){
            int clientID=gameServerInstance.getClientID(p);
            StarterCard starter = (StarterCard) starterDeck.draw();
            p.getHandCard().add(starter); //Add starter card to player's hand
            gameServerInstance.sendStarterCardMessage(clientID, starter);
            gameServerInstance.sendAvaiblePositionUpdate(clientID,  p.getGround().getAvailablePositions());

        }
        gameServerInstance.sendGoldDeckUpdate(this.goldDeck);
        gameServerInstance.sendResourceDeckUpdate(this.resourceDeck);
        gameServerInstance.sendDisplayedPlayableCardUpdate(this.displayedPlayableCard);
        gameServerInstance.sendCurrentPlayerUpdate(this.currentPlayer);

    }

    public void chooseColor(Player player, PlayerColor color) throws ColorNotAvailableException, DeckEmptyException {
        //todo: la view deve chiedere la lista aggiornata - come gestisco le excpetion? - mando messaggio
        if(!tokenAvailable.contains(color))
            throw new ColorNotAvailableException("This color is already taken");

        player.setToken(color);

        tokenAvailable.remove(color);

        colorChosen++;

        gameServerInstance.sendColorChosen(gameServerInstance.getClientID(player), color);

        if (colorChosen == this.numOfPlayers)
            setupHandsAndObjectives();

    }

    public void chooseObjectiveCard(Player player, Card card) throws CardNotPresentException {

        if (!player.getHandObjective().contains((ObjectiveCard) card))
            throw new CardNotPresentException("You can't choose this card");

        int indexToRemove = player.getHandObjective().get(0).equals(card) ? 1 : 0;

        ObjectiveCard cardToPutBack = player.getHandObjective().remove(indexToRemove);

        gameServerInstance.sendHandObjectiveUpdate(gameServerInstance.getClientID(player), player.getHandObjective());

        this.objectiveDeck.addCard(cardToPutBack);

        objectiveCardsChosen++;
        if(objectiveCardsChosen==listOfPlayers.size()){
            randomizeFirstPlayer();
        }
    }


    public synchronized void setupHandsAndObjectives() throws DeckEmptyException {
        for(Player p : listOfPlayers) {
            scores.put(p, 0);
            //draw 2 resourceCard e 1 goldCard
            p.getHandCard().add((PlayableCard) resourceDeck.draw());
            p.getHandCard().add((PlayableCard) resourceDeck.draw());
            p.getHandCard().add((PlayableCard) goldDeck.draw());

            gameServerInstance.sendHandCardsUpdate(gameServerInstance.getClientID(p), p.getHandCard());

        }
        //shuffling the objectiveDeck
        objectiveDeck.shuffle();
        //placing the 2 common objective on the table
        displayedObjectiveCard.add((ObjectiveCard) objectiveDeck.draw());
        displayedObjectiveCard.add((ObjectiveCard) objectiveDeck.draw());


        for(Player p : listOfPlayers){
            p.getHandObjective().add((ObjectiveCard) objectiveDeck.draw());
            p.getHandObjective().add((ObjectiveCard) objectiveDeck.draw());
            gameServerInstance.sendHandObjectiveUpdate(gameServerInstance.getClientID(p), p.getHandObjective());
        }
        this.setState(GameState.CHOOSING_OBJECTIVES);

        gameServerInstance.sendGameStateUpdate(this.state);
        gameServerInstance.sendDisplayedObjectiveCardUpdate(displayedObjectiveCard);

    }

    public void randomizeFirstPlayer(){
        //choosing randomly the first player
        Random rand = new Random();
        int index = rand.nextInt(listOfPlayers.size());
        setFirstPlayer(listOfPlayers.get(index));
        setCurrentPlayer(getFirstPlayer());
        this.setState(GameState.STARTED);

        gameServerInstance.sendGameStateUpdate(this.state);
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
        gameServerInstance.sendPlayersListUpdate(nicknames);

    }

    public void nextTurn() {

        if (this.state == GameState.ENDING) //counting the number of rounds played after a player reaches 20 points
            lastRoundsplayed++;

        if (lastRoundsplayed == listOfPlayers.size() + 1) // if everybody has made its last turn, end the game
            finalScoreCheck();

        int newIndex = (listOfPlayers.indexOf(currentPlayer) + 1) % listOfPlayers.size();
        this.setCurrentPlayer(listOfPlayers.get(newIndex));

        gameServerInstance.sendCurrentPlayerUpdate(this.currentPlayer);

        currentPlayerhasPlayed = false; //todo non va mandato alla view?

    }

    public void updatePoints(int score, Player player){

        scores.merge(player, score, Integer::sum); //sum the old score with the new score

        Map<String, Integer> playerScoresMap = new HashMap<>(); //creating a new map to send clients
        for (Map.Entry<Player, Integer> entry: scores.entrySet()){
            playerScoresMap.put(entry.getKey().getUsername(), entry.getValue());
        }

        gameServerInstance.sendScoreUpdate(playerScoresMap);

        if (scores.get(player) >= 20) {
            potentialWinner=currentPlayer;
        }
    }

    public void drawDisplayedPlayableCard(PlayableCard card, Player player) throws CardNotPresentException, DeckEmptyException {

        if (!(displayedPlayableCard.contains(card)))
            throw new CardNotPresentException("Card is not within the displayed playable cards");

        player.pick(card);

        displayedPlayableCard.remove(card);

        if (card instanceof GoldCard) {
            displayedPlayableCard.add((PlayableCard) goldDeck.draw());
        } else if (card instanceof ResourceCard) {
            displayedPlayableCard.add((PlayableCard) resourceDeck.draw());
        }

        gameServerInstance.sendDisplayedPlayableCardUpdate(this.displayedPlayableCard);

    }

    public void finalScoreCheck(){

        //check for additional objective points, from both common and secret objective cards
        for (Player p: this.listOfPlayers){
            int pointsToAdd = 0;

            for (ObjectiveCard card: this.getDisplayedObjectiveCard()){ //common objective cards
                pointsToAdd += card.verifyObjective(p.getGround());
            }

            pointsToAdd += p.getHandObjective().get(0).verifyObjective(p.getGround()); //secret objective card

            updatePoints(pointsToAdd, p); //points update
        }
        //l'update dei points è gia mandato da updatePoints
        winner = Collections.max(scores.entrySet(), Map.Entry.comparingByValue()).getKey();
        gameServerInstance.sendWinner(winner.getUsername());
        setState(GameState.ENDED);
        gameServerInstance.sendGameStateUpdate(this.state);

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


}

