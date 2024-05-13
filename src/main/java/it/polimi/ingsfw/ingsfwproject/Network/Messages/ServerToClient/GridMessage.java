package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Model.Face;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.Map;

public class GridMessage extends ServerToClientMessage implements Serializable {
    private final Map<Coordinate, Face> grid;
    private final String nickName;
    public GridMessage(int clientID, Map<Coordinate, Face> grid, String nickname) {
        super(clientID);
        this.grid=grid;
        this.nickName=nickname;
    }

    public Map<Coordinate, Face> getGrid() {
        return grid;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setGridForPlayer(nickName, grid);
        client.getView().notifyGridUpdate(nickName, grid);

    }
}
