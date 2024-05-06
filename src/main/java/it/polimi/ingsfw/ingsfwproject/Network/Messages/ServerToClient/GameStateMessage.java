package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.Game;
import it.polimi.ingsfw.ingsfwproject.Model.GameState;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class GameStateMessage extends Message {
    GameState gameState;
    public GameStateMessage(int clientID, GameState gameState) {
        super(clientID, MessageType.GAME_STATE);
        this.gameState=gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
