package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Model.Card;
import it.polimi.ingsfw.ingsfwproject.Model.Content;

abstract public class ObjectiveCard extends Card {
    private Content color;
    private int points;
    public Content getColor() {
        return color;
    }

    public void setColor(Content color) {
        this.color = color;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


}
