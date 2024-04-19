package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

public class StructuredObjectiveCard extends ObjectiveCard{
    private Structure structureType;
    private ArrayList<Content> resourceRequested;

    public StructuredObjectiveCard(int idCard, int points, Structure structureType, ArrayList<Content> resourceRequested) {
        super(idCard, points);
        this.structureType = structureType;
        this.resourceRequested = resourceRequested;
    }

    public Structure getStructureType() {
        return structureType;
    }
    public void setStructureType(Structure structureType) {
        this.structureType = structureType;
    }

    public ArrayList<Content> getResourceRequested() {
        return resourceRequested;
    }

    public void setResourceRequested(ArrayList<Content> resourceRequested) {
        this.resourceRequested = resourceRequested;
    }

    @Override
    public String toString() {
        return "StructuredObjectiveCard{" +
                "structureType=" + structureType +
                ", resourceRequested=" + resourceRequested +
                '}';
    }
//TODO: right e left diagonal sono invertite nel json?
    @Override
    public int verifyObjective(PlayerGround ground) {
        Map<Coordinate, Face> grid = ground.getGrid();
        Content firstType = null;
        Content secondType = null;
        Content thirdType = null;
        Coordinate secondCardOffset = null;
        Coordinate thirdCardOffset = null;
        HashSet<Coordinate> alreadyUsedCoordinates = new HashSet<>();
        int foundStructures = 0;
        switch (structureType){
            case RIGHT_DIAGONAL:
                firstType = resourceRequested.get(0);
                secondType = resourceRequested.get(1);
                thirdType = resourceRequested.get(2);
                secondCardOffset =new Coordinate(-1,1);
                thirdCardOffset = new Coordinate(-2,2);
                break;
            case LEFT_DIAGONAL:
                firstType = resourceRequested.get(0);
                secondType = resourceRequested.get(1);
                thirdType = resourceRequested.get(2);
                secondCardOffset =new Coordinate(1,1);
                thirdCardOffset = new Coordinate(2,2);
                break;
            case DOUBLE_UP_LEFT:
                firstType = resourceRequested.get(0);
                secondType = resourceRequested.get(1);
                thirdType = resourceRequested.get(2);
                secondCardOffset =new Coordinate(0,1);
                thirdCardOffset =new Coordinate(-1,2);
                break;
            case DOUBLE_UP_RIGHT:
                firstType = resourceRequested.get(0);
                secondType = resourceRequested.get(1);
                thirdType = resourceRequested.get(2);
                secondCardOffset = new Coordinate(0,1);
                thirdCardOffset = new Coordinate(1,2);
                break;
            case DOUBLE_DOWN_LEFT:
                firstType = resourceRequested.get(2); //the lowest card to search for will be the different one
                secondType = resourceRequested.get(0);
                thirdType = resourceRequested.get(1);
                secondCardOffset = new Coordinate(1,1);
                thirdCardOffset = new Coordinate(1,2);
                break;
            case DOUBLE_DOWN_RIGHT:
                firstType = resourceRequested.get(2); //the lowest card to search for will be the different one
                secondType = resourceRequested.get(0);
                thirdType = resourceRequested.get(1);
                secondCardOffset = new Coordinate(-1,1);
                thirdCardOffset = new Coordinate(-1,2);
                break;
        }

        CoordinateOrderedSet firstTypeCardsCoordinates = new CoordinateOrderedSet();

        for (Coordinate c: grid.keySet()){ //iterate on the grid and save first type cards' coordinates
            if (Objects.equals(Card.getType(grid.get(c).getIdCard()), firstType))
                firstTypeCardsCoordinates.addCoordinate(c);
        }

        for (Coordinate c: firstTypeCardsCoordinates){
            if (alreadyUsedCoordinates.contains(c)) continue;

            Coordinate secondCardPosition = c.sum(secondCardOffset);
            Coordinate thirdCardPosition = c.sum(thirdCardOffset);


            if ((!grid.containsKey(secondCardPosition) || !grid.containsKey(thirdCardPosition))) continue;

            if (alreadyUsedCoordinates.contains(secondCardPosition) || alreadyUsedCoordinates.contains(thirdCardPosition)) continue;

            if (Objects.equals(Card.getType(grid.get(secondCardPosition).getIdCard()), secondType) && Objects.equals(Card.getType(grid.get(thirdCardPosition).getIdCard()), thirdType)){
                foundStructures++;
                alreadyUsedCoordinates.add(c);
                alreadyUsedCoordinates.add(secondCardPosition);
                alreadyUsedCoordinates.add(thirdCardPosition);
            }

        }

        return foundStructures * this.getPoints();
    }
}

class CoordinateOrderedSet implements Iterable<Coordinate>{
    protected final List<Coordinate> coordinatesList = new ArrayList<Coordinate>();

    public void addCoordinate(Coordinate coordinate){
        coordinatesList.add(coordinate);
    }

    public void removeCoordinate(Coordinate coordinate){
        coordinatesList.remove(coordinate);
    }

    public boolean containsCoordinate(Coordinate coordinate){return this.coordinatesList.contains(coordinate);}

    public Iterator<Coordinate> iterator(){
        coordinatesList.sort(Comparator.comparingInt(Coordinate::getY));
        return coordinatesList.iterator();
    }
}
