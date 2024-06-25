package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;

import java.io.Serializable;

/**
 * Message from client to server to send a heartbeat signal.
 */
public class HeartBeatMessage extends ClientToServerMessage implements Serializable {
    String nickname;

    /**
     * Constructs a HeartBeatMessage with the specified parameters.
     *
     * @param clientID     the ID of the client sending the heartbeat
     * @param nickname     the nickname of the client sending the heartbeat
     * @param isForServer  indicates whether the message is intended for the server
     */
    public HeartBeatMessage(int clientID, String nickname, Boolean isForServer) {
        super(clientID, isForServer);
        this.nickname = nickname;
    }

    /**
     * Notifies the GameController that a heartbeat message has been received.
     *
     * @param controller the controller on which to execute the message
     */
    @Override
    public void execute(Controller controller) {
        GameController gc = (GameController) controller;
        gc.heartbeat(this.getClientID());
    }
}
