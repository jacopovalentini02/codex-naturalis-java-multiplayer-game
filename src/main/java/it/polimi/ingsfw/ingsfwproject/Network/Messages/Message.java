package it.polimi.ingsfw.ingsfwproject.Network.Messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    int clientID;

    public int getClientID(){
        return this.clientID;
    }
}
