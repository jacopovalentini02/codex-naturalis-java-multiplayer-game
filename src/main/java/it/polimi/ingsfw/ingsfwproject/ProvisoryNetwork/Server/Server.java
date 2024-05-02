package it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;

import java.net.ServerSocket;
import java.util.Map;

public class Server {
    private LobbyController lobbyController;
    private Map<Integer, GameServerInstance> gameServers;
    private ServerSocket serverSocket;
    private LobbyHandler lobbyHandler;

}
