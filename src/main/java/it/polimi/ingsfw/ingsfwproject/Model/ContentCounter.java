package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serializable;

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

    private int[] countersArray;

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

    public void decrementCounter(Content content) {
        this.setCounter(content, getCounter(content)-1);
    }

    public void incrementCounter(Content content) {
        this.setCounter(content, getCounter(content)+1);
    }

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

    public ContentCounter getCopy(){
        ContentCounter copy = new ContentCounter();
        for(Content c : Content.values()){
            copy.setCounter(c, this.getCounter(c));
        }
        return copy;
    }

    public String toString() {
        String s = "";
        for(Content c : Content.values()){
            s += c.toString() + " count: " + getCounter(c) + "\n";
        }
        return s;
    }




}
