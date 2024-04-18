package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

public class StarterFront extends Face{
    private ArrayList<Content> center;

    public ArrayList<Content> getCenter() {
        return center;
    }

    public void setCenter(ArrayList<Content> center) {
        this.center = center;
    }

    public StarterFront(int id, Content[] corners, ArrayList<Content> center) {
        super(id, corners);
        this.center = center;
    }
}
