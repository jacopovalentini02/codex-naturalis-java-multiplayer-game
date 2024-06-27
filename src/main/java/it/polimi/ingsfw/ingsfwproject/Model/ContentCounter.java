package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serializable;

/**
 * This class represents a counter to keep track of a player's number of resources and objects.
 * It implements {@link java.io.Serializable} to allow the coordinate objects to be serialized.
 */

public class ContentCounter implements Serializable {
    /*
    countersArray[0] = FUNGI_KINGDOM
    countersArray[1] = PLANT_KINGDOM
    countersArray[2] = ANIMAL_KINGDOM
    countersArray[3] = INSECT_KINGDOM
    countersArray[4] =  QUILL
    countersArray[5] =  INKWELL
    countersArray[6] =  MANUSCRIPT
    */
    private static final long serialVersionUID=1122334455667788990L;
    private int[] countersArray;

    /**
     * Constructs a Content Counter
     */
    public ContentCounter() {
        countersArray = new int[7];
    }

    public void setCounter(Content content, int newValue) {
        if (content == Content.FUNGI_KINGDOM)
            countersArray[0] = newValue;
        else if (content == Content.PLANT_KINGDOM)
            countersArray[1] = newValue;
        else if (content == Content.ANIMAL_KINGDOM)
            countersArray[2] = newValue;
        else if (content == Content.INSECT_KINGDOM)
            countersArray[3] = newValue;
        else if (content == Content.INKWELL)
            countersArray[4] = newValue;
        else if (content == Content.MANUSCRIPT)
            countersArray[5] = newValue;
        else if (content == Content.QUILL)
            countersArray[6] = newValue;
    }

    /**
     * Decrements the count of the object or the resource passed as a parameter
     * @param content the resource or object that needs to be decreased
     */
    public void decrementCounter(Content content) {
        this.setCounter(content, getCounter(content)-1);
    }

    /**
     * Increments the count of the object or the resource passed as a parameter
     * @param content the resource or object that needs to be increased
     */
    public void incrementCounter(Content content) {
        this.setCounter(content, getCounter(content)+1);
    }

    /**
     * Returns the count of the object or the resource passed as a parameter
     * @param content the resource or object for which you want to know the count
     * @return the count of the resource or object passed as a parameter
     */
    public int getCounter(Content content) {
        if (content == Content.FUNGI_KINGDOM)
            return countersArray[0];
        else if (content == Content.PLANT_KINGDOM)
            return countersArray[1];
        else if (content == Content.ANIMAL_KINGDOM)
            return countersArray[2];
        else if (content == Content.INSECT_KINGDOM)
            return countersArray[3];
        else if (content == Content.INKWELL)
            return countersArray[4];
        else if (content == Content.MANUSCRIPT)
            return countersArray[5];
        else if (content == Content.QUILL)
            return countersArray[6];
        return 0;
    }

    /**
     * Returns a copy of the counter
     * @return a copy of the counter
     */
    public ContentCounter getCopy(){
        ContentCounter copy = new ContentCounter();
        for(Content c : Content.values()){
            copy.setCounter(c, this.getCounter(c));
        }
        return copy;
    }
}
