package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.Map;

public class ScoreMessage extends ServerToClientMessage implements Serializable {
    private final Map<String, Integer> scores;
    public ScoreMessage(int clientID, Map<String, Integer> scores) {
        super(clientID);
        this.scores=scores;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    @Override
    public void execute(Client client) {
        client.getVirtualView().setScores(scores);
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String playerName = entry.getKey();
            int score = entry.getValue();
            System.out.println("Player: " + playerName + ", Score: " + score);
        }
        client.getView().notifyScores(scores);
    }
}
