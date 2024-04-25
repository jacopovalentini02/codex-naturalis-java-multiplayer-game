package it.polimi.ingsfw.ingsfwproject.Network2;

import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameFullException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameNotExistingException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NickAlreadyTakenException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public interface LobbyClientHandlerInterface extends Remote {

    public int createGame (int numOfPlayers, String username) throws java.rmi.RemoteException, NotValidNumOfPlayerException;

    public int joinGame(int GameID, String username) throws java.rmi.RemoteException, NickAlreadyTakenException, GameFullException, GameNotExistingException;

    public HashMap<Integer, Integer> getGameList() throws RemoteException;
}
