package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Model.Game;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class GetColorAvailableMessage extends ClientToServerMessage {
    public GetColorAvailableMessage(int clientID) {
        super(clientID, MessageType.ASK_FOR_COLORS, false);
    }

    @Override
    public void execute(Controller controller) {
//        GameController gameController=(GameController) controller;
//        gameController.getModel().getTokenAvailable();


    }
}
