package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

public class PlayCardMessage extends Message {
    PlayableCard card;
    boolean face;
    Coordinate coordinate;
    public PlayCardMessage(int clientID, PlayableCard card, boolean face, Coordinate coordinate) {
        super(clientID, MessageType.PLAY_CARD);
        this.card = card;
        this.face=face;
        this.coordinate=coordinate;
    }
    public PlayableCard getCard() {
        return card;
    }

    public boolean isFace() {
        return face;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

}
