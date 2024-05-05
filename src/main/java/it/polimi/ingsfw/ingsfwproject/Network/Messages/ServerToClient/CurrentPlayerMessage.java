package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Player;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class CurrentPlayerMessage extends Message {
    Player currentPlayer;
    public CurrentPlayerMessage(int clientID, Player currentPlayer) {
        super(clientID, MessageType.CURRENT_PLAYER);
        this.currentPlayer=currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
