package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Model.ChatMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class sendChatMessage extends ClientToServerMessage {

    public String sender;

    public String recipient;

    public String text;

    public sendChatMessage(int clientID, String sender, String recipient, String text) {
        super(clientID, MessageType.CHAT, false);
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
    }

    @Override
    public void execute(Controller controller) {
        GameController gc = (GameController) controller;
        if (this.recipient.equals("global")){
            gc.addMessageToGlobalChat(new ChatMessage(sender, recipient, text));
        } else{
            gc.forwardPrivateChatMessage(new ChatMessage(sender, recipient, text));
        }

    }
}
