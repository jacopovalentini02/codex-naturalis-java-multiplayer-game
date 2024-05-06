package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.io.Serializable;

public class GetGameListMessage extends Message implements Serializable {
    public GetGameListMessage(int clientID) {
        super(clientID, MessageType.GET_GAME_LIST);
    }
}
