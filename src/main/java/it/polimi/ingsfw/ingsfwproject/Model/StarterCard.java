package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class StarterCard extends PlayableCard{
    private Front front;
    private StarterBack back;

    public Front getFront() {
        return front;
    }

    public void setFront(Front front) {
        this.front = front;
    }

    public StarterBack getBack() {
        return back;
    }

    public void setBack(StarterBack back) {
        this.back = back;
    }

    public void createCard(int id, ArrayList<Content> center, Content[] cornerBack, Content[] cornerFront){
        this.front = new Front();
        this.back=new StarterBack();

        this.setIdCard(id);
        this.front.setIdCard(id);
        this.back.setIdCard(id);
        this.front.setCornerList(cornerFront);
        this.back.setCornerList(cornerBack);
        this.back.setCenter(center);
        this.front.setPoints(0);
        boolean[] coveredCorn = new boolean[4];
        this.front.setCoveredCorner(coveredCorn);
        this.back.setCoveredCorner(coveredCorn);

    }

    public void printAll(){
        System.out.println(super.getIdCard());
        System.out.println(this.front.getPoints());
        System.out.println(Arrays.toString(this.front.getCornerList()));
        System.out.println(Arrays.toString(this.back.getCornerList()));
        System.out.println(this.back.getCenter());

    }
}
