package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.ObjectiveCard;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message sent from server to client to update the objective cards in the player's hand.
 */
public class HandObjectiveMessage extends ServerToClientMessage implements Serializable {
    private final ArrayList<ObjectiveCard> cards;

    /**
     * Constructs a HandObjectiveMessage with the specified client ID and objective cards.
     *
     * @param clientID the ID of the client receiving the message
     * @param cards the updated objective cards in the player's hand
     */
    public HandObjectiveMessage(int clientID, ArrayList<ObjectiveCard> cards) {
        super(clientID);
        this.cards=cards;
    }

    /**
     * Sets the objective cards in the player's hand for the client's virtual view.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setHandObjectives(cards);
        client.getView().notifyHandObjectives(cards);
    }
}
