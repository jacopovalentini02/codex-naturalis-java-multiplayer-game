package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Player;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class CurrentPlayerMessage extends Message {
    private String currentPlayer;
    public CurrentPlayerMessage(int clientID, String currentPlayer) {
        super(clientID, MessageType.CURRENT_PLAYER);
        this.currentPlayer=currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
