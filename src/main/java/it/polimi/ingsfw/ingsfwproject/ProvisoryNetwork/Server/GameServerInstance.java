package it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Messages.Message;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameServerInstance {
    private GameController gameController;
    private ConcurrentLinkedQueue<Message> commandQueue;
    private ArrayList<Handler> connectionHandlers;


}
