package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Model.Game;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

/**
 * Message from client to server to request available colors for tokens.
 */
public class GetColorAvailableMessage extends ClientToServerMessage {
    /**
     * Constructs a GetColorAvailableMessage with the specified client ID.
     *
     * @param clientID the ID of the client sending the message
     */
    public GetColorAvailableMessage(int clientID) {
        super(clientID, false);
    }

    /**
     * Sends the available token colors to the client.
     *
     * @param controller the controller on which to execute the message
     */
    @Override
    public void execute(Controller controller) {
        GameController gameController=(GameController) controller;
        gameController.sendTokenAvailable(super.getClientID());

    }
}
