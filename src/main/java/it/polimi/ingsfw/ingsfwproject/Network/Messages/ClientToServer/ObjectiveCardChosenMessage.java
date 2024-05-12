package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class ObjectiveCardChosenMessage extends ClientToServerMessage {
    public String getNickname() {
        return nickname;
    }

    String nickname;
    int cardID;
    public ObjectiveCardChosenMessage(int clientID, String nickname, int cardID) {
        super(clientID, MessageType.CHOSEN_OBJECTIVE_CARD,false);
        this.nickname=nickname;
        this.cardID=cardID;
    }

    public int getCardID() {
        return cardID;
    }


    @Override
    public void execute(Controller controller) {
        GameController gameController=(GameController) controller;
        gameController.chooseObjectiveCard(this.nickname,this.cardID);
    }
}
