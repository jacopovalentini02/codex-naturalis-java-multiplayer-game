package it.polimi.ingsfw.ingsfwproject.Network2.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.GameCreatedMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClientHandler implements Runnable {
    private Socket socket;
    private LobbyController lobbyController;
    private GameManager manager;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerClientHandler(Socket socket, LobbyController lobbyController, GameManager manager) throws IOException {
        this.socket = socket;
        this.lobbyController = lobbyController;
        this.manager = manager;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream()); // leggo e scrivo nella connessione finche' non ricevo "quit"


        System.out.println("Server client handler inizializzato");


    }

    //Server che riceve
    public void run() {
        try {

            while (true) {
                Message m = (Message) in.readObject();
                handleMessages(m);

            }
            //in.close();
            //out.close();
            //socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NotValidNumOfPlayerException e) {
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
            //disconnect();
        }
    }

    public void handleMessages(Message m) throws NotValidNumOfPlayerException, IOException {
        switch (m.getType()){
            case GET_GAME_LIST:

            case CREATE_GAME:
                CreateGameMessage message=(CreateGameMessage)m;
                lobbyController.createGame(message.getNumPlayer(), message.getNickname());
                sendMessage(new GameCreatedMessage());
                break;
        }

    }

}
