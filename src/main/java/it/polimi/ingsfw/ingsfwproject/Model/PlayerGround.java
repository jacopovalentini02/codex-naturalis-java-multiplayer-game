package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

public class PlayerGround {
    /*resourceCount[0] = PLANT_KINGDOM
    resourceCount[1] = ANIMAL_KINGDOM
    resourceCount[2] = FUNGI_KINGDOM
    resourceCount[3] = INSECT_KINGDOM

    objectCount[0] =  QUILL
    objectCount[1] =  INKWELL
    objectCount[2] =  MANUSCRIPT

    * */
    private int[] resourceCount;
    private int[] objectCount;
    private Map<Coordinate, Face> grid;

    public ArrayList<Coordinate> getAvailablePositions() {
        return availablePositions;
    }

    private ArrayList<Coordinate> availablePositions;

    public PlayerGround(){
        resourceCount = new int[4];
        for(int i=0; i<4; i++)
            resourceCount[i] = 0;

        objectCount = new int[3];
        for(int i=0; i<3; i++)
            objectCount[i] = 0;

        grid = new HashMap<>();
        availablePositions = new ArrayList<>();
        availablePositions.add(new Coordinate(0,0));
    }

    public void playCard(PlayableCard card, boolean upwards, Coordinate coord){
        //TODO: eventuali eccezioni
        /*TODO: fare i check:
         * se la face non è in mano al player
         * se la coordinata non è tra le available
         * se il costo della carta è troppo alto (oggetti richiesti > oggetti posseduti)
         */

        //get the face
        Face face;
        if(upwards)
            face = card.getFront();
        else
            face = card.getBack();

        //add the face in the grid
        grid.put(coord, face);

        //update the available position and counts the corners that the face has covered (i.e. gold front points)
        int coveredCorners = updateAvailablePositions(coord);
        /*
        if(upwards)
            calculatePoints(face);
        */

    }

    private int updateAvailablePositions(Coordinate coord){
        //remove the position from availablePositions
        availablePositions.remove(coord);

        //getting the played face
        Face facePlayed = grid.get(coord);

        //create variables to iterate in the 4 near position and, for each, to iterate in the 4 corner
        Content corner = null;
        Coordinate check;

        //counter for counting the number of the corners that the face has covered in this played
        int counter = 0;

        for(int i=-1; i<=1; i=i+2) {
            for (int j = -1; j <= 1; j = j + 2) {
                check = new Coordinate(coord.getX() + i, coord.getY() + j);
                if(!grid.containsKey(check)){
                    if(i==-1 && j==-1) //bottom left card
                        corner = facePlayed.getBL();
                    else if(i==-1 && j==1) //top left card
                        corner = facePlayed.getTL();
                    else if(i==1 && j==-1) //bottom right card
                        corner = facePlayed.getBR();
                    else if(i==1 && j==1) //top right card
                        corner = facePlayed.getTR();

                    //TODO: corner may be null
                    if(corner != Content.HIDDEN)
                        availablePositions.add(check);
                }else{
                    counter++;

                    if(i==-1 && j==-1) { //bottom left card
                        updateCounter(grid.get(check).getTR());
                        grid.get(check).setCoveredTR(true);
                    }
                    else if(i==-1 && j==1) { //top left card
                        updateCounter(grid.get(check).getBR());
                        grid.get(check).setCoveredBR(true);
                    }
                    else if(i==1 && j==-1) { //bottom right card
                        updateCounter(grid.get(check).getTL());
                        grid.get(check).setCoveredTL(true);
                    }
                    else if(i==1 && j==1) { //top right card
                        updateCounter(grid.get(check).getBL());
                        grid.get(check).setCoveredBL(true);
                    }
                }
            }
        }
        //update the counters checking in the center of the played card, if it has it
        if(facePlayed instanceof NormalBack)
            updateCounter(((NormalBack) facePlayed).getCenter());
        else if (facePlayed instanceof  StarterFront) {
            for(Content c : ((StarterFront) facePlayed).getCenter()){
                updateCounter(c);
            }
        }
        return counter;
    }

    private void updateCounter(Content content){
        //TODO: lanciare eccezione se l'angolo è hidden
        if(content == Content.ANIMAL_KINGDOM)
            this.setAnimalCount(this.getAnimalCount()-1);
        else if(content == Content.FUNGI_KINGDOM)
            this.setFungiCount(this.getFungiCount()-1);
        else if(content == Content.INSECT_KINGDOM)
            this.setInsectCount(this.getInsectCount()-1);
        else if(content == Content.PLANT_KINGDOM)
            this.setPlantCount(this.getPlantCount()-1);
        else if(content == Content.INKWELL)
            this.setInkwellCount(this.getInkwellCount()-1);
        else if(content == Content.MANUSCRIPT)
            this.setManuscriptCount(this.getManuscriptCount()-1);
        else if(content == Content.QUILL)
            this.setQuillCount(this.getQuillCount()-1);
    }

    /*public int calculatePoints(Face face) {
        if(face instanceof GoldFront){
            if(((GoldFront) face).getObjectNeeded() == null){

            }
        }
    }
    */
    //RESORUCE GETTERS AND SETTERS
    public int getPlantCount(){
        return this.resourceCount[0];
    }
    public void setPlantCount(int count){
        this.resourceCount[0] = count;
    }
    public int getAnimalCount(){
        return this.resourceCount[1];
    }
    public void setAnimalCount(int count){
        this.resourceCount[1] = count;
    }
    public int getFungiCount(){
        return this.resourceCount[2];
    }
    public void setFungiCount(int count){
        this.resourceCount[2] = count;
    }
    public int getInsectCount(){
        return this.resourceCount[3];
    }
    public void setInsectCount(int count){
        this.resourceCount[3] = count;
    }

    //OBJECT GETTERS AND SETTERS
    public int getQuillCount(){
        return this.objectCount[0];
    }
    public void setQuillCount(int count){
        this.objectCount[0] = count;
    }
    public int getInkwellCount(){
        return this.objectCount[1];
    }
    public void setInkwellCount(int count){
        this.objectCount[1] = count;
    }
    public int getManuscriptCount(){
        return this.objectCount[2];
    }
    public void setManuscriptCount(int count){
        this.objectCount[2] = count;
    }

    //

    public Map<Coordinate, Face> getGrid(){
        return grid;
    }



}
