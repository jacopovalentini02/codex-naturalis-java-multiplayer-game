package it.polimi.ingsfw.ingsfwproject;

import java.util.*;

abstract public class Face {
    private Content[] cornerList=new Content[4];
    private boolean[] coveredCorner;

    public Content[] getCornerList() {
        return cornerList;
    }

    public void setCornerList(Content[] cornerList) {
        this.cornerList = cornerList;
    }

    public boolean[] getCoveredCorner() {
        return coveredCorner;
    }

    public void setCoveredCorner(boolean[] coveredCorner) {
        this.coveredCorner = coveredCorner;
    }
}
