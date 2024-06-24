package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

public class ExceptionMessage extends ServerToClientMessage {
    String description;
    public ExceptionMessage(int clientID, String description) {
        super(clientID);
        this.description =description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void execute(Client client) {
        client.getView().notifyException(description);
    }
}
