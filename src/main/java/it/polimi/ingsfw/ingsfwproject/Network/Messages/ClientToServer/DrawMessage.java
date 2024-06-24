package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;
import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;

/**
 * Message from client to server to request to draw a card
 */
public class DrawMessage extends ClientToServerMessage {
    String nickname;
    boolean resourceDeck;

    /**
     * Constructs a DrawMessage with the specified client ID, nickname, and deck type.
     *
     * @param clientID     the ID of the client sending the message
     * @param nickname     the nickname of the player drawing the card
     * @param resourceDeck true if the draw is from the resource deck, false if from the gold deck
     */
    public DrawMessage(int clientID, String nickname, boolean resourceDeck) {
        super(clientID, false);
        this.nickname=nickname;
        this.resourceDeck=resourceDeck;

    }

    /**
     * Gets the nickname of the player drawing the card.
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Draw a card from the specified deck
     *
     * @param controller the controller on which to execute the message
     */
    @Override
    public void execute(Controller controller)  {
        GameController gameController=(GameController) controller;
        gameController.draw(this.nickname,this.resourceDeck);
    }
}
