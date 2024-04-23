package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Actions.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientSocket extends Client implements Runnable{
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;

    public ClientSocket(String nickname, String ip, int port){
        super(ip,port,nickname);
    }

    @Override
    public void startConnection() throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(getIp(), getPort()));
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());

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
        getReadExecutionQueue().shutdown();
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Disconnection failed");
            e.printStackTrace();
        }
    }

    @Override
    protected Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) input.readObject();
    }

    @Override
    public void run() {

    }
}
