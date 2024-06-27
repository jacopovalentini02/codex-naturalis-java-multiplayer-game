package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

/**
 * Class StructuredObjectiveCard
 *
 * Description: This class represents an objective card that requires a specific structure to be completed. It extends the ObjectiveCard class.
 */
public class StructuredObjectiveCard extends ObjectiveCard {
    private Structure structureType;
    private ArrayList<Content> resourceRequested;

    /**
     * Constructor for the StructuredObjectiveCard class.
     *
     * @param idCard The ID of the card.
     * @param points The points awarded for completing the objective.
     * @param structureType The type of structure required to complete the objective.
     * @param resourceRequested The list of resources required to complete the objective.
     */
    public StructuredObjectiveCard(int idCard, int points, Structure structureType, ArrayList<Content> resourceRequested) {
        super(idCard, points);
        this.structureType = structureType;
        this.resourceRequested = resourceRequested;
    }

    /**
     * @return The type of structure required to complete the objective.
     */
    public Structure getStructureType() {
        return structureType;
    }

    /**
     * Sets the type of structure required to complete the objective.
     *
     * @param structureType The new structure type.
     */
    public void setStructureType(Structure structureType) {
        this.structureType = structureType;
    }

    /**
     * @return The list of resources required to complete the objective.
     */
    public ArrayList<Content> getResourceRequested() {
        return resourceRequested;
    }

    /**
     * Sets the list of resources required to complete the objective.
     *
     * @param resourceRequested The new list of resources.
     */
    public void setResourceRequested(ArrayList<Content> resourceRequested) {
        this.resourceRequested = resourceRequested;
    }

    /**
     * @return A string representation of the StructuredObjectiveCard object.
     */
    @Override
    public String toString() {
        return "StructuredObjectiveCard{" +
                "structureType=" + structureType +
                ", resourceRequested=" + resourceRequested +
                '}';
    }

    /**
     * Verifies if the objective is completed based on the player's ground.
     *
     * @param ground The player's ground to check for the required structure and resources.
     * @return The points awarded for completing the objective.
     */
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

        // Define the offsets and resource types based on the structure type
        switch (structureType) {
            case RIGHT_DIAGONAL:
                firstType = resourceRequested.get(0);
                secondType = resourceRequested.get(1);
                thirdType = resourceRequested.get(2);
                secondCardOffset = new Coordinate(-1, 1);
                thirdCardOffset = new Coordinate(-2, 2);
                break;
            case LEFT_DIAGONAL:
                firstType = resourceRequested.get(0);
                secondType = resourceRequested.get(1);
                thirdType = resourceRequested.get(2);
                secondCardOffset = new Coordinate(1, 1);
                thirdCardOffset = new Coordinate(2, 2);
                break;
            case DOUBLE_UP_LEFT:
                firstType = resourceRequested.get(0);
                secondType = resourceRequested.get(1);
                thirdType = resourceRequested.get(2);
                secondCardOffset = new Coordinate(0, 1);
                thirdCardOffset = new Coordinate(-1, 2);
                break;
            case DOUBLE_UP_RIGHT:
                firstType = resourceRequested.get(0);
                secondType = resourceRequested.get(1);
                thirdType = resourceRequested.get(2);
                secondCardOffset = new Coordinate(0, 1);
                thirdCardOffset = new Coordinate(1, 2);
                break;
            case DOUBLE_DOWN_LEFT:
                firstType = resourceRequested.get(2);
                secondType = resourceRequested.get(0);
                thirdType = resourceRequested.get(1);
                secondCardOffset = new Coordinate(1, 1);
                thirdCardOffset = new Coordinate(1, 2);
                break;
            case DOUBLE_DOWN_RIGHT:
                firstType = resourceRequested.get(2);
                secondType = resourceRequested.get(0);
                thirdType = resourceRequested.get(1);
                secondCardOffset = new Coordinate(-1, 1);
                thirdCardOffset = new Coordinate(-1, 2);
                break;
        }

        CoordinateOrderedSet firstTypeCardsCoordinates = new CoordinateOrderedSet();

        // Iterate over the grid and save coordinates of the first type cards
        for (Coordinate c : grid.keySet()) {
            if (Objects.equals(Card.getType(grid.get(c).getIdCard()), firstType))
                firstTypeCardsCoordinates.addCoordinate(c);
        }

        // Check for the required structure in the player's ground
        for (Coordinate c : firstTypeCardsCoordinates) {
            if (alreadyUsedCoordinates.contains(c)) continue;

            Coordinate secondCardPosition = c.sum(secondCardOffset);
            Coordinate thirdCardPosition = c.sum(thirdCardOffset);

            if ((!grid.containsKey(secondCardPosition) || !grid.containsKey(thirdCardPosition))) continue;

            if (alreadyUsedCoordinates.contains(secondCardPosition) || alreadyUsedCoordinates.contains(thirdCardPosition)) continue;

            if (Objects.equals(Card.getType(grid.get(secondCardPosition).getIdCard()), secondType) &&
                    Objects.equals(Card.getType(grid.get(thirdCardPosition).getIdCard()), thirdType)) {
                foundStructures++;
                alreadyUsedCoordinates.add(c);
                alreadyUsedCoordinates.add(secondCardPosition);
                alreadyUsedCoordinates.add(thirdCardPosition);
            }
        }

        return foundStructures * this.getPoints();
    }
}

/**
 * Class CoordinateOrderedSet
 *
 * Description: This class represents an ordered set of coordinates. It provides methods to add, remove, and check coordinates, as well as iterate over them.
 */
class CoordinateOrderedSet implements Iterable<Coordinate> {
    protected final List<Coordinate> coordinatesList = new ArrayList<>();

    /**
     * Adds a coordinate to the set.
     *
     * @param coordinate The coordinate to add.
     */
    public void addCoordinate(Coordinate coordinate) {
        coordinatesList.add(coordinate);
    }

    /**
     * Removes a coordinate from the set.
     *
     * @param coordinate The coordinate to remove.
     */
    public void removeCoordinate(Coordinate coordinate) {
        coordinatesList.remove(coordinate);
    }

    /**
     * @return Whether the set contains the specified coordinate.
     */
    public boolean containsCoordinate(Coordinate coordinate) {
        return this.coordinatesList.contains(coordinate);
    }

    /**
     * @return An iterator over the coordinates in the set, sorted by the Y coordinate.
     */
    public Iterator<Coordinate> iterator() {
        coordinatesList.sort(Comparator.comparingInt(Coordinate::getY));
        return coordinatesList.iterator();
    }
}
