package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ExceptionMessages;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class GameFullMessage extends Message {
    public GameFullMessage(int clientID) {
        super(clientID, MessageType.GAME_FULL);
    }
}
