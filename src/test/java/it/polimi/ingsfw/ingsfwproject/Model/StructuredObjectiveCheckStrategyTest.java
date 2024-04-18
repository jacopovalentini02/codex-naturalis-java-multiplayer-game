package it.polimi.ingsfw.ingsfwproject.Model;

import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StructuredObjectiveCheckStrategyTest {


    @Test
    void leftDiagonalSearch() throws RemoteException {
        Player player1=new Player("user1");
        Game game=new Game(1,2,player1);
        game.setUpCards();
        Deck resourceDeck = game.getResourceDeck();
        Deck goldDeck = game.getGoldDeck();
        Deck starterDeck = game.getStarterDeck();
        Map<Coordinate, Face> grid = new HashMap<>();
        StarterCard card1 = (StarterCard) starterDeck.draw();

        Coordinate zero = new Coordinate(0,0);
        grid.put(zero, card1.getBack());

        assertEquals(1, grid.size());
        assertEquals(card1.getBack(), grid.get(new Coordinate(0,0)));
        StructuredObjectiveCheckStrategy strategy = new StructuredObjectiveCheckStrategy();
        ArrayList<Content> list = new ArrayList<>();
        list.add(Content.FUNGI_KINGDOM);
        list.add(Content.FUNGI_KINGDOM);
        list.add(Content.FUNGI_KINGDOM);

        int diagonals = strategy.leftDiagonalSearch(list, grid);
        assertEquals(0, diagonals);

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
        Coordinate due = new Coordinate(-1,1);
        Coordinate tre = new Coordinate(-2,2);
        Coordinate quattro = new Coordinate(-3,3);
        grid.put(due, card2.getFront());
        grid.put(tre, card3.getBack());grid.put(quattro, card4.getFront());

       diagonals = strategy.leftDiagonalSearch(list, grid);
       assertEquals(1, diagonals);

       Coordinate cinque = new Coordinate(-4,4);
       grid.put(cinque, card5.getFront());
       diagonals = strategy.leftDiagonalSearch(list, grid);
       assertEquals(1, diagonals);

       Coordinate sei = new Coordinate(-5,5);
       Coordinate sette = new Coordinate(-6, 6);
       grid.put(sei, card6.getBack());
       grid.put(sette, card7.getFront());
       diagonals = strategy.leftDiagonalSearch(list, grid);
       assertEquals(2, diagonals);

        ResourceCard card8 = (ResourceCard) resourceDeck.draw();
        ResourceCard card9 = (ResourceCard) resourceDeck.draw();
        ResourceCard card10 = (ResourceCard) resourceDeck.draw();
        Coordinate otto = new Coordinate(5,6);
        Coordinate nove = new Coordinate(4,7);
        Coordinate dieci = new Coordinate(3,8);
        grid.put(otto, card8.getBack());
        grid.put(nove, card9.getFront());
        grid.put(dieci, card10.getFront());
        diagonals = strategy.leftDiagonalSearch(list, grid);
        assertEquals(3, diagonals);


    }
}