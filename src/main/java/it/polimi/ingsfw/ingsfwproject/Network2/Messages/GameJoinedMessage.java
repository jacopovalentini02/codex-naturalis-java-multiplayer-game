package it.polimi.ingsfw.ingsfwproject.Network2.Messages;

import java.io.Serializable;

//Server-Client. Client need to create its client model and set game state to WAITING
public class GameJoinedMessage extends Message implements Serializable {
    int gameId;
    //QUANDO LO RICEVO DA CLIENT CREO IL CLIENT MODEL E SETTO STATE GAME A WAITING
    public GameJoinedMessage(int gameId) {
        super(MessageType.GAME_JOINED);
        this.gameId=gameId;
    }

    public int getGameId() {
        return gameId;
    }
}
