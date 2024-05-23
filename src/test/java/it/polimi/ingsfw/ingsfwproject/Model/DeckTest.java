package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    @Test
    //Check the creation of a resource card with a JSONObject
    public void testCreateResourceCard(){
        Deck deck = new Deck();

        JSONObject cardObject = new JSONObject();
        cardObject.put("center", "FUNGI_KINGDOM");
        cardObject.put("points", 0);
        JSONArray cornersArray = new JSONArray().put("FUNGI_KINGDOM").put("EMPTY").put("FUNGI_KINGDOM").put("HIDDEN");
        cardObject.put("corners", cornersArray);
        int id = 1;

        deck.createResourceCard(cardObject, id);

        assertEquals(1, deck.getCardList().size()); // Check the card was added to the deck

        ResourceCard addedCard = (ResourceCard) deck.getCardList().getFirst();

        assertEquals(1,  addedCard.getFront().getIdCard());
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getBackface().getCenter());
        assertEquals(0, addedCard.getFront().getPoints());
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCornerList()[0]);
        assertEquals(Content.EMPTY,addedCard.getFront().getCornerList()[1]);
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getFront().getCornerList()[2]);
        assertEquals(Content.HIDDEN, addedCard.getFront().getCornerList()[3]);

    }

    @Test
        //Test the creation of the first gold card
    public void testCreateGoldCard(){
        Deck deck = new Deck();

        JSONObject cardObject = new JSONObject();
        cardObject.put("center", "FUNGI_KINGDOM");
        cardObject.put("points", 1);
        JSONArray costArray = new JSONArray().put("FUNGI_KINGDOM").put("FUNGI_KINGDOM").put("ANIMAL_KINGDOM");
        cardObject.put("cost", costArray);
        cardObject.put("objectNeeded", "QUILL");
        cardObject.put("overlapped", false);
        JSONArray corner = new JSONArray().put("HIDDEN").put("EMPTY").put("EMPTY").put("QUILL");
        cardObject.put("corners", corner);

        deck.createGoldCard(cardObject, 41);
        GoldCard addedCard=(GoldCard) deck.getCardList().getFirst();

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
    public void testCreateStarter(){
        Deck deck=new Deck();
        JSONObject cardObject = new JSONObject();
        JSONArray centerArray = new JSONArray().put("INSECT_KINGDOM");
        cardObject.put("center", centerArray);
        JSONArray cornerBackArray = new JSONArray().put("FUNGI_KINGDOM").put("PLANT_KINGDOM").put("INSECT_KINGDOM").put("ANIMAL_KINGDOM");
        cardObject.put("cornerBack", cornerBackArray);
        JSONArray cornerFrontArray = new JSONArray().put("EMPTY").put("PLANT_KINGDOM").put("INSECT_KINGDOM").put("EMPTY");
        cardObject.put("cornerFront", cornerFrontArray);

        deck.createStarterCard(cardObject, 81);
        StarterCard addedCard = (StarterCard) deck.getCardList().getFirst();

        assertEquals(81,  addedCard.getFront().getIdCard());
        assertEquals(1, addedCard.getFront().getCenter().size());
        assertEquals(Content.INSECT_KINGDOM, addedCard.getFront().getCenter().get(0));
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
    public void testCreateStructObjective(){
        JSONObject cardObject = new JSONObject();
        cardObject.put("points", 2);
        cardObject.put("structure", "LEFT_DIAGONAL");
        JSONArray resourceArray = new JSONArray().put("FUNGI_KINGDOM").put("FUNGI_KINGDOM").put("FUNGI_KINGDOM");
        cardObject.put("resource", resourceArray);

        Deck deck = new Deck();
        deck.createStructObjective(cardObject, 87);
        StructuredObjectiveCard addedCard = (StructuredObjectiveCard) deck.getCardList().getFirst();

        assertEquals(87,  addedCard.getIdCard());
        assertEquals(3, addedCard.getResourceRequested().size());
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getResourceRequested().get(0));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getResourceRequested().get(1));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getResourceRequested().get(2));
        assertEquals(2, addedCard.getPoints());
        assertEquals(Structure.LEFT_DIAGONAL, addedCard.getStructureType());


    }

    @Test
    public void  testCreateNotStructObjective(){
        JSONObject cardObject = new JSONObject();
        cardObject.put("points", 2);
        JSONArray resourceArray = new JSONArray().put("FUNGI_KINGDOM").put("FUNGI_KINGDOM").put("FUNGI_KINGDOM");
        cardObject.put("resource", resourceArray);

        Deck deck = new Deck();
        deck.createNotStructObjective(cardObject, 95);
        NotStructuredObjectiveCard addedCard = (NotStructuredObjectiveCard) deck.getCardList().getFirst();

        assertEquals(95, addedCard.getIdCard());
        assertEquals(3, addedCard.getObjectRequested().size());
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(0));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(1));
        assertEquals(Content.FUNGI_KINGDOM, addedCard.getObjectRequested().get(2));
        assertEquals(2, addedCard.getPoints());


    }

    @Test
    public void testDraw() throws DeckEmptyException {
        Deck deck=new Deck();
        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
        //Creation of the first card
        Content[] corner = { Content.FUNGI_KINGDOM, Content.EMPTY, Content.FUNGI_KINGDOM, Content.HIDDEN };
        NormalBack backFace=new NormalBack(1, emptyCorners, Content.FUNGI_KINGDOM, "");
        NormalFace front=new NormalFace(1, 0, corner,"");
        ResourceCard preRes = new ResourceCard(front,backFace,1);
        deck.addCard(preRes);

        //Creation of the second card
        corner = new Content[]{Content.FUNGI_KINGDOM, Content.FUNGI_KINGDOM, Content.HIDDEN, Content.EMPTY};
        backFace = new NormalBack(2, emptyCorners, Content.FUNGI_KINGDOM,"");
        front=new NormalFace(2, 0, corner,"");
        ResourceCard preRes2 = new ResourceCard(front, backFace, 2);
        deck.addCard(preRes2);


        //Creation of the third card
        corner = new Content[]{Content.PLANT_KINGDOM, Content.PLANT_KINGDOM, Content.HIDDEN, Content.EMPTY};
        backFace = new NormalBack(12, emptyCorners, Content.FUNGI_KINGDOM,"");
        front=new NormalFace(12, 0, corner,"");
        ResourceCard preRes3 = new ResourceCard(front, backFace, 12);
        deck.addCard(preRes3);

        Card drawnCard = deck.draw();

        //First card in the deck should be the second added
        assertEquals(deck.getCardList().getFirst(), preRes2);
        //The drawn card should be not in the deck
        assertFalse(deck.getCardList().contains(drawnCard));

        //If the deck is empty an exception should be thrown
        deck.setCardList(new ArrayList<>());
        assertThrows(DeckEmptyException.class, deck::draw);

    }
}