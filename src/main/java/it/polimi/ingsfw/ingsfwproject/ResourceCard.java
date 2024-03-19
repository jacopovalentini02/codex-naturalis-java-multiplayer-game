package it.polimi.ingsfw.ingsfwproject;

import java.util.ArrayList;
import java.util.Enumeration;

public class ResourceCard extends NormalCard{
    private Front front;

    public Front getFront() {
        return front;
    }

    public void setFront(Front front) {
        this.front = front;
    }

    public void createCard(int id, Content center, int points, Content[] corners){
        this.setIdCard(id);
        this.front.setCornerList(corners);
        this.front.setPoints(points);
        super.getBackface().setCenter(center);

    }
}
