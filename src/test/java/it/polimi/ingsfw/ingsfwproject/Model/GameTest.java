package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;

import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static javax.management.Query.times;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private GameServerInstance gameServerInstance;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Game game;

    @BeforeEach
    void setUp() {
        gameServerInstance=new GameServerInstance();
        player1 = new Player("user1", gameServerInstance, 0);
        game = new Game(gameServerInstance,new GameManager(), 1, 4, player1);
        player2 = new Player("user2", gameServerInstance, 1);
        player3 = new Player("user3", gameServerInstance,2);
        player4 = new Player("user4", gameServerInstance,3);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);

        game.setUpCards();
    }

    @Test
    //Check the size of each deck
    public void testSetUpCards() {
        assertEquals(40, game.getResourceDeck().getCardList().size());
        assertEquals(40, game.getGoldDeck().getCardList().size());
        assertEquals(6, game.getStarterDeck().getCardList().size());
        assertEquals(16, game.getObjectiveDeck().getCardList().size());

    }

    @Test
    //Check resource cards' deck attributes are set up correctly
    public void testSetUpResourceDeck() throws RemoteException {
        ResourceCard addedCard = (ResourceCard) game.getResourceDeck().getCardList().getFirst();

        assertEquals(1,  addedCard.getFront().getIdCard());
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getBackface().getCenter());
        assertEquals(0, addedCard.getFront().getPoints());
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCornerList()[0]);
        assertEquals(Content.EMPTY,addedCard.getFront().getCornerList()[1]);
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCornerList()[2]);
        assertEquals(Content.HIDDEN, addedCard.getFront().getCornerList()[3]);

    }
    @Test
    //Check gold cards' deck attributes are set up correctly
    public void testSetUpGoldDeck()  {
        GoldCard addedCard=  (GoldCard) game.getGoldDeck().getCardList().getFirst();

        assertEquals(41,  addedCard.getFront().getIdCard());
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getBackface().getCenter());
        assertEquals(1, addedCard.getFront().getPoints());
        assertEquals(3, addedCard.getFront().getCost().size()); //Check the right length
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCost().get(0));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCost().get(1));
        assertEquals(Content.ANIMAL_KINGDOM, addedCard.getFront().getCost().get(2));
        assertEquals(Content.QUILL, addedCard.getFront().getObjectNeeded());
        assertFalse(addedCard.getFront().isOverlapped());
        assertEquals(Content.HIDDEN, addedCard.getFront().getCornerList()[0]);
        assertEquals(Content.EMPTY, addedCard.getFront().getCornerList()[1]);
        assertEquals(Content.EMPTY, addedCard.getFront().getCornerList()[2]);
        assertEquals(Content.QUILL, addedCard.getFront().getCornerList()[3]);

    }

    @Test
    //Check starter's deck attributes are set up correctly
    public void testSetUpStarterDeck()   {
        StarterCard addedCard=  (StarterCard) game.getStarterDeck().getCardList().getFirst();
        assertEquals(81,  addedCard.getFront().getIdCard());
        assertEquals(1, addedCard.getFront().getCenter().size());
        assertEquals(Content.INSECT_KINGDOM, addedCard.getFront().getCenter().getFirst());
        assertEquals(4, addedCard.getFront().getCornerList().length);
        assertEquals(Content.EMPTY, addedCard.getFront().getCornerList()[0]);
        assertEquals(Content.PLANT_KINGDOM, addedCard.getFront().getCornerList()[1]);
        assertEquals(Content.INSECT_KINGDOM, addedCard.getFront().getCornerList()[2]);
        assertEquals(Content.EMPTY, addedCard.getFront().getCornerList()[3]);
        assertEquals(4, addedCard.getBack().getCornerList().length);
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getBack().getCornerList()[0]);
        assertEquals(Content.PLANT_KINGDOM, addedCard.getBack().getCornerList()[1]);
        assertEquals(Content.INSECT_KINGDOM, addedCard.getBack().getCornerList()[2]);
        assertEquals(Content.ANIMAL_KINGDOM, addedCard.getBack().getCornerList()[3]);

    }

    @Test
    //Check objective cards' deck attributes are set up correctly
    public void testSetUpStructObjectiveDeck()   {
        StructuredObjectiveCard addedCard=  (StructuredObjectiveCard) game.getObjectiveDeck().getCardList().get(0);

        assertEquals(87,  addedCard.getIdCard());
        assertEquals(3, addedCard.getResourceRequested().size());
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getResourceRequested().get(0));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getResourceRequested().get(1));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getResourceRequested().get(2));
        assertEquals(2, addedCard.getPoints());
        assertEquals(Structure.LEFT_DIAGONAL, addedCard.getStructureType());


    }

    @Test
    //Check objective cards' deck attributes are set up correctly
    public void  testSetUpNotStructObjectiveDeck()   {
        NotStructuredObjectiveCard addedCard=  (NotStructuredObjectiveCard) game.getObjectiveDeck().getCardList().get(8);
        assertEquals(95, addedCard.getIdCard());
        assertEquals(3, addedCard.getObjectRequested().size());
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(0));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(1));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(2));
        assertEquals(2, addedCard.getPoints());

    }

    @Test
    void testNextTurnNormal() {
        // Assume currentPlayer has already played
        game.setCurrentPlayerhasPlayed(true);
        game.nextTurn();

        // Check that currentPlayerhasPlayed is reset to false
        assertFalse(game.getifCurrentPlayerhasPlayed());
        // Check that the current player is updated correctly
        assertEquals(game.getListOfPlayers().get(1), game.getCurrentPlayer());
    }

    @Test
    void testNextTurnEndingGame() {
        game.updatePoints(20, player3);
        game.setState(GameState.ENDING);
        // Set lastRoundsplayed to trigger finalScoreCheck
        game.setLastRoundsplayed(game.getListOfPlayers().size() + 1);

        game.nextTurn();

        // Check that finalScoreCheck is called when all players have made their last turn
        assertEquals(GameState.ENDING, game.getState());
    }

    @Test
    //Check if the player is added correctly and the game state change if the last player join the game
    public void testAddPlayer() {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1 = new Player("user1", gameServerInstance, 0);
        Player player2 = new Player("user2", gameServerInstance, 1);

        Game game=new Game(gameServerInstance,new GameManager(),1,3, player1);
        game.addPlayer(player2);

        //Player 2 should be in the list of player and GameState should still be waiting
        assertTrue(game.getListOfPlayers().contains(player2));
        assertEquals(GameState.WAITING_FOR_PLAYERS, game.getState());

    }

    @Test
    //Check the new score is updated and lastTurn is called if the score is >=20
    public void testUpdatePoints() {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1 = new Player("user1", gameServerInstance, 0);
        Player player2 = new Player("user2", gameServerInstance,1);
        Player player3 = new Player("user3", gameServerInstance,2);

        Game game=new Game(gameServerInstance,new GameManager(),1,3, player1);
        game.addPlayer(player2);
        game.addPlayer(player3);


        game.setupField();

        game.setCurrentPlayer(player1);
        game.updatePoints(10, player1);
        assertEquals(10, game.getScores().get(player1));

        //player1's score is greater than 20, potential winner is set to current player
        game.updatePoints(12, player1);
        assertEquals(game.getPotentialWinner(), game.getCurrentPlayer());

    }


    @Test
    void setupHandsAndObjectives() {
        game.setupHandsAndObjectives();
        for (Player p :  game.getListOfPlayers()) {
            assertEquals(3, p.getHandCard().size()); // 2 resourceCard + 1 goldCard
        }
        assertEquals(2, game.getDisplayedObjectiveCard().size());
        for (Player p : game.getListOfPlayers()) {
            assertEquals(2, p.getHandObjective().size());
        }

    }


    @Test
    void randomizeFirstPlayer()  {
        game.randomizeFirstPlayer();
        Player firstPlayer = game.getFirstPlayer();
        assertTrue(game.getListOfPlayers().contains(firstPlayer));

        assertEquals(GameState.STARTED, game.getState());

        assertEquals(firstPlayer, game.getCurrentPlayer());
    }

    @Test
    void testDrawDisplayedPlayableCardGoldCard() throws DeckEmptyException {
        GoldCard goldCard = (GoldCard) game.getGoldDeck().draw();
        game.getDisplayedPlayableCard().add(goldCard);

        boolean result = game.drawDisplayedPlayableCard(goldCard, player1);

        assertTrue(result);
        assertFalse(game.getDisplayedPlayableCard().contains(goldCard)); // Ensure card is removed from displayed list
    }
    @Test
    void testDrawDisplayedPlayableCardResourceCard() throws DeckEmptyException {
        ResourceCard resourceCard = (ResourceCard) game.getResourceDeck().draw();
        game.getDisplayedPlayableCard().add(resourceCard);

        boolean result = game.drawDisplayedPlayableCard(resourceCard, player1);

        assertTrue(result);
        assertFalse(game.getDisplayedPlayableCard().contains(resourceCard)); // Ensure card is removed from displayed list
    }

    @Test
    void testDrawDisplayedPlayableCardCardNotInDisplayed() throws DeckEmptyException {
        GoldCard goldCard = (GoldCard) game.getGoldDeck().draw();

        boolean result = game.drawDisplayedPlayableCard(goldCard, player1);

        assertFalse(result);
    }

    @Test
    void testDrawDisplayedPlayableCardDeckEmptyException() throws DeckEmptyException {
        GoldCard goldCard = (GoldCard) game.getGoldDeck().draw();
        game.getGoldDeck().getCardList().clear();
        game.getDisplayedPlayableCard().add(goldCard);

        boolean result = game.drawDisplayedPlayableCard(goldCard, player1);
        assertTrue(result);

        ResourceCard resourceCard = (ResourceCard) game.getResourceDeck().draw(); // Depletes the resource deck
        game.getResourceDeck().getCardList().clear(); // Clear the resourceDeck to simulate an empty deck scenario
        game.getDisplayedPlayableCard().add(resourceCard);

        result = game.drawDisplayedPlayableCard(resourceCard, player1);
        assertTrue(result);
    }

    @Test
    void testChooseColorSuccessful() {
        assertTrue(game.chooseColor(player1, PlayerColor.RED));

        // Verify that player1's token is set to RED
        assertEquals(PlayerColor.RED, player1.getToken());

        // Verify that RED is removed from tokenAvailable
        assertFalse(game.getTokenAvailable().contains(PlayerColor.RED));

    }

    @Test
    void testChooseColorAlreadyTaken() {
        // Simulate that RED color is already taken
        game.getTokenAvailable().remove(PlayerColor.RED);

        assertFalse(game.chooseColor(player1, PlayerColor.RED));

        // Verify that player1's token is not set to RED
        assertNotEquals(PlayerColor.RED, player1.getToken());

        // Verify that RED is still removed from tokenAvailable
        assertFalse(game.getTokenAvailable().contains(PlayerColor.RED));

    }

    @Test
    void testChooseObjectiveCardSuccessful() {
        game.setupHandsAndObjectives();

        Card objectiveCard1=player1.getHandObjective().get(0);
        Card objectiveCard2=player1.getHandObjective().get(1);
        assertTrue(game.chooseObjectiveCard(player1, objectiveCard1));

        assertTrue(player1.getHandObjective().contains(objectiveCard1));

        assertTrue(game.getObjectiveDeck().getCardList().contains(objectiveCard2));

        assertEquals(1, game.getObjectiveCardsChosen());

    }

    @Test
    void testChooseObjectiveCardNotInHand() {
        game.setupHandsAndObjectives();

        Card otherPlayerObj=player2.getHandObjective().getFirst();
        Card objectiveCard1=player1.getHandObjective().get(0);

        assertFalse(game.chooseObjectiveCard(player1, otherPlayerObj));
        assertTrue(player1.getHandObjective().contains(objectiveCard1));


        assertEquals(0, game.getObjectiveCardsChosen());

    }

    @Test
    void testFinalScoreCheck() {
        game.setupHandsAndObjectives();
        game.getScores().put(player1,20);
        game.getScores().put(player2,28);
        game.finalScoreCheck();

        // Assert that player1's points were updated correctly
        assertEquals(20, game.getScores().get(player1));
        assertEquals(28, game.getScores().get(player2));


        // Assert that game state is set to ENDED
        assertEquals(GameState.ENDED, game.getState());

        // Assert the winner based on max score
        assertEquals(player2.getUsername(), game.getWinner().getUsername());
    }

    @Test
    void testEndGame() {
        GameManager gameManager = new GameManager();
        gameManager.getGameList().put(0,game);
        game.endGame();

        assertFalse(gameManager.getGameList().containsKey(game.getIdGame()));
    }
    @Test
    void testGetPlayer_existingPlayer() {
        Player player = game.getPlayer("user1");
        assertNotNull(player);
        assertEquals("user1", player.getUsername());
    }

    @Test
    void testGetPlayer_nonExistingPlayer() {
        Player player = game.getPlayer("Bea");
        assertNull(player);
    }

    @Test
    void testGetNumOfPlayers() {
        assertEquals(4, game.getNumOfPlayers());
    }

    @Test
    void testGetGameServerInstance() {
        assertEquals(gameServerInstance, game.getGameServerInstance());
    }

    @Test
    void testGetController() {
        GameController gameController=new GameController(game,gameServerInstance);
        game.setController(gameController);

        assertEquals(gameController, game.getController());
    }

    @Test
    void testClientDisconnected() {
        game.clientDisconnected();
        GameManager gameManager = new GameManager();
        gameManager.getGameList().put(0,game);
        game.endGame();

        assertFalse(gameManager.getGameList().containsKey(game.getIdGame()));
    }




}
