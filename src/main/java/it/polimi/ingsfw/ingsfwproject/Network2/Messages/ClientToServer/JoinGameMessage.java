package it.polimi.ingsfw.ingsfwproject.Network2.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.MessageType;

import java.io.Serializable;

public class JoinGameMessage extends Message implements Serializable {
    private String nickname;
    private int gameID;

    public JoinGameMessage(String nickname, int gameID) {
        super(MessageType.JOIN_GAME);
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
