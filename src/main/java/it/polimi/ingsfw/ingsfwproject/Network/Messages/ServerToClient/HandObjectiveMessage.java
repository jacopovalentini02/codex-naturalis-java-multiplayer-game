package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Card;
import it.polimi.ingsfw.ingsfwproject.Model.ObjectiveCard;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class HandObjectiveMessage extends ServerToClientMessage implements Serializable {
    private final ArrayList<ObjectiveCard> cards;
    public HandObjectiveMessage(int clientID, ArrayList<ObjectiveCard> cards) {
        super(clientID);
        this.cards=cards;
    }

    public ArrayList<ObjectiveCard> getCards() {
        return cards;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setHandObjectives(cards);
        client.getView().notifyHandObjectives(cards);
    }
}
