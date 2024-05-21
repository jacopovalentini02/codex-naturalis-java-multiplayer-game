package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

public class CurrentPlayerHasPlayedMessage extends ServerToClientMessage {

    boolean currentPlayerHasPlayed;

    public CurrentPlayerHasPlayedMessage(int clientID, boolean currentPlayerHasPlayed) {
        super(clientID);
        this.currentPlayerHasPlayed = currentPlayerHasPlayed;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setCurrentPlayerhasPlayed(currentPlayerHasPlayed);
        client.getView().notifyCurrentPlayerHasPlayed(currentPlayerHasPlayed);
    }
}
