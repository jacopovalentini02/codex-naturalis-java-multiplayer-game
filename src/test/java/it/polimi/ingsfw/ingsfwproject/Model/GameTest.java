package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import org.junit.jupiter.api.Test;

import java.util.*;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void testSetUpGame() throws RemoteException, DeckEmptyException {
        Player player1=new Player("user1");
        Game game = new Game(1, 4, player1);
        Player player2 = new Player("user2");
        Player player3 = new Player("user3");
        Player player4 = new Player("user4");
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        game.setupGame();

        //testing the amount of cards in decks and on the field after the setup phase
        assertEquals(4, game.getDisplayedPlayableCard().size());
        assertEquals(2, game.getDisplayedObjectiveCard().size());
        assertEquals(30, game.getResourceDeck().getCardList().size());
        assertEquals(34, game.getGoldDeck().getCardList().size());
        assertEquals(2, game.getStarterDeck().getCardList().size());
        assertEquals(10, game.getObjectiveDeck().getCardList().size());
        //testing if every player has the following things:
        for(Player p : game.getListOfPlayers()) {
            //testing if the player has 3 playable cards in his hands
            assertEquals(3, p.getHandCard().size());
            //testing if the player has his secret objective
            List<ObjectiveCard> prova = new ArrayList<>();
            prova.add(p.getHandObjective());
            assertEquals(1, prova.size());
            //testing if the starter card is on the player ground
            assertEquals(1, p.getGround().getGrid().keySet().size());
            //checking if all players score is set to 0
            assertEquals(0, game.getScores().get(p));
        }
        // check to see if the first player has been assigned correctly
        assertTrue(game.getListOfPlayers().contains(game.getFirstPlayer()));
    }

    @Test
    //Check the size of each deck
    public void testSetUpCards() throws RemoteException {
        Player player1=new Player("user1");
        Game game=new Game(1,2,player1);
        game.setUpCards();

        assertEquals(40, game.getResourceDeck().getCardList().size());
        assertEquals(40, game.getGoldDeck().getCardList().size());
        assertEquals(6, game.getStarterDeck().getCardList().size());
        assertEquals(16, game.getObjectiveDeck().getCardList().size());

    }

    @Test
    //Check resource cards' deck attributes are set up correctly
    public void testSetUpResourceDeck() throws RemoteException {
        Player player1=new Player("user1");
        Game game=new Game(1,2,player1);
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
        Player player1=new Player("user1");
        Game game=new Game(1,2,player1);
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
        Player player1=new Player("user1");
        Game game=new Game(1,2,player1);
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
        Player player1=new Player("user1");
        Game game=new Game(1,2,player1);
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
        Player player1=new Player("user1");
        Game game=new Game(1,2,player1);
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
        Player player1 = new Player("user1");
        Player player2 = new Player("user2");

        Game game=new Game(1,2, player1);
        game.addPlayer(player2);
        game.setCurrentPlayer(player1);
        game.nextTurn();

        //next player should be player2
        assertEquals(player2, game.getCurrentPlayer());
        game.nextTurn();
        //next player should be player1
        assertEquals(player1, game.getCurrentPlayer());

    }

    @Test
    //Check if the player is added correctly and the game state change if the last player join the game
    public void testAddPlayer() throws RemoteException {
        Player player1 = new Player("user1");
        Player player2 = new Player("user2");
        Player player3 = new Player("user3");

        Game game=new Game(1,3, player1);
        game.addPlayer(player2);

        //Player 2 should be in the list of player and GameState should still be waiting
        assertTrue(game.getListOfPlayers().contains(player2));
        assertEquals(GameState.WAITING_FOR_PLAYERS, game.getState());

        //Check player 3 is added correctly and game state is changed since there are 3 players
        game.addPlayer(player3);
        assertTrue(game.getListOfPlayers().contains(player3));
        assertEquals(GameState.STARTED, game.getState());
    }

    @Test
    //Check the new score is updated and lastTurn is called if the score is >=20
    public void testUpdatePoints() throws RemoteException, DeckEmptyException {
        Player player1 = new Player("user1");
        Player player2 = new Player("user2");
        Player player3 = new Player("user3");

        Game game=new Game(1,3, player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        game.setupGame();

        game.setCurrentPlayer(player1);
        game.updatePoints(10);
        assertEquals(10, game.getScores().get(player1));

        //player1's score is greater than 20, lastTurn should be called and the game state is set to ENDING
        game.updatePoints(12);
        assertEquals(GameState.ENDING, game.getState());


    }

    @Test
    public void testLastTurn() throws RemoteException, DeckEmptyException {
        Player player1=new Player("user1");
        Game game = new Game(1, 4, player1);
        Player player2 = new Player("user2");
        Player player3 = new Player("user3");
        Player player4 = new Player("user4");
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        //we need to create the istance of hand cards and grids
        game.setupGame();
        //randomize the first player scoring 20 points
        Random rand = new Random();
        int index = rand.nextInt(game.getListOfPlayers().size());


        //Save the score of each player
        Map<Player, Integer> initialHandSizes = new HashMap<>();
        for (Player player : game.getListOfPlayers()) {
            initialHandSizes.put(player, player.getHandCard().size());
        }

        game.lastTurn(game.getListOfPlayers().get(index));

        //Game state should be in Ending state
        assertSame(game.getState(), GameState.ENDING);

        //Check that every player's card set is decreased by 1
        for (Player player : game.getListOfPlayers()) {
            assertEquals((int) initialHandSizes.get(player) - 1, player.getHandCard().size());
        }


    }
}