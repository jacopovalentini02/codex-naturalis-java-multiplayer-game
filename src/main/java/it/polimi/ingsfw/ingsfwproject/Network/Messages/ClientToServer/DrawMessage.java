package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;
import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.Deck;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class DrawMessage extends ClientToServerMessage {

    String nickname;
    boolean resourceDeck;

    public DrawMessage(int clientID, String nickname, boolean resourceDeck) {
        super(clientID, MessageType.DRAW);
        this.nickname=nickname;
        this.resourceDeck=resourceDeck;

    }

    public String getNickname() {
        return nickname;
    }

    public boolean isResourceDeck() {
        return resourceDeck;
    }

    @Override
    public void execute(Controller controller)  {
        GameController gameController=(GameController) controller;
        gameController.draw(this.nickname,this.resourceDeck);
    }
}
