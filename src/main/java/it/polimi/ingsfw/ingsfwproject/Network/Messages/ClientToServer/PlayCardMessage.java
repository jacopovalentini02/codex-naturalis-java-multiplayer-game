package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class PlayCardMessage extends Message {
    String nickname;
    int cardID;
    boolean face;
    Coordinate coordinate;
    public PlayCardMessage(int clientID, int card, boolean face, Coordinate coordinate, String nickname) {
        super(clientID, MessageType.PLAY_CARD);
        this.cardID = card;
        this.face=face;
        this.coordinate=coordinate;
        this.nickname=nickname;
    }

    public int getCardID() {
        return cardID;
    }

    public boolean isFace() {
        return face;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public String getNickname() {
        return nickname;
    }
}
