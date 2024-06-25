package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

/**
 * Message from server to client notifying that a player has chosen a color.
 */
public class ColorChosenMessage extends ServerToClientMessage implements Serializable {
    PlayerColor color;
    String nickname;

    /**
     * Constructs a ColorChosenMessage with the specified parameters.
     *
     * @param clientID the ID of the client to whom this message is directed
     * @param color    the PlayerColor chosen by the player
     * @param nickname the nickname of the player who chose the color
     */
    public ColorChosenMessage(int clientID, PlayerColor color, String nickname) {
        super(clientID);
        this.color=color;
        this.nickname=nickname;
    }

    /**
     * Notify every player regarding the color chosen.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        if(client.getNickname().equals(nickname))
            client.getVirtualView().setColor(color);

        client.getVirtualView().getPlayerColorMap().put(nickname,color);
        client.getView().notifyColorChosen(color);
    }
}
