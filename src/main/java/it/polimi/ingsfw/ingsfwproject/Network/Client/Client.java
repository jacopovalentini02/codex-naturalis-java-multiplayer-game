package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;
import it.polimi.ingsfw.ingsfwproject.View.VirtualView;
import it.polimi.ingsfw.ingsfwproject.View.View;

import java.io.IOException;
import java.io.Serializable;

public abstract class Client {
    private String ip; //Server IP address
    private int port; //Server port
    private String nickname;
    private int clientID;

    public VirtualView getVirtualView() {
        return virtualView;
    }

    public View getView(){return view;}

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    private VirtualView virtualView;
    private View view;

    private boolean connected;


    public Client(String ip, int port, View view) {
        this.ip = ip;
        this.port = port;
        this.virtualView = new VirtualView();
        this.view=view;
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public abstract void startConnection() throws Exception;

    public abstract void sendMessage(Message message) throws IOException;

    public abstract void disconnect() throws Exception;

    public synchronized void handleMessage(Message message){
        ServerToClientMessage toProcess = (ServerToClientMessage) message;
        toProcess.execute(this);
    }


    public int getClientID(){
        return this.clientID;
    }


    public void setClientID(int clientID){this.clientID = clientID;}

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
