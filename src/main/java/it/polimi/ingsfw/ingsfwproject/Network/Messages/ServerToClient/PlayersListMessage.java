package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.util.ArrayList;

public class PlayersListMessage extends Message {
    private ArrayList<String> nicknames;

    public PlayersListMessage(int clientID, ArrayList<String> nicknames) {
        super(clientID, MessageType.NEW_PLAYER_JOINED);
        this.nicknames = nicknames;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }
}
