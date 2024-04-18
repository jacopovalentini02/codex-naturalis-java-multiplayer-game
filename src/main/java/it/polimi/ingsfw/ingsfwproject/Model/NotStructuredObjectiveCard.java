package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

public class NotStructuredObjectiveCard extends ObjectiveCard{
    private ArrayList<Content> objectRequested;

    public ArrayList<Content> getObjectRequested() {
        return objectRequested;
    }

    public void setObjectRequested(ArrayList<Content> objectRequested) {
        this.objectRequested = objectRequested;
    }

    public NotStructuredObjectiveCard(int idCard, int points, ArrayList<Content> objectRequested) {
        super(idCard, points);
        this.objectRequested = objectRequested;
    }

    @Override
    public String toString() {
        return "NotStructuredObjectiveCard{" +
                "objectRequested=" + objectRequested +
                '}';
    }
}
