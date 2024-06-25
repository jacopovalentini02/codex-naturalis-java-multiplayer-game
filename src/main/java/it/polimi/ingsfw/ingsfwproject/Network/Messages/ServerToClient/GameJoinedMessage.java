package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.GameState;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

import java.io.Serializable;

/**
 * Message sent from server to client when the client successfully joins a game.
 */
public class GameJoinedMessage extends ServerToClientMessage implements Serializable {
    private final int gameId;
    private final String nickName;

    /**
     * Constructs a GameJoinedMessage with the specified client ID, game ID, and nickname.
     *
     * @param clientID the ID of the client receiving the message
     * @param gameId the ID of the game that the client has joined
     * @param nickname the nickname of the client who joined the game
     */
    public GameJoinedMessage(int clientID, int gameId, String nickname) {
        super(clientID);
        this.gameId=gameId;
        this.nickName=nickname;
    }

    /**
     * Sets the client's virtual view state to WAITING_FOR_PLAYERS,
     * and notifies the view that the game has been joined.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        client.getVirtualView().setState(GameState.WAITING_FOR_PLAYERS);
        client.getVirtualView().setGameID(gameId);
        client.setNickname(nickName);
        client.getView().notifyGameJoined(gameId);
    }
}
