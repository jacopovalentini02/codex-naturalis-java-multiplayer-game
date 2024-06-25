package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;

/**
 * Message from client to server indicating that a player has chosen an objective card.
 */
public class ObjectiveCardChosenMessage extends ClientToServerMessage {
    String nickname;
    int cardID;

    /**
     * Constructs an ObjectiveCardChosenMessage with the specified parameters.
     *
     * @param clientID the ID of the client sending the message
     * @param nickname the nickname of the player choosing the objective card
     * @param cardID   the ID of the objective card chosen
     */
    public ObjectiveCardChosenMessage(int clientID, String nickname, int cardID) {
        super(clientID, false);
        this.nickname=nickname;
        this.cardID=cardID;
    }

//    public String getNickname() {
//        return nickname;
//    }

    /**
     * Executes the objective card chosen message on the specified controller.
     * Notifies the GameController to process the player's choice of an objective card.
     *
     * @param controller the controller on which to execute the message
     */
    @Override
    public void execute(Controller controller) {
        GameController gameController=(GameController) controller;
        gameController.chooseObjectiveCard(this.nickname,this.cardID);
    }
}
