package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serializable;

/**
 * This class represents a point in two-dimensional space.
 * It holds x and y coordinate.
 * It implements {@link java.io.Serializable} to allow the coordinate objects to be serialized.
 */

public class Coordinate implements Serializable {
    private final int x;
    private final int y;

    private static final long serialVersionUID=1234567890987654321L;

    /**
     * Constructs a new {@code Coordinate} with the specified x and y value
     * @param x the x value
     * @param y the y value
     */
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of this {@code Coordinate}.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this {@code Coordinate}.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * It Compares this {@code Coordinate} object to the specified object. The result is true if and only if
     * the argument is not null and it has the same x and y values as this {@code Coordinate}.
     *
     * @param coord The object to compare
     * @return True if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object coord){
        if(coord instanceof Coordinate){
            Coordinate coordinata = (Coordinate)coord;
            return this.getX() == coordinata.getX() && this.getY() == coordinata.getY();
        }
        return false;
    }

    /**
     * Returns a string that describes this {@code Coordinate} in the format "(x,y)"
     * @return a string representation of this {@code Coordinate} in the format "(x,y)"
     */
    @Override
    public String toString(){
        return "("+ this.getX() + ","+ this.getY() +")";
    }

    /**
     * Returns a hash code value for this {@code Coordinate}.
     *
     * <p>The hash code is computed using the x and y coordinates
     * in such a way that equal {@code Coordinate} objects will have
     * the same hash code.</p>
     * @return
     */
    @Override
    public int hashCode() {
        final int prime = 31; // Un numero primo usato come base
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    /**
     * Returns a new {@code Coordinate} object whose coordinates
     * are the sum of the coordinates of this {@code Coordinate}
     * and the specified {@code Coordinate}.
     * @param toSum the {@code Coordinate} to be added
     * @return new {@code Coordinate} object whose coordinates
     *      are the sum of the coordinates of this {@code Coordinate}
     *      and {@code toSum}
     */
    public Coordinate sum(Coordinate toSum){
        return new Coordinate(x+toSum.getX(), y+toSum.getY());
    }

}
