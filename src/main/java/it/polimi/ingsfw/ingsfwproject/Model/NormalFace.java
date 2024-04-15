package it.polimi.ingsfw.ingsfwproject.Model;

public class NormalFace extends Face{
    private int points;

    public NormalFace(int id) {
        super(id);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
