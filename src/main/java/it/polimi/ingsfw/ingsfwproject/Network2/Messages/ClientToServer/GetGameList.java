package it.polimi.ingsfw.ingsfwproject.Network2.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.MessageType;

import java.io.Serializable;

public class GetGameList extends Message implements Serializable {
    public GetGameList() {
        super(MessageType.GET_GAME_LIST);
    }
}
