package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message sent from server to client to update the hand cards of a player.
 */
public class HandCardsMessage extends ServerToClientMessage implements Serializable {
    private final ArrayList<PlayableCard> handCards;

    /**
     * Constructs a HandCardsMessage with the specified client ID and hand cards.
     *
     * @param clientID the ID of the client receiving the message
     * @param handCards the updated hand cards of the player
     */
    public HandCardsMessage(int clientID, ArrayList<PlayableCard> handCards) {
        super(clientID);
        this.handCards=handCards;
    }

    /**
     * Sets the hand cards for the client's virtual view.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setHandCards(handCards);
        client.getView().notifyHandCardsUpdate(handCards);
    }
}
