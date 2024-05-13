package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class HandCardsMessage extends ServerToClientMessage implements Serializable {
    private final ArrayList<PlayableCard> handCards;
    public HandCardsMessage(int clientID, ArrayList<PlayableCard> handCards) {
        super(clientID);
        this.handCards=handCards;
    }

    public ArrayList<PlayableCard> getHandCards() {
        return handCards;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setHandCards(handCards);
        client.getView().notifyHandCardsUpdate(handCards);
    }
}
