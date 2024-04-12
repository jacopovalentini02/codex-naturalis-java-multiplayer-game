package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Model.Content;

abstract public class Face {
    /*cornerList[0] = top left
    cornerList[1] = top right
    cornerList[2] = bottom left
    cornerList[3] = bottom right
     */
    private int idCard;

    private Content[] cornerList;
    private boolean[] coveredCorner;

    public Face(){
        cornerList = new Content[4];
        coveredCorner = new boolean[4];
    }

    public Content[] getCornerList() {
        return cornerList;
    }

    public void setCornerList(Content[] cornerList) {
        this.cornerList = cornerList;
    }

    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

    public boolean[] getCoveredCorner() {
        return coveredCorner;
    }

    public void setCoveredCorner(boolean[] coveredCorner) {
        this.coveredCorner = coveredCorner;
    }

    public Content getTL(){
        return this.cornerList[0];
    }
    public Content getTR(){
        return this.cornerList[1];
    }
    public Content getBL(){
        return this.cornerList[2];
    }
    public Content getBR(){
        return this.cornerList[3];
    }

    public void setCoveredTL(boolean value){
        this.coveredCorner[0] = value;
    }
    public void setCoveredTR(boolean value){
        this.coveredCorner[1] = value;
    }
    public void setCoveredBL(boolean value){
        this.coveredCorner[2] = value;
    }
    public void setCoveredBR(boolean value){
        this.coveredCorner[3] = value;
    }
}
