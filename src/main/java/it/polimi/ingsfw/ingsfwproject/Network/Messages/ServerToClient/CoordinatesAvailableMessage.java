package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class CoordinatesAvailableMessage extends ServerToClientMessage implements Serializable {
    ArrayList<Coordinate> coords;
    public CoordinatesAvailableMessage(int clientID, ArrayList<Coordinate> coords) {
        super(clientID);
        this.coords=coords;
    }

    public ArrayList<Coordinate> getCoords() {
        return coords;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setAvailablePositions(coords);
        client.getView().notifyAvailablePositions(coords);
    }
}
