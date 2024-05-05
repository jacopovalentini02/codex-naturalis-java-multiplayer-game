package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;

import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private LobbyController lobbyController;

    //todo che tipo di coda scegliere. Candidata: ConcurredLinkedQueue oppure array list che va sincronizzata

    private HashMap<Integer, Handler> handlers; //clientID-handler

    private ArrayList<GameServerInstance> games;

    private int clientsCounter;

    public void startsServers(){
        //Socket server e rmi server partono

    }

    public void readQueue(){

    }

    public void addToQueue(){

    }


}
