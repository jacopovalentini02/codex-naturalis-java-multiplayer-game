package it.polimi.ingsfw.ingsfwproject.Network2;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameClientHandlerInterface extends Remote {
    public String sum(Player a, int b) throws RemoteException;

    public String chooseObjectiveCard(Player player, Card card) throws RemoteException;

    public void registerClient(ClientCallbackInterface client) throws RemoteException;
}
