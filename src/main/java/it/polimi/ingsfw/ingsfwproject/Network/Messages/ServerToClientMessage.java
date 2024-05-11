package it.polimi.ingsfw.ingsfwproject.Network.Messages;

import it.polimi.ingsfw.ingsfwproject.View.View;

import java.io.Serializable;

public abstract class ServerToClientMessage extends Message implements Serializable {
    int clientID;

    public ServerToClientMessage(int clientID){
        this.clientID = clientID;
    }

    public abstract void execute(View view);

    public int getClientID(){
        return this.clientID;
    }

}
