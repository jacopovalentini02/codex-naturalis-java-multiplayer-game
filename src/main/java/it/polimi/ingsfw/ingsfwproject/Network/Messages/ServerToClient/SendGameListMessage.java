package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Message sent from server to client to provide the list of active games.
 */
public class SendGameListMessage extends ServerToClientMessage implements Serializable {
    private final HashMap<Integer, Integer> gameList;

    /**
     * Constructs a SendGameListMessage with the specified client ID and game list.
     *
     * @param clientID the ID of the client receiving the message
     * @param gameList a map representing the list of active games (game ID to number of players)
     */
    public SendGameListMessage(int clientID, HashMap<Integer, Integer> gameList) {
        super(clientID);
        this.gameList = gameList;
    }

    /**
     * Displays the received game list in the client's view.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getView().displayGameList(gameList);
    }
}
