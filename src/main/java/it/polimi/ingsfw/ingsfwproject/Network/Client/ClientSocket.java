package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Actions.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.GameClientModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
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
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        this.readExecutionQueue=Executors.newSingleThreadExecutor();
        this.model=new GameClientModel();


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
                    String prova= (String) input.readObject();
                    model.prova(prova);
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

    public static void main(String[] args) {
        ClientSocket client = new ClientSocket("bea", "127.0.0.1",1337);

        try {
            client.startConnection();
        } catch (IOException e) {
            System.err.println(e.getMessage()); }

        while(true){
            Scanner in = new Scanner(System.in);
            String prova = in.nextLine();
            //sendMessage(prova);
        }
    }

}
