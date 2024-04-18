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
}
