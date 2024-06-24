package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.StarterCard;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

public class SendStarterCardMessage extends ServerToClientMessage implements Serializable {
    private final StarterCard starterCard;

    public SendStarterCardMessage(int clientID, StarterCard starterCard) {
        super(clientID);
        this.starterCard=starterCard;
    }



    @Override
    public void execute(Client client) {
        client.getVirtualView().getHandCards().add(starterCard);
        client.getView().notifyStarterCard();
    }
}
