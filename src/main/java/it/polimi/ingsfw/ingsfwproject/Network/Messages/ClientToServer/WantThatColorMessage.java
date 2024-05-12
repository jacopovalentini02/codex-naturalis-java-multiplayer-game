package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class WantThatColorMessage extends ClientToServerMessage {

    public String getNickname() {
        return nickname;
    }

    String nickname;
    PlayerColor color;
    public WantThatColorMessage(int clientID, String nickname, PlayerColor color) {
        super(clientID, MessageType.WANTED_COLOR,false);
        this.color=color;
        this.nickname=nickname;
    }
    public PlayerColor getColor() {
        return color;
    }

    @Override
    public void execute(Controller controller){
        GameController gameController=(GameController)  controller;
        gameController.chooseColor(this.nickname,this.color);

    }
}
