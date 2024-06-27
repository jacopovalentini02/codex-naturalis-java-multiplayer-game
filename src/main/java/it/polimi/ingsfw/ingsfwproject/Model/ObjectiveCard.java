package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serial;

abstract public class ObjectiveCard extends Card {
    @Serial
    private static final long serialVersionUID = 19088521543661542L;
    private int points;


    public ObjectiveCard(int idCard, int points) {
        super(idCard);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public abstract int verifyObjective(PlayerGround ground);

}
