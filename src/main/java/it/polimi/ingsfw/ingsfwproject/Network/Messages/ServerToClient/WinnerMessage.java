package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

public class WinnerMessage extends ServerToClientMessage implements Serializable {
    private final String nickname;
    public WinnerMessage(int clientID, String nickname) {
        super(clientID);
        this.nickname=nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setWinner(nickname);
        client.getView().notifyWinnerUpdate(nickname);
    }
}
