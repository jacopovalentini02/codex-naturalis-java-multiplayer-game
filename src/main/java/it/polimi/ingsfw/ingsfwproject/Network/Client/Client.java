package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Actions.Message;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Client {
    private String ip; //Server IP address
    private int port; //Server port
    private final String nickname;
    private Timer timer;
    private static final int timerduration=4000;
    private ExecutorService readExecutionQueue;


    public Client(String ip, int port, String nickname) {
        this.ip = ip;
        this.port = port;
        this.nickname = nickname;
        this.timer=new Timer();
        this.readExecutionQueue=Executors.newSingleThreadExecutor();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getNickname() {
        return nickname;
    }

    public abstract void startConnection() throws Exception;

    public abstract void sendMessage(Message message) throws IOException;

    public abstract void disconnect() throws Exception;

    protected abstract Message receiveMessage() throws IOException, ClassNotFoundException;


    protected void readMessages() {
        readExecutionQueue.execute(() -> {
            try {
                while (!readExecutionQueue.isShutdown()) {
                    Message receivedMessage = receiveMessage();
                    //TODO implementare receiveMessage per RMI
                    //TODO gestire messaggi
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

    public ExecutorService getReadExecutionQueue() {
        return readExecutionQueue;
    }
}
