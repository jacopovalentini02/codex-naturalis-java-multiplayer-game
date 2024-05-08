package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class WantThatColorMessage extends Message {

    String nickname;
    PlayerColor color;
    public WantThatColorMessage(int clientID, PlayerColor color, String nickname) {
        super(clientID, MessageType.WANTED_COLOR);
        this.color=color;
        this.nickname=nickname;
    }
    public PlayerColor getColor() {
        return color;
    }

    public String getNickname() {
        return nickname;
    }
}
