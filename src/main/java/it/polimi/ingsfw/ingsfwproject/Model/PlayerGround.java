package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;

import java.io.Serializable;
import java.util.*;

public class PlayerGround implements Serializable {

    private ContentCounter contentCounter;
    private Map<Coordinate, Face> grid;
    private ArrayList<Coordinate> availablePositions;

    public PlayerGround(){
        contentCounter = new ContentCounter();
        grid = new HashMap<>();
        availablePositions = new ArrayList<>();
        availablePositions.add(new Coordinate(0,0));
    }

    public int playCard(PlayableCard card, boolean upwards, Coordinate coord) throws NotEnoughResourcesException, PositionNotAvailableException {
        checkIfPlayable(card, upwards, coord);

        //get the face
        Face face = upwards? card.getFront() : card.getBack();

        //add the face in the grid
        grid.put(coord, face);

        //update the resources and objects counter
        updateCounters(face);

        //update the available position and counts the corners that the face has covered (i.e. gold front points)
        int coveredCorners = updateAvailablePositions(coord);

        if(face instanceof NormalFace){
            return calculatePoints(face, coveredCorners);
        }
        return 0;
    }

    private void checkIfPlayable(PlayableCard card, boolean upwards, Coordinate coord) throws PositionNotAvailableException, NotEnoughResourcesException {
        if(card instanceof StarterCard && !coord.equals(new Coordinate(0,0))){
            throw new PositionNotAvailableException("you can only play a starter card in " + new Coordinate(0,0));
        }
        if(!availablePositions.contains(coord))
            throw new PositionNotAvailableException("the position " + coord + "is not an available position");
        if(upwards && card.getFront() instanceof GoldFront face){
            ArrayList<Content> cost = face.getCost();
            ContentCounter counterCopy = contentCounter.getCopy();
            for(Content c : cost){
                counterCopy.decrementCounter(c);
                if (counterCopy.getCounter(c) == -1)
                    throw new NotEnoughResourcesException("you have not enough resource to play the card with id" + card.getIdCard());
            }
        }
    }

    private void updateCounters(Face facePlayed){
        //update the counters checking the corners
        if(facePlayed.getBL() != Content.HIDDEN || facePlayed.getBL() != Content.EMPTY)
            contentCounter.incrementCounter(facePlayed.getBL());
        if(facePlayed.getTL() != Content.HIDDEN || facePlayed.getTL() != Content.EMPTY)
            contentCounter.incrementCounter(facePlayed.getTL());
        if(facePlayed.getBR() != Content.HIDDEN || facePlayed.getBR() != Content.EMPTY)
            contentCounter.incrementCounter(facePlayed.getBR());
        if(facePlayed.getTR() != Content.HIDDEN || facePlayed.getTR() != Content.EMPTY)
            contentCounter.incrementCounter(facePlayed.getTR());

        //update the counters checking in the center of the played card, if it has it
        if(facePlayed instanceof NormalBack)
            contentCounter.incrementCounter(((NormalBack) facePlayed).getCenter());
        else if (facePlayed instanceof  StarterFront) {
            for(Content c : ((StarterFront) facePlayed).getCenter()){
                contentCounter.incrementCounter(c);
            }
        }
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

                    if(corner != Content.HIDDEN)
                        availablePositions.add(check);
                }else{
                    counter++;

                    if(i==-1 && j==-1) { //bottom left card
                        contentCounter.decrementCounter(grid.get(check).getTR());
                        grid.get(check).setCoveredTR(true);
                    }
                    else if(i==-1 && j==1) { //top left card
                        contentCounter.decrementCounter(grid.get(check).getBR());
                        grid.get(check).setCoveredBR(true);
                    }
                    else if(i==1 && j==-1) { //bottom right card
                        contentCounter.decrementCounter(grid.get(check).getTL());
                        grid.get(check).setCoveredTL(true);
                    }
                    else if(i==1 && j==1) { //top right card
                        contentCounter.decrementCounter(grid.get(check).getBL());
                        grid.get(check).setCoveredBL(true);
                    }
                }
            }
        }
        return counter;
    }

    public int calculatePoints(Face face, int coveredCorners) {
        int points = 0;
        if(face instanceof GoldFront){
            if(((GoldFront) face).isOverlapped()){
                //overlapped ==> it gives 2 points for each corner that the played card has covered
                points = ((GoldFront) face).getPoints() * coveredCorners;
            }else{
                if(((GoldFront) face).getObjectNeeded() == null){
                    //if it is not overlapped and the needed object variable is null, it means it gives unconditionally points
                    points = ((GoldFront) face).getPoints();
                }else{
                    //if it has a needed object, it gives 1 point for each object of needed object is on the playerGround
                    points = contentCounter.getCounter(((GoldFront) face).getObjectNeeded()) * ((GoldFront) face).getPoints();
                }
            }
        }else{
            points=((NormalFace)face).getPoints();
        }
        return points;
    }

    public ArrayList<Coordinate> getAvailablePositions() {
        return availablePositions;
    }
    public Map<Coordinate, Face> getGrid(){
        return grid;
    }

    public int getContentCount(Content content){
        return contentCounter.getCounter(content);
    }

    public void setContentCount(Content content, int newValue) {
        this.contentCounter.setCounter(content, newValue);
    }

    public ContentCounter getContentCounter(){
        return contentCounter;
    }

    public Iterator<Map.Entry<Coordinate, Face>> topToBottomIterator() {
        List<Map.Entry<Coordinate, Face>> sortedEntries = new ArrayList<>(grid.entrySet());
        sortedEntries.sort((e1, e2) -> {
            int yCompare = Integer.compare(e2.getKey().getY(), e1.getKey().getY());
            if (yCompare == 0) {
                return Integer.compare(e1.getKey().getX(), e2.getKey().getX());
            }
            return yCompare;
        });

        return sortedEntries.iterator();
    }

    public void setAvailablePositions(ArrayList<Coordinate> availablePositions) {
        this.availablePositions = availablePositions;
    }

    public int getMinX(){
        int x = 0;
        for (Coordinate coord : this.grid.keySet()) {
            if (coord.getX() < x)
                x = coord.getX();
        }
        return x;
    }

    public int getMinY(){
        int y = 0;
        for (Coordinate coord : this.grid.keySet()) {
            if(coord.getY() < y)
                y = coord.getY();
        }
        return y;
    }

    public int getMaxX(){
        int x = 0;
        for (Coordinate coord : this.grid.keySet()) {
            if(coord.getX() > x)
                x = coord.getX();
        }
        return x;
    }

    public int getMaxY(){
        int y = 0;
        for (Coordinate coord : this.grid.keySet()) {
            if(coord.getY() > y)
                y = coord.getY();
        }
        return y;
    }


}
