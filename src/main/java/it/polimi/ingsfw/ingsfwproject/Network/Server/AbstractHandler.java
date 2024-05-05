package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

abstract public class AbstractHandler extends UnicastRemoteObject implements Handler {
    private int clientID;
    private Server server;
    private GameServerInstance gameServerInstance;

    protected AbstractHandler() throws RemoteException {}

    public abstract void sendMessage(Message message);

    //Messaggi ricevuti dal client
    void handleMessageIn(Message message){
        //


    }

    public void handleMessageOut(Message message){
        if(message.getType()== MessageType.STARTER_CARD){
            if(message.getClientID()==clientID)
                sendMessage(message);
        }else{
            sendMessage(message);
        }



    }



}
