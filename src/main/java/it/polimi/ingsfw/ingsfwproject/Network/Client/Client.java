package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.HeartBeatMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;
import it.polimi.ingsfw.ingsfwproject.View.VirtualView;
import it.polimi.ingsfw.ingsfwproject.View.View;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Abstract class representing a client in the network. Handles connection, communication,
 * and heartbeat messages to the server.
 */
public abstract class Client implements InGameListener {
    private String ip; //Server IP address
    private int port; //Server port
    private String nickname;
    private int clientID;

    private Thread heartbeatThread;

    public volatile boolean inGame = false;

    private final Object lock = new Object();
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

    /**
     * Constructs a new Client with the specified IP address, port, and view.
     *
     * @param ip   the server IP address
     * @param port the server port
     * @param view the view associated with this client
     */
    public Client(String ip, int port, View view) {
        this.ip = ip;
        this.port = port;
        this.virtualView = new VirtualView();
        virtualView.setInGameListener(this);
        this.view=view;
        startHeartbeat();
   }

    /**
     * Handles the game status change event.
     *
     * @param idGame the new game status
     */
    @Override
    public void onGameStatusChanged(boolean idGame){
        setInGame(idGame);
   }

    /**
     * Starts the heartbeat thread to periodically send heartbeat messages to the server.
     */
    public void startHeartbeat() {
        heartbeatThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (lock) {
                        while (!inGame) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }

                    Message heartbeat = new HeartBeatMessage(clientID, nickname, false);
                    try {
                        sendMessage(heartbeat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        heartbeatThread.start();
    }

    /**
     * Sets the in-game status for this client.
     *
     * @param inGame the new in-game status
     */
    public synchronized void setInGame(boolean inGame){
        this.inGame = inGame;
        synchronized (lock){
            lock.notifyAll();
        }
    }

    /**
     * Returns the server IP address.
     *
     * @return the server IP address
     */
    public String getIp() {
        return ip;
    }

    /**
     * Returns the server port.
     *
     * @return the server port
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the nickname of this client.
     *
     * @return the nickname of this client
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname for this client.
     *
     * @param nickname the new nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Starts the connection to the server. Must be implemented by subclasses.
     *
     * @throws Exception if an error occurs during connection
     */
    public abstract void startConnection() throws Exception;

    /**
     * Sends a message to the server. Must be implemented by subclasses.
     *
     * @param message the message to send
     * @throws IOException if an error occurs during message sending
     */
    public abstract void sendMessage(Message message) throws IOException;

    /**
     * Disconnects from the server. Must be implemented by subclasses.
     *
     * @throws Exception if an error occurs during disconnection
     */
    public abstract void disconnect() throws Exception;

    /**
     * Handles a message received from the server by executing it.
     *
     * @param message the message to handle
     */
    public synchronized void handleMessage(Message message){
        ServerToClientMessage toProcess = (ServerToClientMessage) message;
        toProcess.execute(this);
    }

    /**
     * Returns the client ID of this client.
     *
     * @return the client ID of this client
     */
    public int getClientID(){
        return this.clientID;
    }

    /**
     * Sets the client ID for this client.
     *
     * @param clientID the new client ID to set
     */
    public void setClientID(int clientID){this.clientID = clientID;}

    /**
     * Returns the connection status of this client.
     *
     * @return true if the client is connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }


    /**
     * Sets the connection status for this client.
     *
     * @param connected the new connection status to set
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
