package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class WantThatColorMessage extends Message {

    public String getNickname() {
        return nickname;
    }

    String nickname;
    PlayerColor color;
    public WantThatColorMessage(int clientID, String nickname, PlayerColor color) {
        super(clientID, MessageType.WANTED_COLOR);
        this.color=color;
        this.nickname=nickname;
    }
    public PlayerColor getColor() {
        return color;
    }

}
