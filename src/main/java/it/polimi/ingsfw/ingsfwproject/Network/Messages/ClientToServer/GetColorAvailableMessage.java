package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ColorAvailableMessage;

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
        //gameController.sendTokenAvailable(super.getClientID());
        gameController.getModel().getGameServerInstance().sendUpdateToAll(new ColorAvailableMessage(super.getClientID(), gameController.getModel().getTokenAvailable()));


    }
}
