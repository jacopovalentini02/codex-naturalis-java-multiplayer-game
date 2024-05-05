package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Model.Player;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.util.HashMap;

public class GameServerInstance {
    private GameController gameController;


    //coda dei messaggi in entrata
    //todo che tipo di coda scegliere. Candidata: ConcurredLinkedQueue oppure array list che va sincronizzata

    private HashMap<Integer, Handler> handlers;
    private HashMap<Player, Integer> players;

    public void readQueue(){

    }

    public void addToQueue(){

    }

    public void sendUpdateToAll(Message message ){
        for (Handler handler : handlers.values()) {
            handler.sendToClient(message);
        }
    }


    public int getClientID(Player player){
        return players.get(player);

    }
}
