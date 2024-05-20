package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

public class ColorChosenMessage extends ServerToClientMessage implements Serializable {
    PlayerColor color;

    String nickname;
    public ColorChosenMessage(int clientID, PlayerColor color, String nickname) {
        super(clientID);
        this.color=color;
        this.nickname=nickname;
    }


    @Override
    public void execute(Client client) {
        if(client.getNickname().equals(nickname))
            client.getVirtualView().setColor(color);

        client.getVirtualView().getPlayerColorMap().put(nickname,color);
        client.getView().notifyColorChosen(color);
    }
}
