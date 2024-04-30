package it.polimi.ingsfw.ingsfwproject.Network2.Messages;

import java.io.Serializable;

//Server-Client. Client need to create its client model and set game state to WAITING
public class GameCreatedMessage extends Message implements Serializable {

    //QUANDO LO RICEVO DA CLIENT CREO IL CLIENT MODEL E SETTO STATE GAME A WAITING
    public GameCreatedMessage() {
        super(MessageType.GAME_CREATED);
    }
}
