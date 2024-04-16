package it.polimi.ingsfw.ingsfwproject.Controller;

import it.polimi.ingsfw.ingsfwproject.Model.Game;

import java.rmi.RemoteException;

public class SocketGameController extends GameController implements InterfaceSocketGameController{
    public SocketGameController(Game model) throws RemoteException {
        super(model);
    }
}
