package it.polimi.ingsfw.ingsfwproject.Network2;
import java.rmi.*;
import java.util.Random;

public interface ClientHandlerFactoryInterface extends Remote {

    public LobbyClientHandlerInterface getLobbyHandler() throws RemoteException;

    public GameClientHandlerInterface getGameHandler(int GameID) throws RemoteException;

}
