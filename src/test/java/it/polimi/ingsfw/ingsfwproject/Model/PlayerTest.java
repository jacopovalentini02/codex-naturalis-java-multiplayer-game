package it.polimi.ingsfw.ingsfwproject.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    //it checks if the drawn card is in the player's hand
    void TestDraw() {
        Deck deck = new Deck();
        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
        //Create and add a card to the deck
        Content[] corner = { Content.FUNGI_KINGDOM, Content.EMPTY, Content.FUNGI_KINGDOM, Content.HIDDEN };
        NormalBack backFace=new NormalBack(1, emptyCorners, Content.FUNGI_KINGDOM);
        NormalFace front=new NormalFace(1, 0, corner);
        ResourceCard card1 = new ResourceCard(front,backFace,1);
        deck.addCard(card1);

        Player player=new Player("player1");
        player.draw(deck);

        //Check if the card was added to the player's Hand
        assertTrue(player.getHandCard().contains((PlayableCard) card1));

    }

    @Test
    //Check if the card picked from the displayed card is in the player's hand
    void TestPick() {
        Player player=new Player("player1");
        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
        Content[] corner = { Content.FUNGI_KINGDOM, Content.EMPTY, Content.FUNGI_KINGDOM, Content.HIDDEN };
        NormalBack backFace=new NormalBack(1, emptyCorners, Content.FUNGI_KINGDOM);
        NormalFace front=new NormalFace(1, 0, corner);
        ResourceCard card1 = new ResourceCard(front,backFace,1);

        player.pick(card1);
        assertTrue(player.getHandCard().contains((PlayableCard) card1));


    }
}