package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

public class ColorChosenMessage extends ServerToClientMessage implements Serializable {
    PlayerColor color;
    public ColorChosenMessage(int clientID, PlayerColor color) {
        super(clientID);
        this.color=color;
    }


    @Override
    public void execute(Client client) {
        client.getVirtualView().setColor(color);
        client.getView().notifyColorChosen(color);
    }
}
