package it.polimi.ingsfw.ingsfwproject;

public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object coord){
        if(coord instanceof Coordinate){
            Coordinate coordinata = (Coordinate)coord;
            return this.getX() == coordinata.getX() && this.getY() == coordinata.getY();
        }
        return false;
    }

    @Override
    public String toString(){
        return "("+ this.getX() + ","+ this.getY() +")";
    }
}
