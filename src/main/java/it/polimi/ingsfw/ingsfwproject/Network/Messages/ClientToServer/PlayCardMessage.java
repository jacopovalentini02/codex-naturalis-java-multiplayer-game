package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
/**
 * Message from client to server indicating that a player wants to play a card.
 */
public class PlayCardMessage extends ClientToServerMessage {
    String nickname;
    int cardID;
    boolean face;
    Coordinate coordinate;

    /**
     * Constructs a PlayCardMessage with the specified parameters.
     *
     * @param clientID   the ID of the client sending the message
     * @param card     the ID of the card the player wants to play
     * @param face       whether the card should be played face up (true) or face down (false)
     * @param coordinate the coordinate on the board where the card should be played
     * @param nickname   the nickname of the player who wants to play the card
     */
    public PlayCardMessage(int clientID, int card, boolean face, Coordinate coordinate, String nickname) {
        super(clientID, false);
        this.cardID = card;
        this.face=face;
        this.coordinate=coordinate;
        this.nickname=nickname;
    }

    /**
     * Handle the player's request to play a card.
     *
     * @param controller the controller on which to execute the message
     */
    @Override
    public void execute(Controller controller)  {
        GameController gameController=(GameController)  controller;
        gameController.playCard(this.nickname,this.cardID,this.face,this.coordinate);

    }
}
