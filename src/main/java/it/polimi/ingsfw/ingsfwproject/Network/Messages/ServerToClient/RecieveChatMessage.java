package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Model.ChatMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;

/**
 * Message sent from server to client to deliver a chat message.
 */
public class RecieveChatMessage extends ServerToClientMessage {
    public String sender;
    public String recipient;

    public String text;

    /**
     * Constructs a ReceiveChatMessage with the specified client ID, sender, recipient, and text.
     *
     * @param clientID  the ID of the client receiving the message
     * @param sender    the sender of the chat message
     * @param recipient the recipient of the chat message ("global" or specific nickname)
     * @param text      the content of the chat message
     */
    public RecieveChatMessage(int clientID, String sender, String recipient, String text) {
        super(clientID);
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
    }


    /**
     * Creates a ChatMessage object and adds it to either the global chat or private chat
     * in the client's virtual view based on the recipient.
     *
     * @param client the client on which to execute the message
     */
    @Override
    public void execute(Client client) {
        ChatMessage message = new ChatMessage(sender, recipient, text);
        if (recipient.equals("global")) {
            client.getVirtualView().addMessageToGlobalChat(message);
        } else {
            client.getVirtualView().addMessageToPrivateChat(message);
        }
        client.getView().notifyChatMessage(message);
    }
}
