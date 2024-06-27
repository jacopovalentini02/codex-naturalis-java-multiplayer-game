package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.NotEnoughResourcesException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.PositionNotAvailableException;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * The {@code PlayerGround} class represents the play area of a player in the game.
 * It has a {@code ContentCounter} to keep track of the visible resources and objects on the play area, a {@code Map} to keep
 * track of the played cards and a {@code ArrayList} to keep track of the available positions in the play area.
 */

public class PlayerGround implements Serializable {

    @Serial
    private static final long serialVersionUID = -5738294509649963404L;
    private final ContentCounter contentCounter;
    private Map<Coordinate, Face> grid;
    private final ArrayList<Coordinate> availablePositions;

    /**
     * Constructs a {@code PlayerGround} instance and initialize the {@code ContentCounter}, the {@code Map} for played cards, and
     * the {@code ArrayList} for available positions, adding the {@code Coordinate} (0,0) to it.
     */
    public PlayerGround(){
        contentCounter = new ContentCounter();
        grid = new LinkedHashMap<>();
        availablePositions = new ArrayList<>();
        availablePositions.add(new Coordinate(0,0));
    }

    /**
     * This method is used to add a card in the play area with the specified orientation and in the specified position.
     * It updates the {@code ContentCounter}, the available positions {@code Map} and calculates the points given by this played.
     * @param card the {@code PlayableCard} to be played.
     * @param upwards a boolean indicating whether to play the card face up (true) or face down (false).
     * @param coord the {@code Coordinate} where the card will be placed.
     * @return the number of points earned by playing the card (0 if the card doesn't give points).
     * @throws NotEnoughResourcesException if there are not enough resources in the play area to play the card.
     * @throws PositionNotAvailableException if the specified position is not available for placing a card.
     */
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

    /**
     * This method checks if a card can be played in the specified {@code Coordinate}.
     * @param card the {@code PlayableCard} to be played.
     * @param upwards a boolean indicating whether to play the card face up (true) or face down (false).
     * @param coord the {@code Coordinate} where there will be the check.
     * @throws PositionNotAvailableException if there are not enough resources in the play area to play the card.
     * @throws NotEnoughResourcesException if the specified position is not available for placing a card.
     */
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

    /**
     * This method modifies the {@code ContentCounter} by adding the new resources and objects brought by the face played.
     * @param facePlayed the {@code Face} played that modifies the counters
     */
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

    /**
     * This method updates the grid's available positions by:
     * <ul>
     *   <li>Removing the specified {@code Coordinate} where a face was played.</li>
     *   <li>Adding new positions created by the played face to the available ones.</li>
     * </ul>
     * Additionally, this method counts and marks the corners covered by the played face
     * (for scoring purposes based on covered corners).
     * @param coord where the card has been played.
     * @return the number of corners covered by the played card.
     */
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
                    else if(i==-1) //&& j==1 //top left card
                        corner = facePlayed.getTL();
                    else if(i==1 && j==-1) //bottom right card
                        corner = facePlayed.getBR();
                    else //if(i==1 && j==1) //top right card
                        corner = facePlayed.getTR();

                    if(checkIfCanBeAvailable(check)) {
                        if (corner != Content.HIDDEN && !availablePositions.contains(check)) {
                            availablePositions.add(check);
                        }
                    }else{
                        availablePositions.remove(check);
                    }
                }else{
                    counter++;

                    if(i==-1 && j==-1) { //bottom left card
                        contentCounter.decrementCounter(grid.get(check).getTR());
                        grid.get(check).setCoveredTR();
                    }
                    else if(i==-1) { // && j==1 //top left card
                        contentCounter.decrementCounter(grid.get(check).getBR());
                        grid.get(check).setCoveredBR();
                    }
                    else if(i==1 && j==-1) { //bottom right card
                        contentCounter.decrementCounter(grid.get(check).getTL());
                        grid.get(check).setCoveredTL();
                    }
                    else /*if(i==1 && j==1)*/ { //top right card
                        contentCounter.decrementCounter(grid.get(check).getBL());
                        grid.get(check).setCoveredBL();
                    }
                }
            }
        }
        return counter;
    }

    /**
     * This method checks if a {@code Coordinate} can become an available position.
     * It is useful in those cases where, iterating on the played face's corners, a position seems to be available because
     * the face has a corner there, but in the same position another card has a "hidden corner" which cannot accommodate other cards.
     * @param coord the coordinate to check.
     * @return {@code true} if the {@code Coordinate} passed as a parameter can become an available position, {@code false} otherwise.
     */
    public boolean checkIfCanBeAvailable(Coordinate coord){
        Coordinate check;
        Content corner;
        for(int i=-1; i<=1; i=i+2) {
            for (int j = -1; j <= 1; j = j + 2) {
                check = new Coordinate(coord.getX() + i, coord.getY() + j);
                if (grid.containsKey(check)) {
                    if (i == -1 && j == -1) //bottom left card
                        corner = grid.get(check).getTR();
                    else if (i == -1 /*&& j == 1*/) //top left card
                        corner = grid.get(check).getBR();
                    else if (i == 1 && j == -1) //bottom right card
                        corner = grid.get(check).getTL();
                    else //if (i == 1 && j == 1) //top right card
                        corner = grid.get(check).getBL();

                    if (corner == Content.HIDDEN)
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * This method calculates and returns the point given by a played face.
     * @param face the face played.
     * @param coveredCorners the corners covered by the card in playerground.
     * @return the points given by the face.
     */
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

    /**
     * This method returns an {@code ArrayList} with the available positions.
     * @return an {@code ArrayList} with the available positions.
     */
    public ArrayList<Coordinate> getAvailablePositions() {
        return availablePositions;
    }

    /**
     * This method returns a {@code Map} with the player's grid.
     * @return a {@code Map} with the player's grid.
     */
    public Map<Coordinate, Face> getGrid(){
        return grid;
    }

    /**
     * This method returns the count of the objects or resources passed as a parameter.
     * @param content the {@code Content} whose count you want to know.
     * @return the count of the {@code Content} passed as a parameter.
     */
    public int getContentCount(Content content){
        return contentCounter.getCounter(content);
    }

    /**
     * Sets the count of the specified {@code Content} to the given value.
     * @param content the {@code Content} whose count is to be set.
     * @param newValue the new count value to be assigned to the {@code Content}.
     */
    public void setContentCount(Content content, int newValue) {
        this.contentCounter.setCounter(content, newValue);
    }


}
