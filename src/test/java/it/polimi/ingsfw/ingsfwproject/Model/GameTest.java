package it.polimi.ingsfw.ingsfwproject.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    //Check the size of each deck
    public void testSetUpCards() {
        Game game=new Game();
        game.setUpCards();

        assertEquals(40, game.getResourceDeck().getCardList().size());
        assertEquals(40, game.getGoldDeck().getCardList().size());
        assertEquals(6, game.getStarterDeck().getCardList().size());
        assertEquals(16, game.getObjectiveDeck().getCardList().size());

    }

//    @Test
//    //Check resource cards' deck attributes are set up correctly
//    public void testSetUpResourceDeck(){
//        Game game=new Game();
//        game.setUpCards();
//
//        ResourceCard addedCard = (ResourceCard) game.getResourceDeck().getCardList().getFirst();
//
//        assertEquals(1,  addedCard.getFront().getIdCard());
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getBackface().getCenter());
//        assertEquals(0, addedCard.getFront().getPoints());
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCornerList()[0]);
//        assertEquals(Content.EMPTY,addedCard.getFront().getCornerList()[1]);
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCornerList()[2]);
//        assertEquals(Content.HIDDEN, addedCard.getFront().getCornerList()[3]);
//
//    }
//    @Test
//    //Check gold cards' deck attributes are set up correctly
//    public void testSetUpGoldDeck(){
//
//        Game game=new Game();
//        game.setUpCards();
//
//        GoldCard addedCard=  (GoldCard) game.getGoldDeck().getCardList().getFirst();
//
//        assertEquals(41,  addedCard.getFront().getIdCard());
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getBackface().getCenter());
//        assertEquals(1, addedCard.getFront().getPoints());
//        assertEquals(3, addedCard.getFront().getCost().size()); //Check the right length
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCost().get(0));
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCost().get(1));
//        assertEquals(Content.ANIMAL_KINGDOM, addedCard.getFront().getCost().get(2));
//        assertEquals(Content.QUILL, addedCard.getFront().getObjectNeeded());
//        assertFalse(addedCard.getFront().isOverlapped());
//        assertEquals(Content.HIDDEN, addedCard.getFront().getCornerList()[0]);
//        assertEquals(Content.EMPTY, addedCard.getFront().getCornerList()[1]);
//        assertEquals(Content.EMPTY, addedCard.getFront().getCornerList()[2]);
//        assertEquals(Content.QUILL, addedCard.getFront().getCornerList()[3]);
//
//    }
//
//    @Test
//    //Check starter's deck attributes are set up correctly
//    public void testSetUpStarterDeck(){
//        Game game=new Game();
//        game.setUpCards();
//
//        StarterCard addedCard=  (StarterCard) game.getStarterDeck().getCardList().getFirst();
//        assertEquals(81,  addedCard.getFront().getIdCard());
//        assertEquals(1, addedCard.getBack().getCenter().size());
//        assertEquals(Content.INSECT_KINGDOM, addedCard.getBack().getCenter().getFirst());
//        assertEquals(4, addedCard.getFront().getCornerList().length);
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCornerList()[0]);
//        assertEquals(Content.PLANT_KINGDOM, addedCard.getFront().getCornerList()[1]);
//        assertEquals(Content.INSECT_KINGDOM, addedCard.getFront().getCornerList()[2]);
//        assertEquals(Content.ANIMAL_KINGDOM, addedCard.getFront().getCornerList()[3]);
//        assertEquals(4, addedCard.getBack().getCornerList().length);
//        assertEquals(Content.EMPTY, addedCard.getBack().getCornerList()[0]);
//        assertEquals(Content.PLANT_KINGDOM, addedCard.getBack().getCornerList()[1]);
//        assertEquals(Content.INSECT_KINGDOM, addedCard.getBack().getCornerList()[2]);
//        assertEquals(Content.EMPTY, addedCard.getBack().getCornerList()[3]);
//
//    }
//
//    @Test
//    //Check objective cards' deck attributes are set up correctly
//    public void testSetUpStructObjectiveDeck(){
//        Game game=new Game();
//        game.setUpCards();
//
//        StructuredObjectiveCard addedCard=  (StructuredObjectiveCard) game.getObjectiveDeck().getCardList().get(0);
//
//        assertEquals(87,  addedCard.getIdCard());
//        assertEquals(3, addedCard.getResourceRequested().size());
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getResourceRequested().get(0));
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getResourceRequested().get(1));
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getResourceRequested().get(2));
//        assertEquals(2, addedCard.getPoints());
//        assertEquals(Structure.LEFT_DIAGONAL, addedCard.getStructureType());
//
//
//    }
//
//    @Test
//    //Check objective cards' deck attributes are set up correctly
//    public void  testSetUpNotStructObjectiveDeck(){
//        Game game=new Game();
//        game.setUpCards();
//
//        NotStructuredObjectiveCard addedCard=  (NotStructuredObjectiveCard) game.getObjectiveDeck().getCardList().get(8);
//        assertEquals(95, addedCard.getIdCard());
//        assertEquals(3, addedCard.getObjectRequested().size());
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(0));
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(1));
//        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(2));
//        assertEquals(2, addedCard.getPoints());
//
//
//    }

}