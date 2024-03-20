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
        NormalBack prova= super.getBackface();
        prova= new NormalBack();
        this.front = new Front();
        this.setIdCard(id);
        this.front.setCornerList(corners);
        this.front.setPoints(points);
        prova.setCenter(center);
    }

    public void printAll(){
        System.out.println(super.getIdCard());
        //System.out.println(String.valueOf(super.getBackface().getCenter()));
        System.out.println(this.front.getPoints());
        //System.out.println(String.valueOf(this.front.getCornerList()));
    }
}
