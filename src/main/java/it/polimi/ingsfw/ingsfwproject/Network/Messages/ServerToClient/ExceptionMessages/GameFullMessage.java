package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.MessageType;

public class GameFullMessage extends Message {
    public GameFullMessage() {
        super(MessageType.GAME_FULL);
    }
}
