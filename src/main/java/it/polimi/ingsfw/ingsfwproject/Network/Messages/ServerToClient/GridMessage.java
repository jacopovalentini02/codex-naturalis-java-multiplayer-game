package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Model.Face;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.util.Map;

public class GridMessage extends Message {
    private Map<Coordinate, Face> grid;
    private String nickName;
    public GridMessage(int clientID, Map<Coordinate, Face> grid, String nickname) {
        super(clientID, MessageType.GRID);
        this.grid=grid;
        this.nickName=nickname;
    }

    public Map<Coordinate, Face> getGrid() {
        return grid;
    }

    public String getNickName() {
        return nickName;
    }
}
