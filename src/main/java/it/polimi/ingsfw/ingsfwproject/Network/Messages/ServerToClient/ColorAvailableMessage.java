package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.util.List;

public class ColorAvailableMessage extends Message {
    private List<PlayerColor> tokenAvailable;

    public ColorAvailableMessage(int clientID, List<PlayerColor> tokenAvailable) {
        super(clientID, MessageType.COLORS_AVAILABLE);
        this.tokenAvailable=tokenAvailable;
    }

    public List<PlayerColor> getTokenAvailable() {
        return tokenAvailable;
    }
}
