package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;
import it.polimi.ingsfw.ingsfwproject.Model.Deck;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class DrawMessage extends Message{

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
}
