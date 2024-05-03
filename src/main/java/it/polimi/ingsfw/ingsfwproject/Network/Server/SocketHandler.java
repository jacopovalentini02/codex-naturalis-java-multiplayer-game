package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameFullException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameNotExistingException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NickAlreadyTakenException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketHandler extends Handler implements Runnable{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());

        System.out.println("Server client handler inizializzato");
    }
@Override
    void sendToClient(Message message) {
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
            //disconnect(); //TODO creare metodo disconnect
        }
    }

    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message m = (Message) in.readObject();
                handleMessage(m);
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
