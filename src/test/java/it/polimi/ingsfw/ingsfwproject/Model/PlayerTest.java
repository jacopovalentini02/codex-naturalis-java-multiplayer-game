package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private GameServerInstance gameServerInstance;
    private Player player;
    private GameManager manager;
    private Game game;

    @BeforeEach
    void setUp()  {
        gameServerInstance = new GameServerInstance();
        player = new Player("player1", gameServerInstance, 0);
        manager = new GameManager();
        game = new Game(gameServerInstance, manager, 1, 2, player);
        game.setUpCards();

    }
    @Test
    //it checks if the drawn card is in the player's hand
    void TestDraw()   {
        Deck deck = new Deck();
        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
        //Create and add a card to the deck
        Content[] corner = { Content.FUNGI_KINGDOM, Content.EMPTY, Content.FUNGI_KINGDOM, Content.HIDDEN };
        NormalBack backFace=new NormalBack(1, emptyCorners, Content.FUNGI_KINGDOM, "");
        NormalFace front=new NormalFace(1, 0, corner, "");
        ResourceCard card1 = new ResourceCard(front,backFace,1);
        deck.addCard(card1);

        player.draw(deck);

        //Check if the card was added to the player's Hand
        assertTrue(player.getHandCard().contains((PlayableCard) card1));

    }

    @Test
    void testDrawCardReturnsNullDeckEmpty()  {
        Deck nullDeck = new Deck();
        boolean result = player.draw(nullDeck);

        // Check that the draw method returns false when the draw method of the deck returns null
        assertFalse(result);

    }

    @Test
    //Check if the card picked from the displayed card is in the player's hand
    void TestPick() {
        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
        Content[] corner = { Content.FUNGI_KINGDOM, Content.EMPTY, Content.FUNGI_KINGDOM, Content.HIDDEN };
        NormalBack backFace=new NormalBack(1, emptyCorners, Content.FUNGI_KINGDOM, "");
        NormalFace front=new NormalFace(1, 0, corner, "");
        ResourceCard card1 = new ResourceCard(front,backFace,1);

        player.pick(card1);
        assertTrue(player.getHandCard().contains((PlayableCard) card1));

    }

    @Test
    void testPlayCardSuccessfully() throws DeckEmptyException {
        PlayableCard card1 = (PlayableCard) game.getResourceDeck().draw();
        player.addToHand(card1);
        PlayableCard card = player.getHandCard().getFirst();
        Coordinate coord = new Coordinate(0, 0);

        assertDoesNotThrow(() -> player.playCard(card, true, coord));
        assertFalse(player.getHandCard().contains(card));
    }
    @Test
    void testPlayCardNotInHand() {
        PlayableCard cardNotInHand = new ResourceCard(null, null, -3);
        Coordinate coord = new Coordinate(0, 0);

        int result = player.playCard(cardNotInHand, true, coord);
        assertEquals(-1, result);
        // Ensure no other card is removed from the hand
        //assertEquals(0, player.getHandCard().size());
    }

    @Test
    void testPlayCardPositionNotAvailableException() throws DeckEmptyException {
        PlayableCard card1 = (PlayableCard) game.getResourceDeck().draw();
        player.addToHand(card1);
        PlayableCard card = player.getHandCard().getFirst();
        Coordinate coord = new Coordinate(10, 10);

        int result = player.playCard(card, true, coord);
        assertEquals(-1, result);
        // Ensure the card is still in the player's hand
        assertTrue(player.getHandCard().contains(card));
    }

    @Test
    void testPlayCardNotEnoughResourcesException() throws DeckEmptyException {
        GoldCard goldCard= (GoldCard) game.getGoldDeck().draw();
        player.addToHand(goldCard);

        PlayableCard card = player.getHandCard().getFirst();
        Coordinate coord = new Coordinate(0, 0);

        int result = player.playCard(card, true, coord);
        assertEquals(-1, result);
        // Ensure the card is still in the player's hand
        assertTrue(player.getHandCard().contains(card));
    }

    @Test
    void testAddToHand()   {
        ArrayList<PlayableCard> cardsToAdd = new ArrayList<>();
        PlayableCard card1 = (PlayableCard) game.getResourceDeck().getCardList().getFirst();
        PlayableCard card2 = (PlayableCard) game.getResourceDeck().getCardList().get(1);
        PlayableCard card3 = (PlayableCard) game.getResourceDeck().getCardList().getLast();
        cardsToAdd.add(card1);
        cardsToAdd.add(card2);
        cardsToAdd.add(card3);

        //Add cards to player's hand
        player.addToHand(cardsToAdd);

        //Check cards were correctly added to player's hand
        assertTrue(player.getHandCard().contains(card1));
        assertTrue(player.getHandCard().contains(card2));
        assertTrue(player.getHandCard().contains(card3));
    }

    @Test
    void testAddToHandObjective(){
        ArrayList<ObjectiveCard> cardsToAdd = new ArrayList<>();
        ObjectiveCard card1=(ObjectiveCard) game.getObjectiveDeck().getCardList().getFirst();
        ObjectiveCard card2=(ObjectiveCard) game.getObjectiveDeck().getCardList().getLast();
        cardsToAdd.add(card1);
        cardsToAdd.add(card2);

        player.addToHandObjective(cardsToAdd);

        assertTrue(player.getHandObjective().contains(card1));
        assertTrue(player.getHandObjective().contains(card2));

    }

    @Test
    void testSetToken() {
        PlayerColor color = PlayerColor.RED;
        player.setToken(color);

        // Check if the token was set correctly
        assertEquals(color, player.getToken());
        
    }
}