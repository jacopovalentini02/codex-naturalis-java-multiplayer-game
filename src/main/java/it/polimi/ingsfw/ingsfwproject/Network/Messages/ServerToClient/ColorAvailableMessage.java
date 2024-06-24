package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.List;

/**
 * Message from server to client notifying the available player colors.
 */
public class ColorAvailableMessage extends ServerToClientMessage implements Serializable {
    private final List<PlayerColor> tokenAvailable;

    /**
     * Constructs a ColorAvailableMessage with the specified parameters.
     *
     * @param clientID      the ID of the client to whom this message is directed
     * @param tokenAvailable the list of available PlayerColors
     */
    public ColorAvailableMessage(int clientID, List<PlayerColor> tokenAvailable) {
        super(clientID);
        this.tokenAvailable=tokenAvailable;
    }

    /**
     * Notifies client regarding the available player colors.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getView().notifyColorsAvailable(tokenAvailable);
    }
}
