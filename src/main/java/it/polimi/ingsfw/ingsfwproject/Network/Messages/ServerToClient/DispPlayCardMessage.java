package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.util.ArrayList;

public class DispPlayCardMessage extends Message {
    private ArrayList<PlayableCard> displayedPlayableCard;
    public DispPlayCardMessage(int clientID, ArrayList<PlayableCard> displayedPlayableCard) {
        super(clientID, MessageType.DISPLAYED_PLAYABLE_CARDS);
        this.displayedPlayableCard=displayedPlayableCard;
    }

    public ArrayList<PlayableCard> getDisplayedPlayableCard() {
        return displayedPlayableCard;
    }

}
