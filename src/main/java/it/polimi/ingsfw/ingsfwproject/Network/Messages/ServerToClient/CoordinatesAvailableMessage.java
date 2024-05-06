package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.util.ArrayList;

public class CoordinatesAvailableMessage extends Message {
    ArrayList<Coordinate> coords;
    public CoordinatesAvailableMessage(int clientID, ArrayList<Coordinate> coords) {
        super(clientID, MessageType.COORDINATES_AVAILABLE);
        this.coords=coords;
    }

    public ArrayList<Coordinate> getCoords() {
        return coords;
    }
}
