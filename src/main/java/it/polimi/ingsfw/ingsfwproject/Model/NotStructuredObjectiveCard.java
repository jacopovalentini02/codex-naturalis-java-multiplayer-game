package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

/**
 * Class NotStructuredObjectiveCard
 *
 * Description: This class represents an objective card that requires a non-structured collection of objects to be completed.
 * It extends the ObjectiveCard class.
 */
public class NotStructuredObjectiveCard extends ObjectiveCard {
    private ArrayList<Content> objectRequested;

    /**
     * @return The list of objects requested by this objective card.
     */
    public ArrayList<Content> getObjectRequested() {
        return objectRequested;
    }

    /**
     * Constructor for the NotStructuredObjectiveCard class.
     *
     * @param idCard The ID of the card.
     * @param points The points awarded for completing the objective.
     * @param objectRequested The list of objects required to complete the objective.
     */
    public NotStructuredObjectiveCard(int idCard, int points, ArrayList<Content> objectRequested) {
        super(idCard, points);
        this.objectRequested = objectRequested;
    }

    /**
     * @return A string representation of the NotStructuredObjectiveCard object.
     */
    @Override
    public String toString() {
        return "NotStructuredObjectiveCard{" +
                "objectRequested=" + objectRequested +
                '}';
    }

    /**
     * Verifies if the objective is completed based on the player's ground.
     *
     * @param ground The player's ground to check for the required objects.
     * @return The points awarded for completing the objective.
     */
    @Override
    public int verifyObjective(PlayerGround ground) {
        Map<Content, Integer> counters = new HashMap<>();

        // Count the occurrences of each required content in the player's ground
        for (Content c : objectRequested) {
            counters.put(c, ground.getContentCount(c));
        }

        int index = 0;
        int setsCounter = 0;

        // Loop to check how many complete sets of requested objects are present in the player's ground
        while (true) {
            Content c = objectRequested.get(index); // Get the current object requested by the card
            int currentCount = counters.get(c); // Get the current count of the object from the map

            if (currentCount == 0) {
                break; // If the count is zero, break the loop
            } else {
                currentCount--; // Decrement the count and put it back in the map
                counters.put(c, currentCount);
            }

            if (index == objectRequested.size() - 1) {
                setsCounter++; // A complete set has been found
                index = 0; // Reset the index to start checking for another set
            } else {
                index++;
            }
        }

        return setsCounter * this.getPoints(); // Return the total points for all complete sets found
    }
}
