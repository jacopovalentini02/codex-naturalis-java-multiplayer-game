package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.CardNotInHandException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.junit.jupiter.api.Test;

import java.util.*;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    //Check the size of each deck
    public void testSetUpCards() throws RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1=new Player("user1", gameServerInstance, 0);
        Game game=new Game(gameServerInstance,new GameManager(), 1,2,player1);
        game.setUpCards();

        assertEquals(40, game.getResourceDeck().getCardList().size());
        assertEquals(40, game.getGoldDeck().getCardList().size());
        assertEquals(6, game.getStarterDeck().getCardList().size());
        assertEquals(16, game.getObjectiveDeck().getCardList().size());

    }

    @Test
    //Check resource cards' deck attributes are set up correctly
    public void testSetUpResourceDeck() throws RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1=new Player("user1", gameServerInstance, 0);
        Game game=new Game(gameServerInstance,new GameManager(), 1,2,player1);
        game.setUpCards();

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
    public void testSetUpGoldDeck() throws RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1=new Player("user1", gameServerInstance, 0);
        Game game=new Game(gameServerInstance,new GameManager(), 1,2,player1);
        game.setUpCards();

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
    public void testSetUpStarterDeck() throws RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1=new Player("user1", gameServerInstance, 0);
        Game game=new Game(gameServerInstance,new GameManager(), 1,2,player1);
        game.setUpCards();

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
    public void testSetUpStructObjectiveDeck() throws RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1=new Player("user1", gameServerInstance, 0);
        Game game=new Game(gameServerInstance,new GameManager(), 1,2,player1);
        game.setUpCards();

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
    public void  testSetUpNotStructObjectiveDeck() throws RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1=new Player("user1", gameServerInstance, 0);
        Game game=new Game(gameServerInstance,new GameManager(), 1,2,player1);
        game.setUpCards();

        NotStructuredObjectiveCard addedCard=  (NotStructuredObjectiveCard) game.getObjectiveDeck().getCardList().get(8);
        assertEquals(95, addedCard.getIdCard());
        assertEquals(3, addedCard.getObjectRequested().size());
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(0));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(1));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(2));
        assertEquals(2, addedCard.getPoints());


    }

    @Test
    //Check if the next turn function works correctly
    public void testNextTurn() throws RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1=new Player("user1", gameServerInstance, 0);
        Game game=new Game(gameServerInstance,new GameManager(), 1,2,player1);
        Player player2 = new Player("user2", gameServerInstance, 1);


        game.addPlayer(player2);
        game.setCurrentPlayer(player1);

        game.nextTurn();

        player1.getHandObjective().add((ObjectiveCard) game.getObjectiveDeck().getCardList().getFirst());
        player2.getHandObjective().add((ObjectiveCard) game.getObjectiveDeck().getCardList().getFirst());

        //next player should be player2
        assertEquals(player2, game.getCurrentPlayer());

        game.updatePoints(20, player2);
        game.setState(GameState.ENDING);
        try {
            game.nextTurn(); // First round
            assertEquals(player1, game.getCurrentPlayer());
            assertFalse(game.getifCurrentPlayerhasPlayed());

            game.nextTurn(); // Second round
            assertEquals(player2, game.getCurrentPlayer());
            assertFalse(game.getifCurrentPlayerhasPlayed());

            game.nextTurn();

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        //Check both player played an additional round
        //assertEquals(3, game.getLastRoundsplayed());

    }

    @Test
    //Check if the player is added correctly and the game state change if the last player join the game
    public void testAddPlayer() throws RemoteException, DeckEmptyException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1 = new Player("user1", gameServerInstance, 0);
        Player player2 = new Player("user2", gameServerInstance, 1);
        Player player3 = new Player("user3", gameServerInstance, 2);

        Game game=new Game(gameServerInstance,new GameManager(),1,3, player1);
        game.addPlayer(player2);

        //Player 2 should be in the list of player and GameState should still be waiting
        assertTrue(game.getListOfPlayers().contains(player2));
        assertEquals(GameState.WAITING_FOR_PLAYERS, game.getState());

        //Check player 3 is added correctly and game state is changed since there are 3 players
        game.addPlayer(player3);
        assertTrue(game.getListOfPlayers().contains(player3));
        assertEquals(GameState.CHOOSING_STARTER_CARDS, game.getState());

        game.setupField();

    }

    @Test
    //Check the new score is updated and lastTurn is called if the score is >=20
    public void testUpdatePoints() throws RemoteException, DeckEmptyException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException {
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
    void testSetupField() throws DeckEmptyException, RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1 = new Player("user1", gameServerInstance, 0);
        Player player2 = new Player("user2", gameServerInstance, 1);
        Player player3 = new Player("user3", gameServerInstance, 2);
        Player player4 = new Player("user4", gameServerInstance, 3);


        Game game=new Game(gameServerInstance,new GameManager(),1,3, player1);

        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        //4 players automatically invokes the setupField() method

        assertEquals(38, game.getResourceDeck().getCardList().size());
        assertEquals(38, game.getGoldDeck().getCardList().size());
        assertEquals(2, game.getStarterDeck().getCardList().size());
        assertEquals(4, game.getDisplayedPlayableCard().size());
        for(Player p : game.getListOfPlayers()){
            assertEquals(1, p.getHandCard().size());
        }
    }


    @Test
    void setupHandsAndObjectives() throws RemoteException, DeckEmptyException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1 = new Player("user1", gameServerInstance, 0);
        Player player2 = new Player("user2", gameServerInstance,1);
        Player player3 = new Player("user3", gameServerInstance,2);
        Player player4 = new Player("user4", gameServerInstance,3);


        Game game=new Game(gameServerInstance,new GameManager(),1,3, player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        //simulating the play of the starter card
        for (Player p : game.getListOfPlayers()){
            p.getHandCard().remove(0);
        }
        game.setupHandsAndObjectives();

        assertEquals(30, game.getResourceDeck().getCardList().size());
        assertEquals(34, game.getGoldDeck().getCardList().size());
        assertEquals(2, game.getStarterDeck().getCardList().size());
        assertEquals(6, game.getObjectiveDeck().getCardList().size());
        assertEquals(4, game.getDisplayedPlayableCard().size());
        assertEquals(2, game.getDisplayedObjectiveCard().size());
        for (Player p : game.getListOfPlayers()){
            assertEquals(0,game.getScores().get(p));
        }

    }

    @Test
    void randomizeFirstPlayer() throws RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1 = new Player("user1", gameServerInstance, 0);
        Game game = new Game(gameServerInstance,new GameManager(), 1, 4, player1);
        Player player2 = new Player("user2", gameServerInstance, 1);
        Player player3 = new Player("user3", gameServerInstance,2);
        Player player4 = new Player("user4", gameServerInstance,3);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        //simulating the play of the starter card
        for (Player p : game.getListOfPlayers()){
            p.getHandCard().remove(0);
        }

        game.randomizeFirstPlayer();

        assertTrue(game.getFirstPlayer().equals(game.getCurrentPlayer()));
    }
}