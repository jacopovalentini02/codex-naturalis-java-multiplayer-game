package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.View.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient extends Client{
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private ExecutorService readExecutionQueue;

    public SocketClient(String ip, int port, View view){
        super(ip,port, view);
    }

    @Override
    public void startConnection() throws IOException, ClassNotFoundException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(getIp(), getPort()));

        if (socket.isConnected()) {
            System.out.println("Connessione al server riuscita");
            this.readExecutionQueue = Executors.newSingleThreadExecutor();

            // Inizializzazione dell'ObjectOutputStream e dell'ObjectInputStream
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());

            this.receiveMessage();

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
                    handleMessage(message);
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
}
