package it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server.GameInterface;
import it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server.RmiHandler;

import java.io.Serializable;

//Server-Client. Client need to create its client model and set game state to WAITING
public class GameJoinedMessage extends Message implements Serializable {
    int gameId;

    GameInterface handler = null;

    //QUANDO LO RICEVO DA CLIENT CREO IL CLIENT MODEL E SETTO STATE GAME A WAITING
    public GameJoinedMessage(int gameId) {
        super(MessageType.GAME_JOINED);
        this.gameId=gameId;
    }

    public GameJoinedMessage(int gameId, GameInterface RMIHandler){
        super(MessageType.GAME_JOINED);
        this.gameId = gameId;
        this.handler = RMIHandler;
    }

    public int getGameId() {
        return gameId;
    }
}
