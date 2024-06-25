package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Model.ChatMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;

/**
 * Message from client to server indicating a chat message to be sent.
 */
public class sendChatMessage extends ClientToServerMessage {
    public String sender;
    public String recipient;
    public String text;

    /**
     * Constructs a sendChatMessage with the specified parameters.
     *
     * @param clientID  the ID of the client sending the message
     * @param sender    the nickname of the sender of the chat message
     * @param recipient the nickname of the recipient of the chat message ("global" for global chat)
     * @param text      the text content of the chat message
     */
    public sendChatMessage(int clientID, String sender, String recipient, String text) {
        super(clientID, false);
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
    }

    /**
     * Sending the chat message to the appropriate chat.
     *
     * @param controller the controller on which to execute the message
     */
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
