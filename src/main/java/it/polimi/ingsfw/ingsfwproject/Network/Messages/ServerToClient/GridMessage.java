package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Model.Face;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.Map;

/**
 * Message sent from server to client to update the grid for a specific player.
 */
public class GridMessage extends ServerToClientMessage implements Serializable {
    private final Map<Coordinate, Face> grid;
    private final String nickName;

    /**
     * Constructs a GridMessage with the specified client ID, grid data, and player nickname.
     *
     * @param clientID the ID of the client receiving the message
     * @param grid the updated grid data
     * @param nickname the nickname of the player whose grid is updated
     */
    public GridMessage(int clientID, Map<Coordinate, Face> grid, String nickname) {
        super(clientID);
        this.grid=grid;
        this.nickName=nickname;
    }

    /**
     * Sets the grid data for the specified player in the client's virtual view.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setGridForPlayer(nickName, grid);
        client.getView().notifyGridUpdate(nickName, grid);

    }
}
