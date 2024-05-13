package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.io.Serializable;

//Message from client to server to request a new game creation
public class CreateGameMessage extends ClientToServerMessage implements Serializable {
    private int numPlayer;
    private String nickname;

    public CreateGameMessage(int clientID, int numPlayer, String nickname) {
        super(clientID, MessageType.CREATE_GAME, true);
        this.numPlayer = numPlayer;
        this.nickname = nickname;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void execute(Controller controller)  {
        LobbyController lobbyController=(LobbyController) controller;
        lobbyController.createGame(this.numPlayer,this.nickname,this.getClientID());
    }
}
