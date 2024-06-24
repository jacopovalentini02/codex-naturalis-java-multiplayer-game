package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.ObjectiveCard;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.List;

/**
 * Message from server to client containing the displayed objective cards.
 */
public class DisplayedObjectiveMessage extends ServerToClientMessage implements Serializable {
    private final List<ObjectiveCard> displayedObjectiveCard;

    /**
     * Constructs a DisplayedObjectiveMessage with the specified parameters.
     *
     * @param clientID                the ID of the client to whom this message is directed
     * @param displayedObjectiveCard  the list of displayed objective cards
     */
    public DisplayedObjectiveMessage(int clientID, List<ObjectiveCard> displayedObjectiveCard) {
        super(clientID);
        this.displayedObjectiveCard=displayedObjectiveCard;
    }

    /**
     * Updates the client's virtual view with the displayed objective cards.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setDisplayedObjectiveCards(displayedObjectiveCard);
        client.getView().notifyDisplayedObjectives(displayedObjectiveCard);
    }
}
