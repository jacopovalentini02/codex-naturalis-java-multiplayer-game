package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message sent from server to client to update the list of player nicknames.
 */
public class PlayersListMessage extends ServerToClientMessage implements Serializable {
    private final ArrayList<String> nicknames;

    /**
     * Constructs a PlayersListMessage with the specified client ID and list of player nicknames.
     *
     * @param clientID the ID of the client receiving the message
     * @param nicknames the updated list of player nicknames
     */
    public PlayersListMessage(int clientID, ArrayList<String> nicknames) {
        super(clientID);
        this.nicknames = nicknames;
    }

    /**
     * Sets the list of player nicknames in the client's virtual view.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setListOfPlayers(nicknames);
        client.getView().notifyNewPlayerJoined(nicknames);
    }
}
