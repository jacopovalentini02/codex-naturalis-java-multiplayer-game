package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.util.Map;

public class ScoreMessage extends Message {
    private Map<String, Integer> scores;
    public ScoreMessage(int clientID, Map<String, Integer> scores) {
        super(clientID, MessageType.SCORE);
        this.scores=scores;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }
}
