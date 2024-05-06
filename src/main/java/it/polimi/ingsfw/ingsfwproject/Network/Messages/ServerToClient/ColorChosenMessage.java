package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class ColorChosenMessage extends Message {
    PlayerColor color;
    public ColorChosenMessage(int clientID, PlayerColor color) {
        super(clientID, MessageType.COLOR_CHOSEN);
        this.color=color;
    }
}
