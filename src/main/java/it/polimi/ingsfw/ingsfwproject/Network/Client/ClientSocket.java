package it.polimi.ingsfw.ingsfwproject.Network.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;

public class ClientSocket {
    private String ip;
    private int port;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    //Dove mettere timer per timeout??
//    private Timer timer;
//    private static final int timerduration=4000;

    public ClientSocket(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        this.socket=new Socket(ip,port);
        input=new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        this.socket.connect(new InetSocketAddress(ip,port));

    }

    public void read(){

    }

    public void send(){

    }

    public void close() throws IOException {
        socket.close();
    }
}
