package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

public class StarterBack extends Face{
    private ArrayList<Content> center;

    public StarterBack(int id) {
        super(id);
    }

    public ArrayList<Content> getCenter() {
        return center;
    }

    public void setCenter(ArrayList<Content> center) {
        this.center = center;
    }
}
