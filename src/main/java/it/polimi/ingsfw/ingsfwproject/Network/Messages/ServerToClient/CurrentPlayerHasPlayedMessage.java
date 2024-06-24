package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

/**
 * Message from server to client indicating whether the current player has already played a card in the current turn.
 */
public class CurrentPlayerHasPlayedMessage extends ServerToClientMessage {
    boolean currentPlayerHasPlayed;

    /**
     * Constructs a CurrentPlayerHasPlayedMessage with the specified parameters.
     *
     * @param clientID               the ID of the client to whom this message is directed
     * @param currentPlayerHasPlayed whether the current player has played or not
     */
    public CurrentPlayerHasPlayedMessage(int clientID, boolean currentPlayerHasPlayed) {
        super(clientID);
        this.currentPlayerHasPlayed = currentPlayerHasPlayed;
    }

    /**
     * Notifies the view about if the current player has played.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setCurrentPlayerhasPlayed(currentPlayerHasPlayed);
        client.getView().notifyCurrentPlayerHasPlayed(currentPlayerHasPlayed);
    }
}
