package it.polimi.ingsfw.ingsfwproject.Network2.Messages;

import it.polimi.ingsfw.ingsfwproject.Model.Game;

import java.io.Serializable;
import java.util.HashMap;

public class SendGameList extends Message implements Serializable {
    private HashMap<Integer, Game> gameList;
    public SendGameList(MessageType messageType) {
        super(messageType);
    }
}
