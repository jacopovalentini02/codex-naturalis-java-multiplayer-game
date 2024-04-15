package it.polimi.ingsfw.ingsfwproject.Model;

public class Front extends Face{
    private int points;

    public Front(int id) {
        super(id);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
