package it.polimi.ingsfw.ingsfwproject.Model;

/**
 * The {@code ChatMessage} class represents a message.
 * It contains information about the sender, recipient, and the message content.
 */
public class ChatMessage {

    private final String sender;

    private final String recipient;
    private final String message;

    /**
     * Constructs a new {@code ChatMessage} instance with the specified sender, recipient, and message content.
     *
     * @param sender the sender of the message
     * @param recipient the recipient of the message ({@code "global"} for globalchat, or <i>nickname</i> for private chat)
     * @param message the content of the message
     */
    public ChatMessage(String sender, String recipient, String message){
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    /**
     * Returns the sender of the message.
     * @return the sender of the message
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the recipient of the message.
     * @return the recipient of the message
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Returns the content of the message.
     * @return the content of the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns a string representation of the chat message in the format "sender: message".
     *
     * @return A string representation of the chat message
     */
    @Override
    public String toString(){
        return sender + ": " + message;
    }
}
