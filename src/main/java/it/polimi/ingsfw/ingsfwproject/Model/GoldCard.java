package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class GoldCard extends NormalCard{
    private GoldFront front;

    public GoldCard(NormalBack backface, GoldFront front, int id) {
        super(id, backface);
        this.front=front;
    }

    public GoldFront getFront() {
        return front;
    }

    @Override
    public Face getBack() {
        return this.getBackface();
    }

    public void setFront(GoldFront front) {
        this.front = front;
    }


//    public void createCard(int id, Content center, int points, Content[] corners, ArrayList<Content> cost, Content object, Boolean overlapped){
//        this.front = new GoldFront(id);
//        NormalBack normalBack = new NormalBack(id);
//        this.setBackface(normalBack);
//        this.setIdCard(id);
//        this.front.setIdCard(id);
//        normalBack.setIdCard(id);
//        this.front.setCornerList(corners);
//        this.front.setPoints(points);
//        normalBack.setCenter(center);
//        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
//        normalBack.setCornerList(emptyCorners);
//        boolean[] coveredCorn = new boolean[4];
//        normalBack.setCoveredCorner(coveredCorn);
//        this.front.setOverlapped(overlapped);
//        this.front.setObjectNeeded(object);
//        this.front.setCost(cost);

//    }

}

