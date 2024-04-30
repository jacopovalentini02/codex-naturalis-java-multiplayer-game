package it.polimi.ingsfw.ingsfwproject.Network2.Client;

import it.polimi.ingsfw.ingsfwproject.Model.GameState;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.GameClientModel;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientSocket extends Client{
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private ExecutorService readExecutionQueue;

    private GameClientModel model;

    public ClientSocket(String nickname, String ip, int port){
        super(ip,port,nickname);
    }

    @Override
    public void startConnection() throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(getIp(), getPort()));

        if (socket.isConnected()) {
            System.out.println("Connessione al server riuscita");
            this.readExecutionQueue = Executors.newSingleThreadExecutor();
            this.model = new GameClientModel();

            // Inizializzazione dell'ObjectOutputStream e dell'ObjectInputStream
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());

        } else {
            System.out.println("Connessione al server fallita");
            throw new IOException("Connessione al server fallita");
        }

    }

    //Send message to server
    @Override
    public void sendMessage(Message message) throws IOException {
        try {
            output.writeObject(message);
            output.flush();
            output.reset();
        } catch (IOException e) {
            e.printStackTrace();
            disconnect();
        }
    }


    @Override
    public void disconnect(){
        readExecutionQueue.shutdown();
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Disconnection failed");
            e.printStackTrace();
        }
    }

    public void receiveMessage() throws IOException, ClassNotFoundException {
        readExecutionQueue.execute(() -> {
            try {
                while (!readExecutionQueue.isShutdown()) {
                    Message message = (Message) input.readObject();
                    System.out.println("Message type risposta dal server: "+ message.getType());
                    handleMessageReceived(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                try {
                    disconnect();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }

    public void handleMessageReceived(Message message){
        switch (message.getType()){
            case GAME_CREATED:
                this.model=new GameClientModel();
                this.model.setState(GameState.WAITING_FOR_PLAYERS);
                break;
        }
    }

    public static <Map> void main(String [] args)  {
        ClientSocket client = new ClientSocket("bea", "127.0.0.1",1337);

        try {
            client.startConnection();
            client.sendMessage(new CreateGameMessage(2, "jaco"));
            client.receiveMessage();
        } catch (IOException e) {
            System.err.println(e.getMessage()); } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
