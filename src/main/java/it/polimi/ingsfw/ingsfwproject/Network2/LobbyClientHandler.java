package it.polimi.ingsfw.ingsfwproject.Network2;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameFullException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameNotExistingException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NickAlreadyTakenException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;


public class LobbyClientHandler extends UnicastRemoteObject implements LobbyClientHandlerInterface {
    protected LobbyClientHandler(LobbyController lobbyController) throws RemoteException {
        this.lobbyController = lobbyController;
    }

    private LobbyController lobbyController;

    @Override
    public int createGame(int numOfPlayers, String username) throws RemoteException, NotValidNumOfPlayerException {
        return lobbyController.createGame(numOfPlayers, username);
    }

    @Override
    public int joinGame(int GameID, String username) throws RemoteException, NickAlreadyTakenException, GameFullException, GameNotExistingException {
        return lobbyController.joinExistingGame(username, GameID);
    }
}
