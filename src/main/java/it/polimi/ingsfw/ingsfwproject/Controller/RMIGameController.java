package it.polimi.ingsfw.ingsfwproject.Controller;

import it.polimi.ingsfw.ingsfwproject.Model.Game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIGameController extends GameController implements InterfaceRMIGameController {

    public RMIGameController(Game model) throws RemoteException {
        super(model);
    }
}
