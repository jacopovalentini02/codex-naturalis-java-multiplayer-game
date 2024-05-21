//package it.polimi.ingsfw.ingsfwproject.Model;
//
//import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
//import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PlayerTest {
//
//    @Test
//    //it checks if the drawn card is in the player's hand
//    void TestDraw() throws DeckEmptyException {
//        Deck deck = new Deck();
//        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
//        //Create and add a card to the deck
//        Content[] corner = { Content.FUNGI_KINGDOM, Content.EMPTY, Content.FUNGI_KINGDOM, Content.HIDDEN };
//        NormalBack backFace=new NormalBack(1, emptyCorners, Content.FUNGI_KINGDOM);
//        NormalFace front=new NormalFace(1, 0, corner);
//        ResourceCard card1 = new ResourceCard(front,backFace,1);
//        deck.addCard(card1);
//
//        GameServerInstance gameServerInstance=new GameServerInstance();
//        Player player=new Player("player1", gameServerInstance, 0);
//        player.draw(deck);
//
//        //Check if the card was added to the player's Hand
//        assertTrue(player.getHandCard().contains((PlayableCard) card1));
//
//    }
//
//    @Test
//    //Check if the card picked from the displayed card is in the player's hand
//    void TestPick() {
//        GameServerInstance gameServerInstance=new GameServerInstance();
//
//        Player player=new Player("player1", gameServerInstance, 0);
//        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
//        Content[] corner = { Content.FUNGI_KINGDOM, Content.EMPTY, Content.FUNGI_KINGDOM, Content.HIDDEN };
//        NormalBack backFace=new NormalBack(1, emptyCorners, Content.FUNGI_KINGDOM);
//        NormalFace front=new NormalFace(1, 0, corner);
//        ResourceCard card1 = new ResourceCard(front,backFace,1);
//
//        player.pick(card1);
//        assertTrue(player.getHandCard().contains((PlayableCard) card1));
//
//    }
//
//    @Test
//    void TestPlayCard() throws DeckEmptyException {
//        Deck deck = new Deck();
//        GameServerInstance gameServerInstance=new GameServerInstance();
//
//        Player player=new Player("player1", gameServerInstance, 0);
//
//        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
//        //Create and add a card to the deck
//        Content[] corner = { Content.FUNGI_KINGDOM, Content.EMPTY, Content.FUNGI_KINGDOM, Content.HIDDEN };
//        NormalBack backFace=new NormalBack(1, emptyCorners, Content.FUNGI_KINGDOM);
//        NormalFace front=new NormalFace(1, 0, corner);
//        ResourceCard card1 = new ResourceCard(front,backFace,1);
//        deck.addCard(card1);
//
//        corner = new Content[]{Content.FUNGI_KINGDOM, Content.FUNGI_KINGDOM, Content.HIDDEN, Content.EMPTY};
//        backFace = new NormalBack(2, emptyCorners, Content.FUNGI_KINGDOM);
//        front=new NormalFace(2, 0, corner);
//        ResourceCard preRes2 = new ResourceCard(front, backFace, 2);
//        deck.addCard(preRes2);
//
//        player.draw(deck);
//        player.draw(deck);
//
//        PlayableCard card=player.getHandCard().getFirst();
//        assertDoesNotThrow(() -> player.playCard(card, true, new Coordinate(0,0)));
//        //card played should be removed from player's hand
//        assertFalse(player.getHandCard().contains(card));
//
//    }
//}