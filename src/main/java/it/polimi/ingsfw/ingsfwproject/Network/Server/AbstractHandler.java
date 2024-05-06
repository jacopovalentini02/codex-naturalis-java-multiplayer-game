package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

abstract public class AbstractHandler extends UnicastRemoteObject implements Handler {
    private int clientID;
    private Server server;
    private GameServerInstance gameServerInstance;

    protected AbstractHandler(int clientID, Server server) throws RemoteException {
        this.clientID = clientID;
        this.server = server;
    }

    protected AbstractHandler(int clientID) throws RemoteException{
        this.clientID = clientID;
    }

    public abstract void sendMessage(Message message);

    //Messaggi ricevuti dal client
    void handleMessageIn(Message message){



    }
    public void handleMessageOut(Message message){
        if(message.getClientID() == -10 || message.getClientID() == clientID){ //se messaggio in broadcast oppure per il client associato
                sendMessage(message);
        }

    }

    public void setGameServerInstance(GameServerInstance instance){
        this.gameServerInstance = instance;
    }

    public int getClientID(){
        return this.clientID;
    }



}
