package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.Card;
import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class PickMessage extends ClientToServerMessage {
    String nickname;
    int cardID;
    public PickMessage(int clientID, int cardID, String nickname) {
        super(clientID, MessageType.PICK);
        this.cardID=cardID;
        this.nickname=nickname;
    }
    public int getCardID() {
        return cardID;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void execute(Controller controller) {
        GameController gameController=(GameController)  controller;
        gameController.drawDisplayedPlayableCard(this.nickname,this.cardID);

    }
}
