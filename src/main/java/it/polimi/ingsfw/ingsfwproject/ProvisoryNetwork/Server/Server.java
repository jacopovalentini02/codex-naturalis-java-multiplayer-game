package it.polimi.ingsfw.ingsfwproject.ProvisoryNetwork.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameFullException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameNotExistingException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NickAlreadyTakenException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.ExceptionMessages.GameFullMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.ExceptionMessages.InvalidNumOfPlayerMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.ExceptionMessages.NickAlreadyTakenMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.ExceptionMessages.NotExistingGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.GameJoinedMessage;

import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Map;

public class Server {
    private LobbyController lobbyController;
    private Map<Integer, GameServerInstance> gameServers;
    private ServerSocket serverSocket;
    private LobbyHandler lobbyHandler;

    public Server(LobbyController lobbyController){
        this.lobbyController = lobbyController;
    }

    public void init(){
        this.initRMI();
    }


    private void initRMI(){
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            System.out.println("A RemoteException occoured during Registry Initialization");
            throw new RuntimeException(e);
        }


    }

    public Message createGame(int numOfPlayers, String username){
        Message response = null;
        int idGame = -1;
        try{
            idGame = this.lobbyController.createGame(numOfPlayers, username);
        } catch (NotValidNumOfPlayerException e) {
            response = new InvalidNumOfPlayerMessage();
            return response;
        }
        if (idGame != -1){
            response = new GameJoinedMessage(idGame);
        } else {
            throw new RuntimeException();
        }
        return response;



    }

    public Message joinGame(String username, int idGame){
        Message response = null;
        int id = -1;
        try {
            id = this.lobbyController.joinExistingGame(username, idGame);
        } catch (NickAlreadyTakenException e) {
            response = new NickAlreadyTakenMessage();
            return response;
        } catch (GameFullException e) {
            response = new GameFullMessage();
            return response;
        } catch (GameNotExistingException e) {
            response = new NotExistingGameMessage();
            return response;
        }

        if (id != -1){
            response = new GameJoinedMessage(id);
            return response;
        } else {
            throw new RuntimeException();
        }
    }





}
