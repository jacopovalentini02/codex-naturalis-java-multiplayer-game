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

            case LEFT_DIAGONAL -> DiagonalSearch(card.getResourceRequested(), gameArea, true);
            case RIGHT_DIAGONAL -> DiagonalSearch(card.getResourceRequested(), gameArea, false);
            case DOUBLE_UP_LEFT -> doubleStructureSearch(card.getResourceRequested(), gameArea, 1);
            case DOUBLE_UP_RIGHT -> doubleStructureSearch(card.getResourceRequested(), gameArea, 0);
            case DOUBLE_DOWN_LEFT -> doubleStructureSearch(card.getResourceRequested(), gameArea, 3);
            case DOUBLE_DOWN_RIGHT -> doubleStructureSearch(card.getResourceRequested(), gameArea,2);

        };
        givenPoints = foundStrcutures * card.getPoints();
        return givenPoints;
    }

    int DiagonalSearch(ArrayList<Content> objectsRequested, Map<Coordinate, Face> gameArea, boolean leftDiagonal) {
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

            Coordinate upperPosition;
            Coordinate secondUpperPosition;

            //decision on which position to iterate, based on the structure we're searching
            if (leftDiagonal) { //left diagonal
                upperPosition = new Coordinate(c.getX() - 1, c.getY() + 1);
                secondUpperPosition = new Coordinate(upperPosition.getX() - 1, upperPosition.getY() + 1);
            } else  { // right diagonal
                upperPosition = new Coordinate(c.getX() + 1, c.getY() + 1);
                secondUpperPosition = new Coordinate(upperPosition.getX() + 1, upperPosition.getY() + 1);
            }

            if (checkedCoordinates.coordinatesList.contains(upperPosition) || checkedCoordinates.coordinatesList.contains(secondUpperPosition))
                continue;

            if(cardsToCheck.coordinatesList.contains(upperPosition) && cardsToCheck.coordinatesList.contains(secondUpperPosition)){
                foundDiagonals++;
                checkedCoordinates.coordinatesList.add(c);
                checkedCoordinates.coordinatesList.add(upperPosition);
                checkedCoordinates.coordinatesList.add(secondUpperPosition);
            }
        }
        return foundDiagonals;
    }


    int doubleStructureSearch(ArrayList<Content> objectsRequested, Map<Coordinate, Face> gameArea, int structureType){
        Content firstTypetoSearch = objectsRequested.getFirst();
        Content secondTypeToSearch = objectsRequested.getLast();
        int foundStructures = 0;
        CoordinateOrderedSet firstTypeCardsToCheck = new CoordinateOrderedSet();
        CoordinateOrderedSet secondTypeCardsToCheck = new CoordinateOrderedSet();
        CoordinateOrderedSet checked = new CoordinateOrderedSet();

        //riempimento dei set con carte del primo e secondo tipo
        for (Coordinate c: gameArea.keySet()){
            if (Objects.equals(Card.getType(gameArea.get(c).getIdCard()), firstTypetoSearch)){
                firstTypeCardsToCheck.addCoordinate(c);
            }
            if (Objects.equals(Card.getType(gameArea.get(c).getIdCard()), secondTypeToSearch)){
                secondTypeCardsToCheck.addCoordinate(c);
            }
        }

        for (Coordinate c: firstTypeCardsToCheck){
            if (checked.coordinatesList.contains(c)) continue;

            Coordinate upperPosition = new Coordinate(c.getX(), c.getY()+1);
            Coordinate diagonalPosition = null;

            switch (structureType){
                case 0: //double-up right
                    diagonalPosition = new Coordinate(upperPosition.getX()+1, upperPosition.getY()+1);
                    break;
                case 1: //double up left
                    diagonalPosition = new Coordinate(upperPosition.getX()-1, upperPosition.getY()+1);
                    break;
                case 2: //double down right
                    diagonalPosition = new Coordinate(c.getX()+1, c.getY()-1);
                    break;
                case 3: //double down left
                    diagonalPosition = new Coordinate(c.getX()-1, c.getY()-1);
            }

            if (checked.coordinatesList.contains(upperPosition) || checked.coordinatesList.contains(diagonalPosition))
                continue;

            if (firstTypeCardsToCheck.coordinatesList.contains(upperPosition) && secondTypeCardsToCheck.coordinatesList.contains(diagonalPosition)){
                foundStructures++;
                checked.coordinatesList.add(c);
                checked.coordinatesList.add(upperPosition);
                checked.coordinatesList.add(diagonalPosition);
            }
        }

        return foundStructures;
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