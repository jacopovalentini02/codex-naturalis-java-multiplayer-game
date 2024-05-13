package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Player;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

public class CurrentPlayerMessage extends ServerToClientMessage implements Serializable {
    private final String currentPlayer;
    public CurrentPlayerMessage(int clientID, String currentPlayer) {
        super(clientID);
        this.currentPlayer=currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setCurrentPlayer(currentPlayer);
        client.getView().notifyCurrentPlayer(currentPlayer);
    }
}
