package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;
import it.polimi.ingsfw.ingsfwproject.View.View;

import java.io.Serializable;

//Server-Client. Client need to create its client model and set game state to WAITING
public class GameJoinedMessage extends ServerToClientMessage implements Serializable {
    private final int gameId;
    private final String nickName;


    //QUANDO LO RICEVO DA CLIENT CREO IL CLIENT MODEL E SETTO STATE GAME A WAITING
    public GameJoinedMessage(int clientID, int gameId, String nickname) {
        super(clientID);
        this.gameId=gameId;
        this.nickName=nickname;
    }


    public int getGameId() {
        return gameId;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public void execute(View view) {

    }
}
