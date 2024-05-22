package it.polimi.ingsfw.ingsfwproject.Model;

public class ChatMessage {

    private final String sender;

    private final String recipient;
    private final String message;

    public ChatMessage(String sender, String recipient, String message){
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString(){
        return sender + ": " + message;
    }
}
