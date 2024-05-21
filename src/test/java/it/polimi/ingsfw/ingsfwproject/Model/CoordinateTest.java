package it.polimi.ingsfw.ingsfwproject.Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void getPositiveX() {
        Coordinate c1 = new Coordinate(1, 2);
        assertEquals(1, c1.getX());

        Coordinate c2 = new Coordinate(3, 0);
        assertEquals(3, c2.getX());

        Coordinate c3 = new Coordinate(19, -6);
        assertEquals(19, c3.getX());

        for(int i = 0; i< 90; i++){
            Coordinate c = new Coordinate(i, 0);
            assertEquals(i, c.getX());
        }
    }

    @Test
    void getNegativeX() {
        Coordinate c1 = new Coordinate(-1, 2);
        assertEquals(-1, c1.getX());

        Coordinate c2 = new Coordinate(-3, 0);
        assertEquals(-3, c2.getX());

        Coordinate c3 = new Coordinate(-19, -6);
        assertEquals(-19, c3.getX());

        for(int i = -90; i<0; i++){
            Coordinate c = new Coordinate(i, 0);
            assertEquals(i, c.getX());
        }
    }
    @Test
    void getZeroX(){
        Coordinate c1 = new Coordinate(0, 0);
        assertEquals(0, c1.getX());

        Coordinate c2 = new Coordinate(0, 2);
        assertEquals(0, c2.getX());

        Coordinate c3 = new Coordinate(0, -6);
        assertEquals(0, c3.getX());

        for(int i = -90; i<90; i++){
            Coordinate c = new Coordinate(0, i);
            assertEquals(0, c.getX());
        }
    }

    @Test
    void getPositiveY() {
        Coordinate c1 = new Coordinate(6, 1);
        assertEquals(1, c1.getY());

        Coordinate c2 = new Coordinate(-12, 3);
        assertEquals(3, c2.getY());

        Coordinate c3 = new Coordinate(0, 19);
        assertEquals(19, c3.getY());

        for(int i = 0; i< 90; i++){
            Coordinate c = new Coordinate(0, i);
            assertEquals(i, c.getY());
        }
    }

    @Test
    void getNegativeY() {
        Coordinate c1 = new Coordinate(2, -1);
        assertEquals(-1, c1.getY());

        Coordinate c2 = new Coordinate(0, -3);
        assertEquals(-3, c2.getY());

        Coordinate c3 = new Coordinate(-6, -19);
        assertEquals(-19, c3.getY());

        for(int i = -90; i<0; i++){
            Coordinate c = new Coordinate(0, i);
            assertEquals(i, c.getY());
        }
    }

    @Test
    void getZeroY(){
        Coordinate c1 = new Coordinate(0, 0);
        assertEquals(0, c1.getY());

        Coordinate c2 = new Coordinate(2, 0);
        assertEquals(0, c2.getY());

        Coordinate c3 = new Coordinate(-6, 0);
        assertEquals(0, c3.getY());

        for(int i = -90; i<90; i++){
            Coordinate c = new Coordinate(i, 0);
            assertEquals(0, c.getY());
        }
    }

    @Test
    void testNotEquals() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(3, 0);
        assertNotEquals(c1, c2);

        Coordinate c3 = new Coordinate(-1, 2);
        Coordinate c4 = new Coordinate(-1, 0);
        assertNotEquals(c3, c4);

        Coordinate c5 = new Coordinate(-1, -3);
        Coordinate c6 = new Coordinate(0, -3);
        assertNotEquals(c5, c6);

        Coordinate c7 = new Coordinate(2, -6);
        Coordinate c8 = new Coordinate(2, 10);
        assertNotEquals(c7, c8);

        Coordinate c9 = new Coordinate(3, 4);
        Coordinate c10 = new Coordinate(-6, 4);
        assertNotEquals(c9, c10);
    }

    @Test
    void testEquals() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(1, 2);
        assertEquals(c1, c2);

        Coordinate c3 = new Coordinate(0, 0);
        Coordinate c4 = new Coordinate(0, 0);
        assertEquals(c3, c4);

        Coordinate c5 = new Coordinate(-6, 0);
        Coordinate c6 = new Coordinate(-6, 0);
        assertEquals(c5, c6);

        Coordinate c7 = new Coordinate(0, -9);
        Coordinate c8 = new Coordinate(0, -9);
        assertEquals(c7, c8);

        Coordinate c9 = new Coordinate(-3, -5);
        Coordinate c10 = new Coordinate(-3, -5);
        assertEquals(c9, c10);
    }

    @Test
    void testEqualsInArrayList() {
        ArrayList<Coordinate> list = new ArrayList<Coordinate>();

        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(1, 2);
        list.add(c1);
        list.add(c2);
        assertEquals(list.get(0), list.get(1));

        Coordinate c3 = new Coordinate(0, 0);
        Coordinate c4 = new Coordinate(0, 0);
        list.add(c3);
        list.add(c4);
        assertEquals(list.get(2),list.get(3));

        Coordinate c5 = new Coordinate(-6, 0);
        Coordinate c6 = new Coordinate(-6, 0);
        list.add(c5);
        list.add(c6);
        assertEquals(list.get(4),list.get(5));

        Coordinate c7 = new Coordinate(0, -9);
        Coordinate c8 = new Coordinate(0, -9);
        list.add(c7);
        list.add(c8);
        assertEquals(list.get(6),list.get(7));

        Coordinate c9 = new Coordinate(-3, -5);
        Coordinate c10 = new Coordinate(-3, -5);
        list.add(c9);
        list.add(c10);
        assertEquals(list.get(8),list.get(9));
    }


    @Test
    void testHashCodeNotEquals() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(3, 0);
        assertNotEquals(c1.hashCode(), c2.hashCode());

        Coordinate c3 = new Coordinate(-1, 2);
        Coordinate c4 = new Coordinate(-1, 0);
        assertNotEquals(c3.hashCode(), c4.hashCode());

        Coordinate c5 = new Coordinate(-1, -3);
        Coordinate c6 = new Coordinate(0, -3);
        assertNotEquals(c5.hashCode(), c6.hashCode());

        Coordinate c7 = new Coordinate(2, -6);
        Coordinate c8 = new Coordinate(2, 10);
        assertNotEquals(c7.hashCode(), c8.hashCode());

        Coordinate c9 = new Coordinate(3, 4);
        Coordinate c10 = new Coordinate(-6, 4);
        assertNotEquals(c9.hashCode(), c10.hashCode());
    }

    @Test
    void testHashCodeEquals() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(1, 2);
        assertEquals(c1.hashCode(), c2.hashCode());

        Coordinate c3 = new Coordinate(0, 0);
        Coordinate c4 = new Coordinate(0, 0);
        assertEquals(c3.hashCode(), c4.hashCode());

        Coordinate c5 = new Coordinate(-6, 0);
        Coordinate c6 = new Coordinate(-6, 0);
        assertEquals(c5.hashCode(), c6.hashCode());

        Coordinate c7 = new Coordinate(0, -9);
        Coordinate c8 = new Coordinate(0, -9);
        assertEquals(c7.hashCode(), c8.hashCode());

        Coordinate c9 = new Coordinate(-3, -5);
        Coordinate c10 = new Coordinate(-3, -5);
        assertEquals(c9.hashCode(), c10.hashCode());
    }

    @Test
    void sum() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(3, 4);
        assertEquals(new Coordinate(c1.getX()+c2.getX(), c1.getY() +c2.getY()), c1.sum(c2));
        assertEquals(new Coordinate(c1.getX()+c2.getX(), c1.getY() +c2.getY()), c2.sum(c1));

        Coordinate c3 = new Coordinate(0, 0);
        Coordinate c4 = new Coordinate(0, 0);
        assertEquals(new Coordinate(c3.getX()+c4.getX(), c3.getY()+c4.getY()), c3.sum(c4));
        assertEquals(new Coordinate(c3.getX()+c4.getX(), c3.getY()+c4.getY()), c4.sum(c3));
    }
}