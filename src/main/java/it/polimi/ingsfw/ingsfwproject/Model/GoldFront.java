package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

public class GoldFront extends NormalFace {
    private ArrayList<Content> cost;
    private boolean overlapped;
    private Content objectNeeded;

    public GoldFront(int id, int points, Content[] corners, ArrayList<Content> cost, boolean overlapped, Content objectNeeded) {
        super(id, points, corners);
        this.cost = cost;
        this.overlapped = overlapped;
        this.objectNeeded = objectNeeded;
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
