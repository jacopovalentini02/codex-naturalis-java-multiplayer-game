package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

/**
 * Message from server to client to notify an exception occurred on the server.
 */
public class ExceptionMessage extends ServerToClientMessage {
    String description;

    /**
     * Constructs an ExceptionMessage with the specified parameters.
     *
     * @param clientID    the ID of the client to whom this message is directed
     * @param description the description of the exception
     */
    public ExceptionMessage(int clientID, String description) {
        super(clientID);
        this.description =description;
    }

    /**
     * Notifies the client's view about the exception.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getView().notifyException(description);
    }
}
