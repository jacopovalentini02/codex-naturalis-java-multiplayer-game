package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

public interface ObjectiveCheckStrategy<T extends ObjectiveCard> {
    public int checkObjective(Player player, T card);

}
class StructuredObjectiveCheckStrategy implements ObjectiveCheckStrategy<StructuredObjectiveCard>{

    static class CoordinateOrderedSet implements Iterable<Coordinate>{
        protected final List<Coordinate> coordinatesList = new ArrayList<Coordinate>();

        public void addCoordinate(Coordinate coordinate){
            coordinatesList.add(coordinate);
        }

        public void removeCordinate(Coordinate coordinate){
            coordinatesList.remove(coordinate);
        }

        public Iterator<Coordinate> iterator(){
            coordinatesList.sort(Comparator.comparingInt(Coordinate::getY));
            return coordinatesList.iterator();
        }
    }
    @Override
    public int checkObjective(Player player, StructuredObjectiveCard card) {
        int givenPoints = 0;
        int foundStrcutures = 0;
        Map<Coordinate, Face> gameArea = player.getGround().getGrid();
        foundStrcutures = switch (card.getStructureType()){

            case LEFT_DIAGONAL -> leftDiagonalSearch(card.getResourceRequested(), gameArea);
            case RIGHT_DIAGONAL -> rightDiagonalSearch(card.getResourceRequested(), gameArea);
            case DOUBLE_UP_LEFT -> doubleUpLeftSearch(card.getResourceRequested(), gameArea);
            case DOUBLE_UP_RIGHT -> doubleUpRightSearch(card.getResourceRequested(), gameArea);
            case DOUBLE_DOWN_LEFT -> doubleDownLeftSearch(card.getResourceRequested(), gameArea);
            case DOUBLE_DOWN_RIGHT -> doubleDownRightSearch(card.getResourceRequested(), gameArea);

        };
        givenPoints = foundStrcutures * card.getPoints();
        return givenPoints;
    }

    int leftDiagonalSearch(ArrayList<Content> objectsRequested, Map<Coordinate, Face> gameArea) {
        //TODO: sistemare, partendo dall'alto o dal basso e proseguendo
        Content typeToSearch = objectsRequested.getFirst();
        int foundDiagonals = 0;
        CoordinateOrderedSet cardsToCheck = new CoordinateOrderedSet();
        CoordinateOrderedSet checkedCoordinates = new CoordinateOrderedSet();
        //riempimento del set con le carte da visitare
        for(Coordinate c : gameArea.keySet()){
            if (Objects.equals(Card.getType(gameArea.get(c).getIdCard()), typeToSearch))
                cardsToCheck.addCoordinate(c);
        }
        //visita delle carte per vedere se sono disposte in diagonale
        for (Coordinate c : cardsToCheck){
            if (checkedCoordinates.coordinatesList.contains(c)) continue;

            Coordinate upperLeftPosition = new Coordinate(c.getX() - 1, c.getY() + 1);
            Coordinate secondUpperLeftPosition = new Coordinate(upperLeftPosition.getX()-1, upperLeftPosition.getY()+1);

            if(cardsToCheck.coordinatesList.contains(upperLeftPosition) && cardsToCheck.coordinatesList.contains(secondUpperLeftPosition)){
                foundDiagonals++;
                checkedCoordinates.coordinatesList.add(c);
                checkedCoordinates.coordinatesList.add(upperLeftPosition);
                checkedCoordinates.coordinatesList.add(secondUpperLeftPosition);
            }
        }
        return foundDiagonals;
    }

    int rightDiagonalSearch(ArrayList<Content> objectsRequested, Map<Coordinate, Face> gameArea){
        return 0;
    }

    int doubleUpLeftSearch(ArrayList<Content> objectsRequested, Map<Coordinate, Face> gameArea){
        return 0;
    }

    int doubleUpRightSearch(ArrayList<Content> objectsRequested, Map<Coordinate, Face> gameArea){
        return 0;
    }

    int doubleDownLeftSearch(ArrayList<Content> objectsRequested, Map<Coordinate, Face> gameArea){
        return 0;
    }

    int doubleDownRightSearch(ArrayList<Content> objectsRequested, Map<Coordinate, Face> gameArea){
        return 0;
    }


}

class UnStructuredObjectiveCheckStrategy implements ObjectiveCheckStrategy<NotStructuredObjectiveCard>{
    @Override
    public int checkObjective(Player player, NotStructuredObjectiveCard card) {
        int givenPoints = 0;

        switch (card.getIdCard()){
            case 95:
                int fungiNumber = player.getGround().getFungiCount();
                givenPoints = (fungiNumber % 3) * card.getPoints();
                break;
            case 96:
                int plantNumber = player.getGround().getPlantCount();
                givenPoints = (plantNumber % 3) * card.getPoints();
                break;
            case 97:
                int animalNumber = player.getGround().getAnimalCount();
                givenPoints = (animalNumber % 3) * card.getPoints();
                break;
            case 98:
                int insectNumber = player.getGround().getInsectCount();
                givenPoints = (insectNumber % 3) * card.getPoints();
                break;
            case 99:
                int minimumNumberOfObjects = Math.min(Math.min(player.getGround().getInkwellCount(), player.getGround().getManuscriptCount()), player.getGround().getQuillCount());
                givenPoints = minimumNumberOfObjects * card.getPoints();
                break;
            case 100:
                int manuscriptNumber = player.getGround().getManuscriptCount();
                givenPoints = (manuscriptNumber % 2) * card.getPoints();
                break;
            case 101:
                int inkwellNumber = player.getGround().getInkwellCount();
                givenPoints = (inkwellNumber % 2) * card.getPoints();
                break;
            case 102:
                int quillNumber = player.getGround().getQuillCount();
                givenPoints = (quillNumber % 2) * card.getPoints();
                break;
            default:
                throw new IllegalArgumentException("Card is not of NotStructuredObjective type");
        }

        return givenPoints;
    }
}