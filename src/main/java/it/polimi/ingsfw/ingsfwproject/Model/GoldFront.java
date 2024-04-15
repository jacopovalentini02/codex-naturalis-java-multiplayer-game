package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

public class GoldFront extends Front{
    private ArrayList<Content> cost;
    private boolean overlapped;
    private Content objectNeeded;

    public GoldFront(int id) {
        super(id);
    }

    public void setCost(ArrayList<Content> cost) {
        this.cost = cost;
    }

    public void setOverlapped(boolean overlapped) {
        this.overlapped = overlapped;
    }

    public void setObjectNeeded(Content objectNeeded) {
        this.objectNeeded = objectNeeded;
    }

    public boolean isOverlapped() {
        return overlapped;
    }

    public Content getObjectNeeded() {
        return objectNeeded;
    }

    public ArrayList<Content> getCost() {
        return cost;
    }
}
