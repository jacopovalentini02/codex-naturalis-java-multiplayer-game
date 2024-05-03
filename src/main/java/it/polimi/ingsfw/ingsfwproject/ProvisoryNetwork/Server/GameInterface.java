package it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;

import java.rmi.*;
public interface GameInterface extends Remote{

    public Message sendMessage(Message m);

}
