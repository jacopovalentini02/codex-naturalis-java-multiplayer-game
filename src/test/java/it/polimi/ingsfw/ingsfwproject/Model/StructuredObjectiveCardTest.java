package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StructuredObjectiveCardTest {

    @Test
    void verifyObjective() throws RemoteException, DeckEmptyException {
        Player player1 = new Player("player1");
        PlayerGround ground = player1.getGround();
        Map<Coordinate, Face> grid = ground.getGrid();

        Game game = new Game(1, 4, player1);
        game.setUpCards();
        Deck resourceDeck = game.getResourceDeck();
        Deck goldDeck = game.getGoldDeck();
        Deck starterDeck = game.getStarterDeck();
        StarterCard card1 = (StarterCard) starterDeck.draw();

        ground.playCard(card1, true, new Coordinate(0,0));

        assertEquals(1, grid.size());
        assertEquals(card1.getFront(), grid.get(new Coordinate(0,0)));

        Deck objectiveDeck = game.getObjectiveDeck();
        ArrayList<Card> objectiveCards = objectiveDeck.getCardList();

        assertEquals(16, objectiveCards.size());
        ObjectiveCard redRightDiagonalObjectiveCard = null;
        
        for (Card card : objectiveCards) {
            if (card.getIdCard() == 87)
                redRightDiagonalObjectiveCard = (ObjectiveCard) card;
        }
        
        assertNotEquals(null, redRightDiagonalObjectiveCard);

        ResourceCard card2 = (ResourceCard) resourceDeck.draw();
        ResourceCard card3 = (ResourceCard) resourceDeck.draw();
        ResourceCard card4 = (ResourceCard) resourceDeck.draw();
        ResourceCard card5 = (ResourceCard) resourceDeck.draw();
        ResourceCard card6 = (ResourceCard) resourceDeck.draw();
        ResourceCard card7 = (ResourceCard) resourceDeck.draw();


        assertEquals(Card.getType(card2.getBack().getIdCard()), Content.FUNGI_KINGDOM);
        assertEquals(Card.getType(card3.getBack().getIdCard()), Content.FUNGI_KINGDOM);
        assertEquals(Card.getType(card4.getBack().getIdCard()), Content.FUNGI_KINGDOM);
        assertEquals(Card.getType(card5.getBack().getIdCard()), Content.FUNGI_KINGDOM);
        assertEquals(Card.getType(card6.getBack().getIdCard()), Content.FUNGI_KINGDOM);
        assertEquals(Card.getType(card7.getBack().getIdCard()), Content.FUNGI_KINGDOM);

        ground.playCard(card2, true, new Coordinate(1,1));
        assertEquals(2, grid.size());

        ground.playCard(card3, true, new Coordinate(2,2));
        assertEquals(3, grid.size());

        ground.playCard(card4, true, new Coordinate(3,3));
        assertEquals(4, grid.size());

        int pointsGivenbyObjective;
        assert redRightDiagonalObjectiveCard != null;
        //int pointsGivenbyObjective = redRightDiagonalObjectiveCard.verifyObjective(ground);

        //assertEquals(2, pointsGivenbyObjective);

        ground.playCard(card5, true, new Coordinate(4,4));
        assertEquals(5, grid.size());

        //pointsGivenbyObjective = redRightDiagonalObjectiveCard.verifyObjective(ground);
        //assertEquals(2, pointsGivenbyObjective);

        ground.playCard(card6, true, new Coordinate(5,5));
        assertEquals(6, grid.size());

        pointsGivenbyObjective = redRightDiagonalObjectiveCard.verifyObjective(ground);
        assertEquals(2, pointsGivenbyObjective);

        ground.playCard(card7, true, new Coordinate(6,6));
        assertEquals(7, grid.size());

        pointsGivenbyObjective = redRightDiagonalObjectiveCard.verifyObjective(ground);
        assertEquals(4, pointsGivenbyObjective);

    }
}