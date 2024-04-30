package it.polimi.ingsfw.ingsfwproject.Network2.Messages;

import java.io.Serializable;

//Message from client to server to request a new game creation
public class CreateGameMessage extends Message implements Serializable {
    private int numPlayer;
    private String nickname;

    public CreateGameMessage(int numPlayer, String nickname) {
        super(MessageType.CREATE_GAME);
        this.numPlayer = numPlayer;
        this.nickname = nickname;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
