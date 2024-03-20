package it.polimi.ingsfw.ingsfwproject;

import java.util.ArrayList;
import java.util.Arrays;

public class GoldCard extends NormalCard{
    private GoldFront front;

    public GoldFront getFront() {
        return front;
    }

    public void setFront(GoldFront front) {
        this.front = front;
    }


    public void createCard(int id, Content center, int points, Content[] corners, ArrayList<Content> cost, Content object, Boolean overlapped){
        this.front = new GoldFront();
        NormalBack normalBack = new NormalBack();
        this.setBackface(normalBack);
        this.setIdCard(id);
        this.front.setCornerList(corners);
        this.front.setPoints(points);
        normalBack.setCenter(center);
        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
        normalBack.setCornerList(emptyCorners);
        boolean[] coveredCorn = new boolean[4];
        normalBack.setCoveredCorner(coveredCorn);
        this.front.setOverlapped(overlapped);
        this.front.setObjectNeeded(object);
        this.front.setCost(cost);

    }

    public void printAll(){
        System.out.println(super.getIdCard());
        System.out.println(String.valueOf(super.getBackface().getCenter()));
        System.out.println(this.front.getPoints());
        System.out.println(Arrays.toString(this.front.getCornerList()));
        System.out.println(Arrays.toString(this.getBackface().getCornerList()));
        System.out.println(Arrays.toString(this.getBackface().getCoveredCorner()));
        System.out.println( this.front.getObjectNeeded());
        System.out.println((this.front.getCost()));
        System.out.println();

    }
}

