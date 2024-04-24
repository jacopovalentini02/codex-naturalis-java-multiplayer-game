package it.polimi.ingsfw.ingsfwproject.Network2;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientHandlerFactory extends UnicastRemoteObject implements ClientHandlerFactoryInterface {

    private LobbyController lobbyController;
    private GameManager manager;

    protected ClientHandlerFactory(LobbyController lobbyController, GameManager manager) throws RemoteException {
        this.lobbyController = lobbyController;
        this.manager = manager;
    }

    @Override
    public LobbyClientHandlerInterface getLobbyHandler() throws RemoteException {
        return new LobbyClientHandler(this.lobbyController);
    }

    @Override
    public GameClientHandlerInterface getGameHandler(int GameID) throws RemoteException {
        GameController controller = manager.getGameList().get(GameID).getController();
        return new GameClientHandler(controller);
    }
}
