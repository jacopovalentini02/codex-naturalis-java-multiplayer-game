package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.io.Serializable;

public class JoinGameMessage extends Message implements Serializable {
    private String nickname;
    private int gameID;

    public JoinGameMessage(int clientID, String nickname, int gameID) {
        super(clientID, MessageType.JOIN_GAME);
        this.nickname=nickname;
        this.gameID=gameID;
    }

    public String getNickname() {
        return nickname;
    }

    public int getGameID() {
        return gameID;
    }
}
