package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;


/**
 * Message sent from server to client to announce the winner of the game.
 */
public class WinnerMessage extends ServerToClientMessage implements Serializable {
    private final String nickname;

    /**
     * Constructs a WinnerMessage with the specified client ID and winner's nickname.
     *
     * @param clientID the ID of the client receiving the message
     * @param nickname the nickname of the player who won the game
     */
    public WinnerMessage(int clientID, String nickname) {
        super(clientID);
        this.nickname=nickname;
    }

    /**
     * Sets the winner in the client's virtual view
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setWinner(nickname);
        client.getView().notifyWinnerUpdate(nickname);
    }
}
