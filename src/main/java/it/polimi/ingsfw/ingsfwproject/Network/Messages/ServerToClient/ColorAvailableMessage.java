package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.List;

public class ColorAvailableMessage extends ServerToClientMessage implements Serializable {
    private final List<PlayerColor> tokenAvailable;

    public ColorAvailableMessage(int clientID, List<PlayerColor> tokenAvailable) {
        super(clientID);
        this.tokenAvailable=tokenAvailable;
    }

    public List<PlayerColor> getTokenAvailable() {
        return tokenAvailable;
    }


    @Override
    public void execute(Client client) {
        client.getView().notifyColorsAvailable(tokenAvailable);
    }
}
