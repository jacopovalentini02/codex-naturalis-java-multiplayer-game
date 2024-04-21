package it.polimi.ingsfw.ingsfwproject.Model;

public class ContentCounter {
    /*resourceCount[0] = FUNGI_KINGDOM
    resourceCount[1] = PLANT_KINGDOM
    resourceCount[2] = ANIMAL_KINGDOM
    resourceCount[3] = INSECT_KINGDOM

    objectCount[0] =  QUILL
    objectCount[1] =  INKWELL
    objectCount[2] =  MANUSCRIPT

    */

    private int[] resourceCount;
    private int[] objectCount;

    public ContentCounter() {
        resourceCount = new int[4];
        objectCount = new int[3];
    }

    public void setCounter(Content content, int newValue) {
        if (content == Content.FUNGI_KINGDOM)
            resourceCount[0] = newValue;
        else if (content == Content.PLANT_KINGDOM)
            resourceCount[1] = newValue;
        else if (content == Content.ANIMAL_KINGDOM)
            resourceCount[2] = newValue;
        else if (content == Content.INSECT_KINGDOM)
            resourceCount[3] = newValue;
        else if (content == Content.INKWELL)
            objectCount[0] = newValue;
        else if (content == Content.MANUSCRIPT)
            objectCount[1] = newValue;
        else if (content == Content.QUILL)
            objectCount[2] = newValue;
    }

    public void decrementCounter(Content content) {
        this.setCounter(content, getCounter(content)-1);
    }

    public int getCounter(Content content) {
        if (content == Content.FUNGI_KINGDOM)
            return resourceCount[0];
        else if (content == Content.PLANT_KINGDOM)
            return resourceCount[1];
        else if (content == Content.ANIMAL_KINGDOM)
            return resourceCount[2];
        else if (content == Content.INSECT_KINGDOM)
            return resourceCount[3];
        else if (content == Content.INKWELL)
            return objectCount[0];
        else if (content == Content.MANUSCRIPT)
            return objectCount[1];
        else if (content == Content.QUILL)
            return objectCount[2];
        return -1;
    }

    public ContentCounter getCopy(){
        ContentCounter copy = new ContentCounter();
        for(Content c : Content.values()){
            copy.setCounter(c, this.getCounter(c));
        }
        return copy;
    }




}
