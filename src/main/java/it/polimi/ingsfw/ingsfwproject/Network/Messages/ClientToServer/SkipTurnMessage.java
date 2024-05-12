package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class SkipTurnMessage extends ClientToServerMessage {

    public SkipTurnMessage(int clientID, String nickname) {
        super(clientID, MessageType.SKIP_TURN);
    }

    @Override
    public void execute(Controller controller){
        //todo implementare lo skip dei messaggi
    }
}
