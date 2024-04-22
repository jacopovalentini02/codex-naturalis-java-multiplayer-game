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


    @Override
    public int verifyObjective(PlayerGround ground) {
        Map<Content, Integer> counters = new HashMap<>();

        for (Content c : objectRequested) {
            counters.put(c, ground.getContentCount(c));
        }

        int index = 0;
        int setsCounter = 0;

        while (true){
            Content c = objectRequested.get(index); //get i-th object requested by the card
            int currentCount = counters.get(c); //get object actual value from the map
            if (currentCount == 0){
                break;
            } else {
                currentCount--; //decrement the counter and put it back in the map
                counters.put(c, currentCount);
            }

            if(index == objectRequested.size()-1){ //go back to the beginning of the list if it's over
                setsCounter++; //a set has been completed
                index = 0;
            } else {
                index++;
            }

        }
        return setsCounter * this.getPoints();

    }
}
