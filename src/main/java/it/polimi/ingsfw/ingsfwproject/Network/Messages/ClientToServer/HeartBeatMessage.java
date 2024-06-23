package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.io.Serializable;

public class HeartBeatMessage extends ClientToServerMessage implements Serializable {

    String nickname;
    public HeartBeatMessage(int clientID, String nickname, MessageType messageType, Boolean isForServer) {
        super(clientID, messageType, isForServer);
        this.nickname = nickname;
    }

    @Override
    public void execute(Controller controller) {
        GameController gc = (GameController) controller;
        gc.heartbeat(this.getClientID());
    }
}
