package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.CardNotInHandException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;
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

    private Player winner;

    private GameManager gameManager;


    public Game(GameManager gameManager, int idGame, int numOfPlayers, Player player1) {
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
        controller = new GameController(this);
        displayedPlayableCard = new ArrayList<PlayableCard>();
        displayedObjectiveCard = new ArrayList<>();
        scores = new HashMap<>();
        tokenAvailable = new ArrayList<>();
        tokenAvailable.add(PlayerColor.GREEN);
        tokenAvailable.add(PlayerColor.RED);
        tokenAvailable.add(PlayerColor.BLUE);
        tokenAvailable.add(PlayerColor.YELLOW);
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
        //cycling on every player the beginning operations
        for (Player p : listOfPlayers) {
            StarterCard starter = (StarterCard) starterDeck.draw();
            p.getHandCard().add(starter);
        }
    }

    public synchronized void setupHandsAndObjectives() throws DeckEmptyException {
        for(Player p : listOfPlayers) {
            scores.put(p, 0);
            p.getHandCard().add((PlayableCard) resourceDeck.draw());
            p.getHandCard().add((PlayableCard) resourceDeck.draw());
            p.getHandCard().add((PlayableCard) goldDeck.draw());
        }
        //shuffling the objectiveDeck
        objectiveDeck.shuffle();
        //placing the 2 common objective on the table
        displayedObjectiveCard.add((ObjectiveCard) objectiveDeck.draw());
        displayedObjectiveCard.add((ObjectiveCard) objectiveDeck.draw());
        for(Player p : listOfPlayers){
            p.getHandObjective().add((ObjectiveCard) objectiveDeck.draw());
            p.getHandObjective().add((ObjectiveCard) objectiveDeck.draw());
        }
    }

public void randomizeFirstPlayer(){
        //choosing randomly the first player
        Random rand = new Random();
        int index = rand.nextInt(listOfPlayers.size());
        setFirstPlayer(listOfPlayers.get(index));
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

            //Shuffle methods that i'll insert in the main function 'setupGame'
           /* goldDeck.shuffle();
            starterDeck.shuffle();
            objectiveDeck.shuffle();
            resourceDeck.shuffle();

            //check method to see if we did correct
            goldDeck.printCardsDeck();*/

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void endGame(){
        gameManager.deleteGame(this.idGame);
    }

    public void addPlayer(Player newPlayer){
        listOfPlayers.add(newPlayer);
        if(listOfPlayers.size()==this.numOfPlayers){
            setState(GameState.STARTED);
            setCurrentPlayer(getListOfPlayers().get(0));
            try {
                this.setupField();
            } catch (DeckEmptyException e) {
                e.printStackTrace();
            } /*catch( PositionNotAvailableException | NotEnoughResourcesException | CardNotInHandException e){
                /*
                TODO: CATCH DA ELIMINARE: QUESTE TRE ECCEZIONI LE CATCHERà GIA IL CONTROLLER, IL PROBLEMA
                è CHE SETUPGAME CHIAMA PLAYCARD MA NON DOVREBBE ESSERE LUI, BENSì IL CONTROLLER!


                e.printStackTrace();
            }*/
        }

    }

    public void nextTurn(){
        int newIndex = (listOfPlayers.indexOf(currentPlayer) + 1) % listOfPlayers.size();
        this.setCurrentPlayer(listOfPlayers.get(newIndex));

        if(currentPlayer==potentialWinner){
            this.finalScoreCheck();
        }

    }

    public void lastTurn(Player firstTwenty) throws PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException {
        int flag=0;
        int turnsPassed =0;
        boolean firstTwentyFirstTurn = true; // Aggiunta una variabile per tenere traccia del primo turno di firstTwenty
        while(flag<2){
            //turn operations
            if(flag!=0) {
                if (getCurrentPlayer() != firstTwenty || !firstTwentyFirstTurn) {
                    Deck lastHand = new Deck();
                    //letting the player choose the last card to play
                    System.out.println("Scegli la tua ultima carta da giocare (indicare id): ");
                    for (int i = 0; i < getCurrentPlayer().getHandCard().size(); i++) {
                        lastHand.getCardList().add(getCurrentPlayer().getHandCard().get(i));
                    }
                    lastHand.printCardsDeck();
                    //TODO: Mettere poi la logica di scelta della carta, qua ho messo index 1
                /*Scanner scanner = new Scanner(System.in);
                int index = scanner.nextInt();
                scanner.close();
                PlayableCard rightCard = null;
                for (Card o : currentPlayer.getHandCard()) {
                    if (o.getIdCard() == index) {
                        getCurrentPlayer().getHandCard().remove(o);
                        rightCard = (PlayableCard) o;
                        break;
                    }
                }*/
                    PlayableCard rightCard = getCurrentPlayer().getHandCard().get(1);
                    getCurrentPlayer().getHandCard().remove(1);
                    //letting the player choose the position on the grid
                    System.out.println("Scegli la posizione della carta tra queste (indicare prima x e poi y): \n");
                    for (Coordinate c : getCurrentPlayer().getGround().getAvailablePositions()) {
                        System.out.println("(" + c.getX() + " , " + c.getY() + ")\n");
                    }
                    //TODO: anche qui ho rimpiazzato la scelta delle coordinate su cui giocare con una scelta arbitraria per fare i test
                /*int x = scanner.nextInt();
                int y = scanner.nextInt();
                scanner.close();*/
                    Coordinate coordinateTest = getCurrentPlayer().getGround().getAvailablePositions().get(1);
                    //TODO: questo dovrebbe farlo il controller ma per ora lasceremo che sia player a farlo
                    //invoking the playCard method
                    getCurrentPlayer().playCard(rightCard, true, coordinateTest);
                    //TODO : QUA ANDREBBE POI FATTO UN UPDATE DEL PUNTEGGIO DEL GIOCATORE
                }

                if(firstTwentyFirstTurn){
                    firstTwentyFirstTurn=false;
                }
            }
            //every time we meet the first player to reach x>=20 points, we keep it in mind
            if(getCurrentPlayer()==firstTwenty){
                flag++;
            }
            //if flag==2 it means the last turn is completed for every player, so we don't have to go for a nextTurn()
            if(flag!=2){
                turnsPassed++;
                nextTurn();
            }
        }
        System.out.println(turnsPassed);
        //now that all have completed their last turn, the game is set in ending state
        setState(GameState.ENDING);
        //TODO: Rimetterla in funzione post-implementazione
        //invoking the final function to check who is the winner
        //finalScoreCheck();
    }

    public void updatePoints(int score, Player player){

        scores.merge(player, score, Integer::sum); //sum the old score with the new score

        if (scores.get(player) >= 20) {
            potentialWinner=currentPlayer;
            this.state=GameState.ENDING;
        }
    }

    public void finalScoreCheck(){
        //OPERAZIONI...

        //check for additional objective points, from both common and secret objective cards
        for (Player p: this.listOfPlayers){
            int pointsToAdd = 0;

            for (ObjectiveCard card: this.getDisplayedObjectiveCard()){ //common objective cards
                pointsToAdd += card.verifyObjective(p.getGround());
            }

            pointsToAdd += p.getHandObjective().get(0).verifyObjective(p.getGround()); //secret objective card

            updatePoints(pointsToAdd, p); //points update
        }

        winner = Collections.max(scores.entrySet(), Map.Entry.comparingByValue()).getKey();
        //TODO: notify clients with a listener
        setState(GameState.ENDED);

        this.endGame();
    }
}