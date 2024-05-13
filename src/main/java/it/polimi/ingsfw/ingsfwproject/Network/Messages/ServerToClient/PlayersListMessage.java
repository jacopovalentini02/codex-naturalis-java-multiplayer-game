package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayersListMessage extends ServerToClientMessage implements Serializable {
    private final ArrayList<String> nicknames;

    public PlayersListMessage(int clientID, ArrayList<String> nicknames) {
        super(clientID);
        this.nicknames = nicknames;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setListOfPlayers(nicknames);
        client.getView().notifyNewPlayerJoined(nicknames);
    }
}
