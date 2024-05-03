package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server.GameInterface;

import java.io.Serializable;

//Server-Client. Client need to create its client model and set game state to WAITING
public class GameJoinedMessage extends Message implements Serializable {
    int gameId;

    GameInterface handler = null;

    //QUANDO LO RICEVO DA CLIENT CREO IL CLIENT MODEL E SETTO STATE GAME A WAITING
    public GameJoinedMessage(int clientID, int gameId) {
        super(clientID, MessageType.GAME_JOINED);
        this.gameId=gameId;
    }

    public GameJoinedMessage(int clientID, int gameId, GameInterface RMIHandler){
        super(clientID, MessageType.GAME_JOINED);
        this.gameId = gameId;
        this.handler = RMIHandler;
    }

    public int getGameId() {
        return gameId;
    }
}
