package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Model.Card;
import it.polimi.ingsfw.ingsfwproject.Model.Content;

abstract public class ObjectiveCard extends Card {
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
