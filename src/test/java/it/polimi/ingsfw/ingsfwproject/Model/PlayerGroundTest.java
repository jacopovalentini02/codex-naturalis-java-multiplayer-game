package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.CardNotInHandException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlayerGroundTest {

    void playStarterCard(int numOfStarter, boolean upwards, HashMap<Content,Integer> contents){
        GameServerInstance gameServerInstance = new GameServerInstance();
        Player p1 = new Player("player1", gameServerInstance, 0);
        PlayerGround ground = p1.getGround();
        GameManager manager = new GameManager();
        Game game = new Game(gameServerInstance, manager, 1, 4, p1);
        game.setUpCards();

        //check if the only available position is (0,0)
        assertEquals(1, ground.getAvailablePositions().size());
        assertEquals(new Coordinate(0,0), ground.getAvailablePositions().getFirst());

        //check if all counters are set to 0
        for(Content c : Content.values())
            assertEquals(0, ground.getContentCount(c));

        //gettin the starter to play
        final StarterCard starter = (StarterCard) game.getStarterDeck().getCardList().get(numOfStarter);

//        //at this time, the card is not in the player's hand
//        assertThrows(CardNotInHandException.class,
//                () ->{
//                    ground.playCard(starter, upwards, new Coordinate(0,0));
//                });
//
//        //adding the card to player's hand
//        p1.getHandCard().add(starter);

        //a StarterCard can't be placed in other places then (0,0)
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(starter, upwards, new Coordinate(1,0));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(starter, upwards, new Coordinate(2,3));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(starter, upwards, new Coordinate(-1,0));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(starter, upwards, new Coordinate(-7,4));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(starter, upwards, new Coordinate(-11,-2));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(starter, upwards, new Coordinate(1,-6));
                });


        //now he can place it
        assertDoesNotThrow(
                () ->{
                    ground.playCard(starter, upwards, new Coordinate(0,0));
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
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(1, 1)));
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(1, -1)));
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(-1, 1)));
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(-1, -1)));
        }else if(starter.getIdCard() == 85 || starter.getIdCard() ==86) {
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(1, 1)));
            assertTrue(ground.getAvailablePositions().contains(new Coordinate(-1, 1)));
        }
    }

    @Test
    void playFirstStarterCard() {
        HashMap<Content,Integer> contents = new HashMap<>();
        contents.put(Content.INSECT_KINGDOM, 2);
        contents.put(Content.PLANT_KINGDOM, 1);

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

        for(int i = 0; i <6; i++)
            playStarterCard(i, false, contents);
    }

    @Test
    void playResourceCard(){
        GameServerInstance gameServerInstance = new GameServerInstance();
        Player p1 = new Player("player1", gameServerInstance, 0);
        PlayerGround ground = p1.getGround();
        GameManager manager = new GameManager();
        Game game = new Game(gameServerInstance, manager, 1, 4, p1);
        game.setUpCards();

        //gettin the FIRST starter to play (IDCARD = 81)
        final StarterCard starter = (StarterCard) game.getStarterDeck().getCardList().getFirst();

//        //adding the card to player's hand
//        p1.getHandCard().add(starter);

        //now he can place it UPWARDS
        try {
            ground.playCard(starter, true, new Coordinate(0,0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //gettin the FIRST resource
        ResourceCard resource = (ResourceCard) game.getResourceDeck().getCardList().getFirst();
//        p1.getHandCard().add(resource);

        //the resource can't be placed in a position that is not contained in availablePosition
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource, true, new Coordinate(2,0));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource, true, new Coordinate(-3,0));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource, true, new Coordinate(2,2));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource, true, new Coordinate(-5,-3));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource, true, new Coordinate(0,7));
                });
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource, true, new Coordinate(0,-9));
                });

        //now it can be placed in (1,1)
        assertDoesNotThrow(
                () ->{
                    int points = ground.playCard(resource, true, new Coordinate(1,1));
                    game.updatePoints(points, p1);
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

        //gettin the resource card with ID = 15 (it is in position 14)
        ResourceCard resource2 = (ResourceCard) game.getResourceDeck().getCardList().get(14);
        assertEquals(15, resource2.getIdCard());

        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource2, true, new Coordinate(2,0));
                });

        //placing it in (2,2) DOWNWARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(resource2, false, new Coordinate(2,2));
                    game.updatePoints(score, p1);
                });

        //this card does not give points
        assertEquals(0, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(2, ground.getContentCount(Content.FUNGI_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.PLANT_KINGDOM)); //this one is the only one that should change
        assertEquals(0, ground.getContentCount(Content.ANIMAL_KINGDOM));
        assertEquals(0, ground.getContentCount(Content.QUILL));
        assertEquals(0, ground.getContentCount(Content.INKWELL));
        assertEquals(0, ground.getContentCount(Content.MANUSCRIPT));


        ResourceCard resource3 = (ResourceCard) game.getResourceDeck().getCardList().get(21);
        assertEquals(22, resource3.getIdCard());

        //placing it in (-1,-1) UPWARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(resource3, true, new Coordinate(-1,-1));
                    game.updatePoints(score, p1);
                });

        //this card does not give points
        assertEquals(0, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(2, ground.getContentCount(Content.FUNGI_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.ANIMAL_KINGDOM)); //this one is the only one that should change
        assertEquals(0, ground.getContentCount(Content.QUILL));
        assertEquals(0, ground.getContentCount(Content.INKWELL));
        assertEquals(0, ground.getContentCount(Content.MANUSCRIPT));


        GoldCard gold = (GoldCard) game.getGoldDeck().getCardList().get(11);
        assertEquals(52, gold.getIdCard());

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold, true, new Coordinate(2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold, true, new Coordinate(-2,0));
                });

        //not enough resource
        assertThrows(NotEnoughResourcesException.class,
                () ->{
                    ground.playCard(gold, true, new Coordinate(0,2));
                });

        //placing it in (0,2) DOWNWARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(gold, false, new Coordinate(0,2));
                    game.updatePoints(score, p1);
                });

        //this card backwards does not give points
        assertEquals(0, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(1, ground.getContentCount(Content.FUNGI_KINGDOM)); // one covered
        assertEquals(1, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.PLANT_KINGDOM));  //this one is the only one that should change
        assertEquals(2, ground.getContentCount(Content.ANIMAL_KINGDOM));
        assertEquals(0, ground.getContentCount(Content.QUILL));
        assertEquals(0, ground.getContentCount(Content.INKWELL));
        assertEquals(0, ground.getContentCount(Content.MANUSCRIPT));


        GoldCard gold2 = (GoldCard) game.getGoldDeck().getCardList().get(10);
        assertEquals(51, gold2.getIdCard());

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold2, true, new Coordinate(2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold2, true, new Coordinate(-2,0));
                });


        //placing it in (1,-1) UPWARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(gold2, true, new Coordinate(1,-1));
                    game.updatePoints(score, p1);
                });

        //this card should give only 1 point (only one quill)
        assertEquals(1, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(1, ground.getContentCount(Content.FUNGI_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.ANIMAL_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.QUILL)); //this one is the only one that should change
        assertEquals(0, ground.getContentCount(Content.INKWELL));
        assertEquals(0, ground.getContentCount(Content.MANUSCRIPT));



        ResourceCard resource4 = (ResourceCard) game.getResourceDeck().getCardList().get(35);
        assertEquals(36, resource4.getIdCard());

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource4, true, new Coordinate(2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource4, true, new Coordinate(-2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource4, true, new Coordinate(5,3));
                });

        //placing it in (0,-2) UPWARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(resource4, true, new Coordinate(0,-2));
                    game.updatePoints(score, p1);
                });

        //this card does not give points
        assertEquals(1, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(2, ground.getContentCount(Content.FUNGI_KINGDOM)); //one added
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM)); //one added
        assertEquals(2, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.ANIMAL_KINGDOM)); //one animal covered
        assertEquals(1, ground.getContentCount(Content.QUILL));
        assertEquals(0, ground.getContentCount(Content.INKWELL));
        assertEquals(1, ground.getContentCount(Content.MANUSCRIPT)); //one added



        ResourceCard resource5 = (ResourceCard) game.getResourceDeck().getCardList().get(1);
        assertEquals(2, resource5.getIdCard());

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource5, false, new Coordinate(2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource5, false, new Coordinate(-2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource5, false, new Coordinate(-1,-3));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource5, false, new Coordinate(0,4));
                });

        //placing it in (-1,3) DOWNWARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(resource5, false, new Coordinate(-1,3));
                    game.updatePoints(score, p1);
                });

        //this card does not give points
        assertEquals(1, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(3, ground.getContentCount(Content.FUNGI_KINGDOM)); //one added
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM)); //one added
        assertEquals(2, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.ANIMAL_KINGDOM)); //one animal covered
        assertEquals(1, ground.getContentCount(Content.QUILL));
        assertEquals(0, ground.getContentCount(Content.INKWELL));
        assertEquals(1, ground.getContentCount(Content.MANUSCRIPT)); //one added



        ResourceCard resource6 = (ResourceCard) game.getResourceDeck().getCardList().get(5);
        assertEquals(6, resource6.getIdCard());

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource6, true, new Coordinate(2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource6, true, new Coordinate(-2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(resource6, true, new Coordinate(-1,-3));
                });

        //placing it in (0,4) UPWARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(resource6, true, new Coordinate(0,4));
                    game.updatePoints(score, p1);
                });

        //this card does not give points
        assertEquals(1, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(4, ground.getContentCount(Content.FUNGI_KINGDOM)); //one added
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.ANIMAL_KINGDOM)); //one added
        assertEquals(1, ground.getContentCount(Content.QUILL));
        assertEquals(1, ground.getContentCount(Content.INKWELL)); //one added
        assertEquals(1, ground.getContentCount(Content.MANUSCRIPT));




        GoldCard gold3 = (GoldCard) game.getGoldDeck().getCardList().get(5);
        assertEquals(46, gold3.getIdCard());

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold3, true, new Coordinate(2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold3, true, new Coordinate(-2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold3, true, new Coordinate(-1,-3));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold3, true, new Coordinate(0,4));
                });

        //placing it in (1,3) UPWARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(gold3, true, new Coordinate(1,3));
                    game.updatePoints(score, p1);
                });

        //this card should give 6 points ( 2 for each corner)
        assertEquals(7, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(4, ground.getContentCount(Content.FUNGI_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.ANIMAL_KINGDOM)); //one covered
        assertEquals(1, ground.getContentCount(Content.QUILL));
        assertEquals(1, ground.getContentCount(Content.INKWELL));
        assertEquals(1, ground.getContentCount(Content.MANUSCRIPT));




        ResourceCard resource7 = (ResourceCard) game.getResourceDeck().getCardList().get(9);
        assertEquals(10, resource7.getIdCard());

        //placing it in (0,4) UPWARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(resource7, true, new Coordinate(3,1));
                    game.updatePoints(score, p1);
                });

        //this card give 1 point
        assertEquals(8, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(5, ground.getContentCount(Content.FUNGI_KINGDOM)); //one added
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.ANIMAL_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.QUILL));
        assertEquals(1, ground.getContentCount(Content.INKWELL));
        assertEquals(1, ground.getContentCount(Content.MANUSCRIPT));



        GoldCard gold4 = (GoldCard) game.getGoldDeck().getCardList().get(9);
        assertEquals(50, gold4.getIdCard());

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold4, true, new Coordinate(2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold4, true, new Coordinate(-2,0));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold4, true, new Coordinate(-1,-3));
                });

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold4, true, new Coordinate(0,4));
                });

        //placing it in (1,3) UPWARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(gold4, true, new Coordinate(1,-3));
                    game.updatePoints(score, p1);
                });


        //this card give 1 point
        assertEquals(13, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(4, ground.getContentCount(Content.FUNGI_KINGDOM)); //one added
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.ANIMAL_KINGDOM));
        assertEquals(1, ground.getContentCount(Content.QUILL));
        assertEquals(1, ground.getContentCount(Content.INKWELL));
        assertEquals(1, ground.getContentCount(Content.MANUSCRIPT));


        GoldCard gold5 = (GoldCard) game.getGoldDeck().getCardList().get(22);
        assertEquals(63, gold5.getIdCard());

        //the position is not available
        assertThrows(PositionNotAvailableException.class,
                () ->{
                    ground.playCard(gold5, true, new Coordinate(-1,-3));
                });

        //not enough resource
        assertThrows(NotEnoughResourcesException.class,
                () ->{
                    ground.playCard(gold5, true, new Coordinate(0,-4));
                });


        //placing it in (0,-4) DOWNQARDS
        assertDoesNotThrow(
                () ->{
                    int score = ground.playCard(gold5, false, new Coordinate(0,-4));
                    game.updatePoints(score, p1);
                });


        //this card give no points backwards
        assertEquals(13, game.getScores().get(p1));

        //check for the contents counters
        assertEquals(4, ground.getContentCount(Content.FUNGI_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.INSECT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.PLANT_KINGDOM));
        assertEquals(2, ground.getContentCount(Content.ANIMAL_KINGDOM));  //one added
        assertEquals(1, ground.getContentCount(Content.QUILL));
        assertEquals(1, ground.getContentCount(Content.INKWELL));
        assertEquals(1, ground.getContentCount(Content.MANUSCRIPT));



    }

}