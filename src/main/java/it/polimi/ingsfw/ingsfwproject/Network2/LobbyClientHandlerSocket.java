package it.polimi.ingsfw.ingsfwproject.Network2;

import it.polimi.ingsfw.ingsfwproject.Exceptions.GameFullException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameNotExistingException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NickAlreadyTakenException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;

import java.rmi.RemoteException;
import java.util.HashMap;

public class LobbyClientHandlerSocket implements LobbyClientHandlerInterface{

    @Override
    public int createGame(int numOfPlayers, String username) throws RemoteException, NotValidNumOfPlayerException {
        return 0;
    }

    @Override
    public int joinGame(int GameID, String username) throws RemoteException, NickAlreadyTakenException, GameFullException, GameNotExistingException {
        return 0;
    }

    @Override
    public HashMap<Integer, Integer> getGameList() throws RemoteException {
        return null;
    }
}
