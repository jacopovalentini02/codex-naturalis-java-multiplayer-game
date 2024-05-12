package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.SendGameListMessage;

import java.io.Serializable;

public class GetGameListMessage extends ClientToServerMessage implements Serializable {
    public GetGameListMessage(int clientID) {

        super(clientID, MessageType.GET_GAME_LIST,true);
    }

    @Override
    public void execute(Controller controller) {
        LobbyController lobbyController=(LobbyController) controller;
        lobbyController.getGameList(this.getClientID());
    }
}
