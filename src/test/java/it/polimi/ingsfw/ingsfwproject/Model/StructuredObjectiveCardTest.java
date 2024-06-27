package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.junit.jupiter.api.RepeatedTest;
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
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1 = new Player("player1", gameServerInstance, 0);
        PlayerGround ground = player1.getGround();
        Map<Coordinate, Face> grid = ground.getGrid();
        GameManager manager = new GameManager();
        Game game = new Game(gameServerInstance,manager, 1, 4, player1);
        game.setUpCards();
        Deck resourceDeck = game.getResourceDeck();
        Deck goldDeck = game.getGoldDeck();
        Deck starterDeck = game.getStarterDeck();
        StarterCard card1 = (StarterCard) starterDeck.draw();

        grid.put(new Coordinate(0,0), card1.getFront());

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

        grid.put(new Coordinate(1,1), card2.getFront());
        assertEquals(2, grid.size());


        grid.put(new Coordinate(2,2), card3.getFront());
        assertEquals(3, grid.size());


        grid.put(new Coordinate(3,3), card4.getFront());
        assertEquals(4, grid.size());

        int pointsGivenbyObjective;
        assert redRightDiagonalObjectiveCard != null;
        //int pointsGivenbyObjective = redRightDiagonalObjectiveCard.verifyObjective(ground);

        //assertEquals(2, pointsGivenbyObjective);

        grid.put(new Coordinate(4,4), card5.getFront());
        assertEquals(5, grid.size());

        //pointsGivenbyObjective = redRightDiagonalObjectiveCard.verifyObjective(ground);
        //assertEquals(2, pointsGivenbyObjective);


        grid.put(new Coordinate(5,5), card7.getFront());
        assertEquals(6, grid.size());

        pointsGivenbyObjective = redRightDiagonalObjectiveCard.verifyObjective(ground);
        assertEquals(2, pointsGivenbyObjective);

        grid.put(new Coordinate(6,6), card7.getFront());
        assertEquals(7, grid.size());

        pointsGivenbyObjective = redRightDiagonalObjectiveCard.verifyObjective(ground);
        assertEquals(4, pointsGivenbyObjective);

    }

    @Test
    void verifyLeftDiagonalObjective() throws RemoteException, DeckEmptyException{
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1 = new Player("player1", gameServerInstance, 0);
        PlayerGround ground = player1.getGround();
        Map<Coordinate, Face> grid = ground.getGrid();
        GameManager manager = new GameManager();
        Game game = new Game(gameServerInstance,manager, 1, 4, player1);
        game.setUpCards();
        Deck resourceDeck = game.getResourceDeck();
        Deck goldDeck = game.getGoldDeck();
        Deck starterDeck = game.getStarterDeck();
        StarterCard card1 = (StarterCard) starterDeck.draw();


        grid.put(new Coordinate(0,0), card1.getFront());

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

            grid.put(coordinate1, card2.getFront());
            grid.put(coordinate2, card3.getFront());
            grid.put(coordinate3, card4.getFront());
        }

        assertEquals(10, grid.size());

        assert greenLeftDiagonalObjectiveCard != null;

        int pointsGiven = greenLeftDiagonalObjectiveCard.verifyObjective(ground);

        assertEquals(6, pointsGiven);

    }

    @RepeatedTest(100)
    void verifyDoubleStructureObjective() throws RemoteException, DeckEmptyException{
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1 = new Player("player1", gameServerInstance, 0);
        PlayerGround ground = player1.getGround();
        Map<Coordinate, Face> grid = ground.getGrid();
        GameManager manager = new GameManager();
        Game game = new Game(gameServerInstance,manager, 1, 4, player1);
        game.setUpCards();
        Deck resourceDeck = game.getResourceDeck();
        Deck goldDeck = game.getGoldDeck();
        Deck starterDeck = game.getStarterDeck();
        StarterCard card1 = (StarterCard) starterDeck.draw();


        grid.put(new Coordinate(0,0), card1.getFront());

        Deck objectiveDeck = game.getObjectiveDeck();
        ArrayList<Card> objectiveCards = objectiveDeck.getCardList();

        ArrayList<StructuredObjectiveCard> doubleStructureObjectives = new ArrayList<>();

        for (Card c: objectiveCards){
            if (c.getIdCard() >= 91 && c.getIdCard() <= 94)
                doubleStructureObjectives.add((StructuredObjectiveCard) c);
        }
        assertEquals(4, doubleStructureObjectives.size());

        Collections.shuffle(doubleStructureObjectives);
        StructuredObjectiveCard cardToTest = doubleStructureObjectives.removeFirst();

        ArrayList<PlayableCard> greenCards = new ArrayList<>();
        ArrayList<PlayableCard> redCards = new ArrayList<>();
        ArrayList<PlayableCard> purpleCards = new ArrayList<>();
        ArrayList<PlayableCard> blueCards = new ArrayList<>();


        for (Card c: resourceDeck.getCardList()){
            if (Card.getType(c.getIdCard()) == Content.FUNGI_KINGDOM)
                redCards.add((PlayableCard) c);
            if (Card.getType(c.getIdCard()) == Content.INSECT_KINGDOM)
                purpleCards.add((PlayableCard) c);
            if (Card.getType(c.getIdCard()) == Content.ANIMAL_KINGDOM)
                blueCards.add((PlayableCard) c);
            if (Card.getType(c.getIdCard()) == Content.PLANT_KINGDOM)
                greenCards.add((PlayableCard) c);
        }

        for (Card c: goldDeck.getCardList()){
            if (Card.getType(c.getIdCard()) == Content.FUNGI_KINGDOM)
                redCards.add((PlayableCard) c);
            if (Card.getType(c.getIdCard()) == Content.INSECT_KINGDOM)
                purpleCards.add((PlayableCard) c);
            if (Card.getType(c.getIdCard()) == Content.ANIMAL_KINGDOM)
                blueCards.add((PlayableCard) c);
            if (Card.getType(c.getIdCard()) == Content.PLANT_KINGDOM)
                greenCards.add((PlayableCard) c);
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

        for (int i = 0; i < 6; i++){
            Random random = new Random();
            int x = random.nextInt(max-min + 1)+ min;
            int y = random.nextInt(max-min+1)+min;
            Coordinate coordinate1 = new Coordinate(x,y);
            if (grid.containsKey(coordinate1)) continue;

            Coordinate coordinate2 = null;
            Coordinate coordinate3 = null;
            PlayableCard card2 = null;
            PlayableCard card3 = null;
            PlayableCard card4 = null;

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
                case DOUBLE_DOWN_LEFT:{
                    coordinate2 = new Coordinate(x+1,y+1);
                    coordinate3 = new Coordinate(x+1, y+2);
                    card2 = purpleCards.removeFirst();
                    card3 = greenCards.removeFirst();
                    card4 = greenCards.removeFirst();
                }break;
                case DOUBLE_DOWN_RIGHT:{
                    coordinate2 = new Coordinate(x-1,y+1);
                    coordinate3= new Coordinate(x-1,y+2);
                    card2 = greenCards.removeFirst();
                    card3 = redCards.removeFirst();
                    card4 = redCards.removeFirst();
                }break;
            }

            assert card2 != null;
            assert card3 != null;
            assert card4 != null;

            if (!(grid.containsKey(coordinate1) || grid.containsKey(coordinate2) || grid.containsKey(coordinate3))) {
                grid.put(coordinate1, card2.getFront());
                grid.put(coordinate2, card3.getFront());
                grid.put(coordinate3, card4.getBack());
                structuresCreated++;
            }
        }

        int pointsGiven = cardToTest.verifyObjective(ground);

        assertEquals(structuresCreated*cardToTest.getPoints(),pointsGiven);

        for (Card c: objectiveCards){
            if (c.getIdCard() >= 91 && c.getIdCard() <= 94){
                StructuredObjectiveCard obj = (StructuredObjectiveCard) c;
                assertEquals(c.toString(), "StructuredObjectiveCard{" +
                        "structureType=" + obj.getStructureType()+
                        ", resourceRequested=" + obj.getResourceRequested() +
                        '}');
            }
        }



    }}