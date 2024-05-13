package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.ObjectiveCard;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.List;

public class DisplayedObjectiveMessage extends ServerToClientMessage implements Serializable {
    private final List<ObjectiveCard> displayedObjectiveCard;
    public DisplayedObjectiveMessage(int clientID, List<ObjectiveCard> displayedObjectiveCard) {
        super(clientID);
        this.displayedObjectiveCard=displayedObjectiveCard;
    }

    public List<ObjectiveCard> getDisplayedObjectiveCard() {
        return displayedObjectiveCard;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setDisplayedObjectiveCards(displayedObjectiveCard);
        client.getView().notifyDisplayedObjectives(displayedObjectiveCard);
    }
}
