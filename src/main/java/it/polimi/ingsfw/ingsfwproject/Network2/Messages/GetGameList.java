package it.polimi.ingsfw.ingsfwproject.Network2.Messages;

import java.io.Serializable;

public class GetGameList extends Message implements Serializable {
    public GetGameList(MessageType messageType) {
        super(MessageType.GET_GAME_LIST);
    }
}
