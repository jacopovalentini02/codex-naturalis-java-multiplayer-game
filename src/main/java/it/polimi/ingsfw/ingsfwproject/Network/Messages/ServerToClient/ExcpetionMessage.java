package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;
import it.polimi.ingsfw.ingsfwproject.View.View;

public class ExcpetionMessage extends ServerToClientMessage {
    String description;
    public ExcpetionMessage(int clientID, String description) {
        super(clientID);
        this.description =description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void execute(View view) {
        view.notifyException(description);
    }
}
