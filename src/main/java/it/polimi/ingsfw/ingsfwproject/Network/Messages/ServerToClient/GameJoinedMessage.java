package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.io.Serializable;

//Server-Client. Client need to create its client model and set game state to WAITING
public class GameJoinedMessage extends Message implements Serializable {
    private int gameId;
    private String nickName;


    //QUANDO LO RICEVO DA CLIENT CREO IL CLIENT MODEL E SETTO STATE GAME A WAITING
    public GameJoinedMessage(int clientID, int gameId, String nickname) {
        super(clientID, MessageType.GAME_JOINED);
        this.gameId=gameId;
        this.nickName=nickname;
    }


    public int getGameId() {
        return gameId;
    }

    public String getNickName() {
        return nickName;
    }
}
