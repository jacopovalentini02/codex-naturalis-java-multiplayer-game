package it.polimi.ingsfw.ingsfwproject;

import java.util.*;

public class StructuredObjectiveCard extends ObjectiveCard{
    private Structure structureType;
    private ArrayList<Content> resourceRequested;

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
