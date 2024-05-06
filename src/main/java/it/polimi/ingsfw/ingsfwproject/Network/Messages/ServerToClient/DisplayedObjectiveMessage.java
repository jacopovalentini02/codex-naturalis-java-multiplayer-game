package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.ObjectiveCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.util.List;

public class DisplayedObjectiveMessage extends Message {
    private List<ObjectiveCard> displayedObjectiveCard;
    public DisplayedObjectiveMessage(int clientID, List<ObjectiveCard> displayedObjectiveCard) {
        super(clientID, MessageType.DISPLAYED_OBJECTIVE);
        this.displayedObjectiveCard=displayedObjectiveCard;
    }

    public List<ObjectiveCard> getDisplayedObjectiveCard() {
        return displayedObjectiveCard;
    }
}
