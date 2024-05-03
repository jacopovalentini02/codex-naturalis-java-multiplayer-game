package it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ClientToServer.JoinGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.ExceptionMessages.InvalidRequestMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LobbyHandler extends UnicastRemoteObject implements GameInterface{

    private final Server server;
    protected LobbyHandler(Server s) throws RemoteException {
        this.server = s;
    }

    @Override
    public Message sendMessage(Message m) {
        switch (m.getType()){
            case CREATE_GAME: {
                CreateGameMessage message = (CreateGameMessage)m;
                return server.createGame(message.getNumPlayer(), message.getNickname());
            }
            case JOIN_GAME:{
                JoinGameMessage message = (JoinGameMessage)m;
                return server.joinGame(message.getNickname(), message.getGameID());
            }
            default: {
                return new InvalidRequestMessage();
            }
        }
    }

}
