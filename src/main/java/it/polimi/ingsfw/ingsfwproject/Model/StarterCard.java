package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class StarterCard extends PlayableCard{
    private NormalFace back;
    private StarterFront front;


    public StarterCard(int idCard, NormalFace back, StarterFront front) {
        super(idCard);
        this.back = back;
        this.front = front;
    }

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


    public void printAll(){
        System.out.println(super.getIdCard());
        System.out.println(this.back.getPoints());
        System.out.println(Arrays.toString(this.back.getCornerList()));
        System.out.println(Arrays.toString(this.front.getCornerList()));
        System.out.println(this.front.getCenter());

    }
}
