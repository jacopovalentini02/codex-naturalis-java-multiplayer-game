package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class StarterCard extends PlayableCard{
    private NormalFace back;
    private StarterFront front;


    @Override
    public NormalFace getBack() {
        return back;
    }

    public void setBack(NormalFace back) {
        this.back = back;
    }

    @Override
    public StarterFront getFront() {
        return front;
    }

    public void setFront(StarterFront front) {
        this.front = front;
    }

    public void createCard(int id, ArrayList<Content> center, Content[] cornerBack, Content[] cornerFront){
        this.back = new NormalFace(id);
        this.front=new StarterFront(id);

        this.setIdCard(id);
        this.back.setIdCard(id);
        this.front.setIdCard(id);
        this.back.setCornerList(cornerFront);
        this.front.setCornerList(cornerBack);
        this.front.setCenter(center);
        this.back.setPoints(0);
        boolean[] coveredCorn = new boolean[4];
        this.back.setCoveredCorner(coveredCorn);
        this.front.setCoveredCorner(coveredCorn);

    }

    public void printAll(){
        System.out.println(super.getIdCard());
        System.out.println(this.back.getPoints());
        System.out.println(Arrays.toString(this.back.getCornerList()));
        System.out.println(Arrays.toString(this.front.getCornerList()));
        System.out.println(this.front.getCenter());

    }
}
