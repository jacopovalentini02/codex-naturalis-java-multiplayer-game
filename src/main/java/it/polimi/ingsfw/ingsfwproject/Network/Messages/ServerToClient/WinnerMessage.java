package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class WinnerMessage extends Message {
    private String nickname;
    public WinnerMessage(int clientID, String nickname) {
        super(clientID, MessageType.WINNER);
        this.nickname=nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
