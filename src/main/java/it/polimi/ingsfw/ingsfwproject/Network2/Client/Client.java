package it.polimi.ingsfw.ingsfwproject.Network2.Client;

import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;

import java.io.IOException;
import java.util.Timer;

public abstract class Client {
    private int clientID; //nickname is not unique
    private String ip; //Server IP address
    private int port; //Server port
    private final String nickname;


    public Client(String ip, int port, String nickname) {
        this.ip = ip;
        this.port = port;
        this.nickname = nickname;

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
