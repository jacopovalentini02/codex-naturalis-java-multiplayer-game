package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;

/**
 * Message from client to server indicating that a player has picked a displayed playable card.
 */
public class PickMessage extends ClientToServerMessage {
    String nickname;
    int cardID;

    /**
     * Constructs a PickMessage with the specified parameters.
     *
     * @param clientID the ID of the client sending the message
     * @param cardID   the ID of the displayed playable card picked by the player
     * @param nickname the nickname of the player who picked the card
     */
    public PickMessage(int clientID, int cardID, String nickname) {
        super(clientID, false);
        this.cardID=cardID;
        this.nickname=nickname;
    }

    /**
     * Handle the player's pick of a displayed playable card.
     *
     * @param controller the controller on which to execute the message
     */
    @Override
    public void execute(Controller controller) {
        GameController gameController=(GameController)  controller;
        gameController.drawDisplayedPlayableCard(this.nickname,this.cardID);

    }
}
