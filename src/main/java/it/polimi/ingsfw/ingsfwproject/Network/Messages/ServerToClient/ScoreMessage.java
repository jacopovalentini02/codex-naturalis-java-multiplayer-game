package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;
import java.util.Map;

/**
 * Message sent from server to client to update the scores of players.
 */
public class ScoreMessage extends ServerToClientMessage implements Serializable {
    private final Map<String, Integer> scores;


    /**
     * Constructs a ScoreMessage with the specified client ID and scores map.
     *
     * @param clientID the ID of the client receiving the message
     * @param scores   a map representing the updated scores of players
     */
    public ScoreMessage(int clientID, Map<String, Integer> scores) {
        super(clientID);
        this.scores=scores;
    }

    /**
     * Sets the updated scores in the client's virtual view.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setScores(scores);
        client.getView().notifyScores(scores);
    }
}
