package it.polimi.ingsfw.ingsfwproject.Model;

public class NormalFace extends Face{
    private int points;

    public NormalFace(int id, int points, Content[] corners) {
        super(id, corners);
        this.points=points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
