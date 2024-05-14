package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.ChatMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

public class RecieveChatMessage extends ServerToClientMessage {

    public String sender;

    public String recipient;

    public String text;

    public RecieveChatMessage(int clientID, String sender, String recipient, String text) {
        super(clientID);
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
    }



    @Override
    public void execute(Client client) {
        ChatMessage message = new ChatMessage(sender, recipient, text);
        client.getVirtualView().addMessageToGlobalChat(message);
        client.getView().notifyChatMessage(message);
    }
}
