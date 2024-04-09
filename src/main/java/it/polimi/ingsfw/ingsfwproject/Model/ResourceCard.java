package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.Arrays;

public class ResourceCard extends NormalCard{
    private Front front;

    public Front getFront() {
        return front;
    }

    public void setFront(Front front) {
        this.front = front;
    }

   public void createCard(int id, Content center, int points, Content[] corners){
       this.front = new Front();
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



    }

    public void printAll(){
        System.out.println(super.getIdCard());
        System.out.println(String.valueOf(super.getBackface().getCenter()));
        System.out.println(this.front.getPoints());
        System.out.println(Arrays.toString(this.front.getCornerList()));
        System.out.println(Arrays.toString(this.getBackface().getCornerList()));
        System.out.println(Arrays.toString(this.getBackface().getCoveredCorner()));
        System.out.println();

    }
}
