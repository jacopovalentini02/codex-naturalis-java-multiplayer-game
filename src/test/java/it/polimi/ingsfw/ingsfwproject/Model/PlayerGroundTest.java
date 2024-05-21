package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.CardNotInHandException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network2.RMIClient;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlayerGroundTest {

    void playStarterCard(int numOfStarter, boolean upwards, HashMap<Content,Integer> contents){
        Player p1 = new Player("player1");
        PlayerGround ground = p1.getGround();
        GameManager manager = new GameManager();
        Game game = new Game(manager, 1, 4, p1);
        game.setUpCards();

        //check if the only available position is (0,0)
        assertEquals(1, ground.getAvailablePositions().size());
        assertEquals(new Coordinate(0,0), ground.getAvailablePositions().getFirst());

        //check if all counters are set to 0
        for(Content c : Content.values())
            assertEquals(0, ground.getContentCount(c));

        //gettin the starter to play
        final StarterCard starter = (StarterCard) game.getStarterDeck().getCardList().get(numOfStarter);

        //at this time, the card is not in the player's hand
        assertThrows(CardNotInHandException.class,
                () ->{
                    p1.playCard(starter, upwards, new Coordinate(0,0));
                });

        //adding the card to player's hand
        p1.getHandCard().add(starter);

        //a StarterCard can't be placed in other places then (0,0)
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(starter, upwards, new Coordinate(1,0));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(starter, upwards, new Coordinate(2,3));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(starter, upwards, new Coordinate(-1,0));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(starter, upwards, new Coordinate(-7,4));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(starter, upwards, new Coordinate(-11,-2));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(starter, upwards, new Coordinate(1,-6));
                });


        //now he can place it
        assertDoesNotThrow(
                () ->{
                    p1.playCard(starter, upwards, new Coordinate(0,0));
                });

        //check for the contents counters

        for(Content c : Content.values()){
            if(contents.containsKey(c))
                assertEquals(contents.get(c), ground.getContentCount(c));
            else
                assertEquals(0, ground.getContentCount(c));
        }

        //check availablePositions

        //Check if (0,0) is no more an availablePosition
        assertFalse(ground.getAvailablePositions().contains(new Coordinate(0,0)));

        //check for the others availablePositions
        if(starter.getIdCard() >=81 && starter.getIdCard() <=84) {
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(0, 1)));
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(0, -1)));
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(1, 0)));
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(-1, 0)));
        }else if(starter.getIdCard() == 85 || starter.getIdCard() ==86) {
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(0, 1)));
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(1, 0)));
        }
    }

    @Test
    void playFirstStarterCard() {
        HashMap<Content,Integer> contents = new HashMap<>();
        contents.put(Content.INSECT_KINGDOM, 2);
        contents.put(Content.ANIMAL_KINGDOM, 1);

        playStarterCard(0,true,contents);
    }

    @Test
    void playSecondStarterCard(){
        HashMap<Content,Integer> contents = new HashMap<>();
        contents.put(Content.FUNGI_KINGDOM, 2);
        contents.put(Content.ANIMAL_KINGDOM, 1);

        playStarterCard(1,true,contents);
    }

    @Test
    void playThirdStarterCard(){
        HashMap<Content,Integer> contents = new HashMap<>();
        contents.put(Content.FUNGI_KINGDOM, 1);
        contents.put(Content.PLANT_KINGDOM, 1);

        playStarterCard(2,true,contents);
    }

    @Test
    void playFourthStarterCard(){
        HashMap<Content,Integer> contents = new HashMap<>();
        contents.put(Content.ANIMAL_KINGDOM, 1);
        contents.put(Content.INSECT_KINGDOM, 1);

        playStarterCard(3,true, contents);
    }

    @Test
    void playFifthStarterCard(){
        HashMap<Content,Integer> contents = new HashMap<>();
        contents.put(Content.ANIMAL_KINGDOM, 1);
        contents.put(Content.INSECT_KINGDOM, 1);
        contents.put(Content.PLANT_KINGDOM, 1);

        playStarterCard(4,true,contents);
    }

    @Test
    void playSixthStarterCard(){
        HashMap<Content,Integer> contents = new HashMap<>();
        contents.put(Content.ANIMAL_KINGDOM, 1);
        contents.put(Content.FUNGI_KINGDOM, 1);
        contents.put(Content.PLANT_KINGDOM, 1);

        playStarterCard(5,true,contents);
    }

    @Test
    void playStarterCardBack(){
        HashMap<Content,Integer> contents = new HashMap<>();
        contents.put(Content.ANIMAL_KINGDOM, 1);
        contents.put(Content.FUNGI_KINGDOM, 1);
        contents.put(Content.PLANT_KINGDOM, 1);
        contents.put(Content.INSECT_KINGDOM, 1);

        for(int i = 1; i <=6; i++)
            playStarterCard(i, false, contents);
    }

    void playResourceCard(){
        Player p1 = new Player("player1");
        PlayerGround ground = p1.getGround();
        GameManager manager = new GameManager();
        Game game = new Game(manager, 1, 4, p1);
        game.setUpCards();

        //gettin the FIRST starter to play (IDCARD = 81)
        final StarterCard starter = (StarterCard) game.getStarterDeck().getCardList().getFirst();

        //adding the card to player's hand
        p1.getHandCard().add(starter);

        //now he can place it UPWARDS
        try {
            p1.playCard(starter, true, new Coordinate(0,0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //gettin the FIRST resource
        ResourceCard resource = (ResourceCard) game.getResourceDeck().getCardList().getFirst();
        p1.getHandCard().add(resource);

        //the resource can't be placed in a position that is not contained in availablePosition
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(resource, true, new Coordinate(2,0));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(resource, true, new Coordinate(-3,0));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(resource, true, new Coordinate(2,2));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(resource, true, new Coordinate(-5,-3));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(resource, true, new Coordinate(0,7));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    p1.playCard(resource, true, new Coordinate(0,-9));
                });

        //now it can be placed in (1,1)
        assertDoesNotThrow(
                () ->{
                    p1.playCard(resource, true, new Coordinate(1,1));
                });

        //this card does not give points
        assertEquals(0, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(2, ground.getContentCount(Content.FUNGI_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(0, ground.getContentCount(Content.PLANT_KINGDOM)); // the TR corner in the starter is gettin covered by resource placed in (1,0)
        assertEquals(0, ground.getContentCount(Content.ANIMAL_KINGDOM));
        assertEquals(0, ground.getContentCount(Content.QUILL));
        assertEquals(0, ground.getContentCount(Content.INKWELL));
        assertEquals(0, ground.getContentCount(Content.MANUSCRIPT));

        //gettin the resource card with ID = 15
        ResourceCard resource2 = (ResourceCard) game.getResourceDeck().getCardList().get(15);
        //TODO: controllare se la 15 ritorna la carta con id = 15 (memo: ho tolto la prima carta)
        assertEquals(15, resource2.getIdCard());

        //placing it in (2,2) DOWNWARDS
        assertDoesNotThrow(
                () ->{
                    p1.playCard(resource2, false, new Coordinate(2,2));
                });

        //check for the contents counters
        assertEquals(2, ground.getContentCount(Content.FUNGI_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.PLANT_KINGDOM)); //this one is the only one that should change
        assertEquals(0, ground.getContentCount(Content.ANIMAL_KINGDOM));
        assertEquals(0, ground.getContentCount(Content.QUILL));
        assertEquals(0, ground.getContentCount(Content.INKWELL));
        assertEquals(0, ground.getContentCount(Content.MANUSCRIPT));


        ResourceCard resource3 = (ResourceCard) game.getResourceDeck().getCardList().get(22);
        //TODO: controllare se la 22 ritorna la carta con id = 22 (memo: ho tolto la prima carta)
        assertEquals(22, resource2.getIdCard());

        //placing it in (2,2) DOWNWARDS
        assertDoesNotThrow(
                () ->{
                    p1.playCard(resource3, true, new Coordinate(-1,-1));
                });

        //check for the contents counters
        assertEquals(2, ground.getContentCount(Content.FUNGI_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.ANIMAL_KINGDOM)); //this one is the only one that should change
        assertEquals(0, ground.getContentCount(Content.QUILL));
        assertEquals(0, ground.getContentCount(Content.INKWELL));
        assertEquals(0, ground.getContentCount(Content.MANUSCRIPT));


    }




    @Test
    void calculatePoints() {
    }
}