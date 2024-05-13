package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class DisplayedPlayableCardsMessage extends ServerToClientMessage implements Serializable {
    private final ArrayList<PlayableCard> displayedPlayableCard;
    public DisplayedPlayableCardsMessage(int clientID, ArrayList<PlayableCard> displayedPlayableCard) {
        super(clientID);
        this.displayedPlayableCard=displayedPlayableCard;
    }

    public ArrayList<PlayableCard> getDisplayedPlayableCard() {
        return displayedPlayableCard;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setDisplayedCards(displayedPlayableCard);
        client.getView().notifyDisplayedCardsUpdate(displayedPlayableCard);
    }
}
