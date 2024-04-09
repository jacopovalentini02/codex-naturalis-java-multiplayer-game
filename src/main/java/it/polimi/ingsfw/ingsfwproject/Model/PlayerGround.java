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
    private ArrayList<Coordinate> availablePositions;

    public PlayerGround(){
        resourceCount = new int[4];
        objectCount = new int[3];
        grid = new HashMap<>();
        availablePositions = new ArrayList<>();
        availablePositions.add(new Coordinate(0,0));
    }

    public void playCard(Face face, Coordinate coord){
        grid.put(coord, face);
        updateAvailablePositions(coord);
    }

    private void updateAvailablePositions(Coordinate coord){
        availablePositions.remove(coord);

        Face facePlayed = grid.get(coord);

        Content corner = null;
        Coordinate check;
        for(int i=-1; i<=1; i=i+2) {
            for (int j = -1; j <= 1; j = j + 2) {
                check = new Coordinate(coord.getX() + i, coord.getY() + j);
                if(!grid.containsKey(check)){
                    if(i==-1 && j==-1) //bottom left card
                        corner = facePlayed.getBL();
                    if(i==-1 && j==1) //top left card
                        corner = facePlayed.getTL();
                    if(i==1 && j==-1) //bottom right card
                        corner = facePlayed.getBR();
                    if(i==1 && j==1) //top right card
                        corner = facePlayed.getTR();


                    if(corner != Content.HIDDEN)
                        availablePositions.add(check);
                }else{
                    if(i==-1 && j==-1) //bottom left card
                        grid.get(check).setCoveredTR(true);
                    if(i==-1 && j==1) //top left card
                        grid.get(check).setCoveredBR(true);
                    if(i==1 && j==-1) //bottom right card
                        grid.get(check).setCoveredTL(true);
                    if(i==1 && j==1) //top right card
                        grid.get(check).setCoveredBL(true);
                }
            }
        }
    }

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


    /*public int objPointsCalc(){

    }*/
}
