package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;

abstract public class Handler {
    Server server;
    GameServerInstance gameServerInstance;

    abstract void sendToClient(Message message);

    void handleMessage(Message message){

    }
}
