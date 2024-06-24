package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message from server to client notifying available coordinates for placing cards.
 */
public class CoordinatesAvailableMessage extends ServerToClientMessage implements Serializable {
    ArrayList<Coordinate> coords;

    /**
     * Constructs a CoordinatesAvailableMessage with the specified parameters.
     *
     * @param clientID the ID of the client to whom this message is directed
     * @param coords   the list of available coordinates
     */
    public CoordinatesAvailableMessage(int clientID, ArrayList<Coordinate> coords) {
        super(clientID);
        this.coords=coords;
    }

    /**
     * Updates the client's virtual view with the available coordinates.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setAvailablePositions(coords);
        client.getView().notifyAvailablePositions(coords);
    }
}
