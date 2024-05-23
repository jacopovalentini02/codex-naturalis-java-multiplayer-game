package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.Arrays;

public class ResourceCard extends NormalCard{
    private NormalFace front;

    public NormalFace getFront() {
        return front;
    }

    @Override
    public Face getBack() {
        return this.getBackface();
    }

    public ResourceCard(NormalFace front, NormalBack backface, int id) {
        super(id, backface);
        this.front = front;
    }

    public void setFront(NormalFace front) {
        this.front = front;
    }

//   public void createCard(int id, Content center, int points, Content[] corners){
//       this.front = new NormalFace(id);
//       NormalBack normalBack = new NormalBack(id);
//       this.setBackface(normalBack);
//       this.front.setCornerList(corners);
//       this.front.setPoints(points);
//       this.setIdCard(id);
//       this.front.setIdCard(id);
//       normalBack.setIdCard(id);
//       normalBack.setCenter(center);
//       Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
//       normalBack.setCornerList(emptyCorners);
//       boolean[] coveredCorn = new boolean[4];
//       normalBack.setCoveredCorner(coveredCorn);
//
//
//
//    }

}
