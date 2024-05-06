package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class WantThatColorMessage extends Message {


    PlayerColor color;
    public WantThatColorMessage(int clientID, PlayerColor color) {
        super(clientID, MessageType.WANTED_COLOR);
        this.color=color;
    }
    public PlayerColor getColor() {
        return color;
    }
}
