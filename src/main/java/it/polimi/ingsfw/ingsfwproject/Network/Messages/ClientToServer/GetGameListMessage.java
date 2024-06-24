package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;

import java.io.Serializable;

/**
 * Message from client to server to request the list of available games.
 */
public class GetGameListMessage extends ClientToServerMessage implements Serializable {
    /**
     * Constructs a GetGameListMessage with the specified client ID.
     *
     * @param clientID the ID of the client sending the message
     */
    public GetGameListMessage(int clientID) {

        super(clientID, true);
    }

    /**
     * Retrieves the list of available games and sends it to the client.
     *
     * @param controller the controller on which to execute the message
     */
    @Override
    public void execute(Controller controller) {
        LobbyController lobbyController=(LobbyController) controller;
        lobbyController.getGameList(this.getClientID());
    }
}
