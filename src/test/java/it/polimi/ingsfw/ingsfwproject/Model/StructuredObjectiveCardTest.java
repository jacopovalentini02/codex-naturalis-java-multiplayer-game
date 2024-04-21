package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StructuredObjectiveCardTest {

    @Test
    void verifyRightDiagonalObjective() throws RemoteException, DeckEmptyException {
        Player player1 = new Player("player1");
        PlayerGround ground = player1.getGround();
        Map<Coordinate, Face> grid = ground.getGrid();
        GameManager manager = new GameManager();
        Game game = new Game(manager, 1, 4, player1);
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

    @Test
    void verifyLeftDiagonalObjective() throws RemoteException, DeckEmptyException{
        Player player1 = new Player("player1");
        PlayerGround ground = player1.getGround();
        Map<Coordinate, Face> grid = ground.getGrid();
        GameManager manager = new GameManager();
        Game game = new Game(manager, 1, 4, player1);
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
        ObjectiveCard greenLeftDiagonalObjectiveCard = null;

        for (Card card : objectiveCards) {
            if (card.getIdCard() == 88)
                greenLeftDiagonalObjectiveCard = (ObjectiveCard) card;
        }

        assertNotEquals(null, greenLeftDiagonalObjectiveCard);

        ArrayList<Card> greenCards = new ArrayList<>();

        for (Card c: resourceDeck.getCardList()){
            if (Card.getType(c.getIdCard()) == Content.PLANT_KINGDOM)
                greenCards.add(c);
        }

        for (Card c: goldDeck.getCardList()){
            if (Card.getType(c.getIdCard()) == Content.PLANT_KINGDOM)
                greenCards.add(c);
        }

        for (Card c: greenCards){
            assertEquals(Card.getType(c.getIdCard()), Content.PLANT_KINGDOM);}

        int max = 20;
        int min = -20;

        Collections.shuffle(greenCards);

        //creating diagonal structures randomly in the map
        for (int i = 0; i < 3; i++){
            Random random = new Random();
            int x = random.nextInt(max-min + 1)+ min;
            int y = random.nextInt(max-min+1)+min;
            Coordinate coordinate1 = new Coordinate(x,y);
            Coordinate coordinate2 = new Coordinate(x-1, y+1);
            Coordinate coordinate3 = new Coordinate(x-2, y+2);
            PlayableCard card2 = (PlayableCard)greenCards.removeFirst();
            PlayableCard card3 = (PlayableCard)greenCards.removeFirst();
            PlayableCard card4 = (PlayableCard)greenCards.removeFirst();

            ground.playCard(card2, true, coordinate1);
            ground.playCard(card3, true, coordinate2);
            ground.playCard(card4, false, coordinate3);
        }

        assertEquals(10, grid.size());

        assert greenLeftDiagonalObjectiveCard != null;

        int pointsGiven = greenLeftDiagonalObjectiveCard.verifyObjective(ground);

        assertEquals(6, pointsGiven);

    }

    @Test
    void verifyDoubleStructureObjective() throws RemoteException, DeckEmptyException{
        Player player1 = new Player("player1");
        PlayerGround ground = player1.getGround();
        Map<Coordinate, Face> grid = ground.getGrid();
        GameManager manager = new GameManager();
        Game game = new Game(manager, 1, 4, player1);
        game.setUpCards();
        Deck resourceDeck = game.getResourceDeck();
        Deck goldDeck = game.getGoldDeck();
        Deck starterDeck = game.getStarterDeck();
        StarterCard card1 = (StarterCard) starterDeck.draw();

        ground.playCard(card1, true, new Coordinate(0,0));

        Deck objectiveDeck = game.getObjectiveDeck();
        ArrayList<Card> objectiveCards = objectiveDeck.getCardList();

        ArrayList<StructuredObjectiveCard> doubleStructureObjectives = new ArrayList<>();

        for (Card c: objectiveCards){
            if (c.getIdCard() >= 91 && c.getIdCard() <= 94)
                doubleStructureObjectives.add((StructuredObjectiveCard) c);
        }
        assertEquals(4, doubleStructureObjectives.size());

        Collections.shuffle(doubleStructureObjectives);
        StructuredObjectiveCard cardToTest = doubleStructureObjectives.getFirst();

        ArrayList<Card> greenCards = new ArrayList<>();
        ArrayList<Card> redCards = new ArrayList<>();
        ArrayList<Card> purpleCards = new ArrayList<>();
        ArrayList<Card> blueCards = new ArrayList<>();


        for (Card c: resourceDeck.getCardList()){
           if (Card.getType(c.getIdCard()) == Content.FUNGI_KINGDOM)
               redCards.add(c);
           if (Card.getType(c.getIdCard()) == Content.INSECT_KINGDOM)
                purpleCards.add(c);
           if (Card.getType(c.getIdCard()) == Content.ANIMAL_KINGDOM)
               blueCards.add(c);
           if (Card.getType(c.getIdCard()) == Content.PLANT_KINGDOM)
               greenCards.add(c);
        }

        for (Card c: goldDeck.getCardList()){
            if (Card.getType(c.getIdCard()) == Content.FUNGI_KINGDOM)
                redCards.add(c);
            if (Card.getType(c.getIdCard()) == Content.INSECT_KINGDOM)
                purpleCards.add(c);
            if (Card.getType(c.getIdCard()) == Content.ANIMAL_KINGDOM)
                blueCards.add(c);
            if (Card.getType(c.getIdCard()) == Content.PLANT_KINGDOM)
                greenCards.add(c);
        }

        assertEquals(20, redCards.size());
        assertEquals(20, greenCards.size());
        assertEquals(20, purpleCards.size());
        assertEquals(20, blueCards.size());


        Collections.shuffle(redCards);
        Collections.shuffle(greenCards);
        Collections.shuffle(purpleCards);
        Collections.shuffle(blueCards);

        int max = 20;
        int min = -20;

        int structuresCreated = 0;

        for (int i = 0; i < 4; i++){
            Random random = new Random();
            int x = random.nextInt(max-min + 1)+ min;
            int y = random.nextInt(max-min+1)+min;
            Coordinate coordinate1 = new Coordinate(x,y);
            if (grid.containsKey(coordinate1)) continue;

            Coordinate coordinate2;
            Coordinate coordinate3;
            Card card2;
            Card card3;
            Card card4;

            switch (cardToTest.getStructureType()){
                case DOUBLE_UP_LEFT: {
                    coordinate2 = new Coordinate(x, y+1);
                    coordinate3 = new Coordinate(x-1, y+2);
                    card2 = purpleCards.removeFirst();
                    card3 = purpleCards.removeFirst();
                    card4 = blueCards.removeFirst();
                } break;
                case DOUBLE_UP_RIGHT: {
                    coordinate2 = new Coordinate(x, y+1);
                    coordinate3 = new Coordinate(x+1,y+2);
                    card2 = blueCards.removeFirst();
                    card3 = blueCards.removeFirst();
                    card4 = redCards.removeFirst();
                }break;



            }





        }




    }




}