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
    private int clientID; //nickname is not unique
    private String ip; //Server IP address
    private int port; //Server port
    private final String nickname;
    private Timer timer;
    private static final int timerduration=4000;



    public Client(String ip, int port, String nickname) {
        this.ip = ip;
        this.port = port;
        this.nickname = nickname;
        this.timer=new Timer();
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

}
