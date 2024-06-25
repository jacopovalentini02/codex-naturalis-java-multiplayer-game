package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message from server to client containing the displayed playable cards.
 */
public class DisplayedPlayableCardsMessage extends ServerToClientMessage implements Serializable {
    private final ArrayList<PlayableCard> displayedPlayableCard;

    /**
     * Constructs a DisplayedPlayableCardsMessage with the specified parameters.
     *
     * @param clientID                the ID of the client to whom this message is directed
     * @param displayedPlayableCard   the list of displayed playable cards
     */
    public DisplayedPlayableCardsMessage(int clientID, ArrayList<PlayableCard> displayedPlayableCard) {
        super(clientID);
        this.displayedPlayableCard=displayedPlayableCard;
    }

    /**
     * Updates the client's virtual view with the displayed playable cards.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setDisplayedCards(displayedPlayableCard);
        client.getView().notifyDisplayedCardsUpdate(displayedPlayableCard);
    }
}
