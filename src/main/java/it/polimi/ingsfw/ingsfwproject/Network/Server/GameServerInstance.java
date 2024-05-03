package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;

import java.util.HashMap;

public class GameServerInstance {
    private GameController gameController;

    //todo che tipo di coda scegliere. Candidata: ConcurredLinkedQueue oppure array list che va sincronizzata

    private HashMap<Integer, Handler> handlers;

    public void readQueue(){

    }

    public void addToQueue(){

    }

    public void sendUpdateToAll(){

    }

}
