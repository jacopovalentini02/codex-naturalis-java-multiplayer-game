package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    //it checks if the drawn card is in the player's hand
    void TestDraw() throws DeckEmptyException {
        Deck deck = new Deck();
        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
        //Create and add a card to the deck
        Content[] corner = { Content.FUNGI_KINGDOM, Content.EMPTY, Content.FUNGI_KINGDOM, Content.HIDDEN };
        NormalBack backFace=new NormalBack(1, emptyCorners, Content.FUNGI_KINGDOM, "");
        NormalFace front=new NormalFace(1, 0, corner, "");
        ResourceCard card1 = new ResourceCard(front,backFace,1);
        deck.addCard(card1);

        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player=new Player("player1", gameServerInstance, 0);
        player.draw(deck);

        //Check if the card was added to the player's Hand
        assertTrue(player.getHandCard().contains((PlayableCard) card1));

    }

    @Test
    void testDrawCardReturnsNullDeckEmpty() throws DeckEmptyException {
        Deck nullDeck = new Deck();
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player=new Player("player1", gameServerInstance, 0);
        boolean result = player.draw(nullDeck);

        // Check that the draw method returns false when the draw method of the deck returns null
        assertFalse(result);

    }

    @Test
    //Check if the card picked from the displayed card is in the player's hand
    void TestPick() {
        GameServerInstance gameServerInstance=new GameServerInstance();

        Player player=new Player("player1", gameServerInstance, 0);
        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
        Content[] corner = { Content.FUNGI_KINGDOM, Content.EMPTY, Content.FUNGI_KINGDOM, Content.HIDDEN };
        NormalBack backFace=new NormalBack(1, emptyCorners, Content.FUNGI_KINGDOM, "");
        NormalFace front=new NormalFace(1, 0, corner, "");
        ResourceCard card1 = new ResourceCard(front,backFace,1);

        player.pick(card1);
        assertTrue(player.getHandCard().contains((PlayableCard) card1));

    }

    @Test
    void TestPlayCard() throws DeckEmptyException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player=new Player("player1", gameServerInstance, 0);
        GameManager manager = new GameManager();
        Game game = new Game(gameServerInstance, manager, 1, 4, player);
        game.setUpCards();

        Card goldCard=game.getGoldDeck().getCardList().getFirst();
        player.draw(game.getResourceDeck());
        //player.getHandCard().add((PlayableCard) goldCard);

        //Check play card does not throw exception
        PlayableCard card=player.getHandCard().getFirst();
        assertDoesNotThrow(() -> player.playCard(card, true, new Coordinate(0,0)));
        //card played should be removed from player's hand
        assertFalse(player.getHandCard().contains(card));

//        System.out.println(goldCard.getIdCard());
//        assertThrows(NotEnoughResourcesException.class, () -> {
//            player.playCard((PlayableCard) goldCard, true, new Coordinate(1, 1));
//        });

    }

    @Test
    void testAddToHand() throws DeckEmptyException {
        GameServerInstance gameServerInstance = new GameServerInstance();
        Player player = new Player("player1", gameServerInstance, 0);
        GameManager manager = new GameManager();
        Game game = new Game(gameServerInstance, manager, 1, 4, player);
        game.setUpCards();

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
        GameServerInstance gameServerInstance = new GameServerInstance();
        Player player = new Player("player1", gameServerInstance, 0);
        GameManager manager = new GameManager();
        Game game = new Game(gameServerInstance, manager, 1, 4, player);
        game.setUpCards();

        ArrayList<ObjectiveCard> cardsToAdd = new ArrayList<>();
        ObjectiveCard card1=(ObjectiveCard) game.getObjectiveDeck().getCardList().getFirst();
        ObjectiveCard card2=(ObjectiveCard) game.getObjectiveDeck().getCardList().getLast();
        cardsToAdd.add(card1);
        cardsToAdd.add(card2);

        player.addToHandObjective(cardsToAdd);

        assertTrue(player.getHandObjective().contains(card1));
        assertTrue(player.getHandObjective().contains(card2));

    }
}