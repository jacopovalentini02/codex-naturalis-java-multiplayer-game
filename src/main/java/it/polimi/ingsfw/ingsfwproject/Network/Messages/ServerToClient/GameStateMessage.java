package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Game;
import it.polimi.ingsfw.ingsfwproject.Model.GameState;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

public class GameStateMessage extends ServerToClientMessage implements Serializable {
    GameState gameState;
    public GameStateMessage(int clientID, GameState gameState) {
        super(clientID);
        this.gameState=gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setState(gameState);
        client.getView().notifyGameState(gameState);
    }
}
