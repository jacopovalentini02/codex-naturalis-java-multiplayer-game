package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;

/**
 * Message from client to server indicating the chosen color.
 */
public class WantThatColorMessage extends ClientToServerMessage {
    String nickname;
    PlayerColor color;

    /**
     * Constructs a WantThatColorMessage with the specified parameters.
     *
     * @param clientID  the ID of the client sending the message
     * @param nickname  the nickname of the client wanting to choose the color
     * @param color     the desired PlayerColor chosen by the client
     */
    public WantThatColorMessage(int clientID, String nickname, PlayerColor color) {
        super(clientID, false);
        this.color=color;
        this.nickname=nickname;
    }

    /**
     * Handle the color choice for the specified player.
     *
     * @param controller the controller on which to execute the message
     */
    @Override
    public void execute(Controller controller){
        GameController gameController=(GameController)  controller;
        gameController.chooseColor(this.nickname,this.color);

    }
}
