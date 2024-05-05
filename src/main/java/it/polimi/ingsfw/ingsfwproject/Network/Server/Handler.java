package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

abstract public class Handler {
    private int clientID;
    private Server server;
    private GameServerInstance gameServerInstance;

    abstract void sendToClient(Message message);

    //Messaggi ricevuti dal client
    void handleMessageIn(Message message){
        //

    }

    void handleMessageOut(Message message){
        if(message.getType()== MessageType.STARTER_CARD){
            if(message.getClientID()==clientID)
                sendToClient(message);
        }else{
            sendToClient(message);
        }

        

    }



}
