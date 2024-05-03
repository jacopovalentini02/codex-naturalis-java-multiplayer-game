package it.polimi.ingsfw.ingsfwproject.Network2.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameFullException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameNotExistingException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NickAlreadyTakenException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.*;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ClientToServer.JoinGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.GameJoinedMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.SendGameList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClientHandler implements Runnable {
    private Socket socket;
    private LobbyController lobbyController;
    private GameController gameController;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    //TODO passo il server dove ci sono i metodi per gestire la lobby - GameServerInstance (gamecontroller) dove chiamo i metodi per la partita


    //TODO Aggiungere heartbeat

    public ServerClientHandler(Socket socket, LobbyController lobbyController, GameManager manager) throws IOException {
        this.socket = socket;
        this.lobbyController = lobbyController;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());

        System.out.println("Server client handler inizializzato");

    }

    //Server che riceve
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message m = (Message) in.readObject();
                handleMessages(m);
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException | NotValidNumOfPlayerException | NickAlreadyTakenException | GameFullException |
                 GameNotExistingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(Message message) throws IOException {
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
            //disconnect(); //TODO creare metodo disconnect
        }
    }

    public void handleMessages(Message m) throws NotValidNumOfPlayerException, IOException, NickAlreadyTakenException, GameFullException, GameNotExistingException {
        switch (m.getType()){
            case GET_GAME_LIST:
                sendMessage(new SendGameList(lobbyController.getGameList())); //TODO non mandio ma lo aggiungo alla lista
                break;

            case CREATE_GAME:
                CreateGameMessage message=(CreateGameMessage)m;
                //aggiungo coda
                //Server: chiama send message(game joined message)
                //server.addToQueue -> server fa handler.sendMessage
                int gameId=lobbyController.createGame(message.getNumPlayer(), message.getNickname());
                sendMessage(new GameJoinedMessage(gameId));
                break;

            case JOIN_GAME:
                JoinGameMessage joinMess=(JoinGameMessage)m;
                int id=lobbyController.joinExistingGame(joinMess.getNickname(), joinMess.getGameID());
                sendMessage(new GameJoinedMessage(id));
                break;

        }

    }

}
