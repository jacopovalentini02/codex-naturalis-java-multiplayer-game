package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Model.GameState;
import it.polimi.ingsfw.ingsfwproject.Network2.GameClientModel;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.GameJoinedMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ServerToClient.SendGameList;
import it.polimi.ingsfw.ingsfwproject.Network.Client.VirtualView;

import java.io.IOException;

public abstract class Client {
    private String ip; //Server IP address
    private int port; //Server port
    private final String nickname;
    private int clientID;
    private VirtualView view;


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

    public abstract void receiveMessage() throws IOException, ClassNotFoundException;

    public void handleMessage(Message message){
        switch (message.getType()){
            case GAME_JOINED: //Create model, set state and gameId
                this.view.setState(GameState.WAITING_FOR_PLAYERS);
                GameJoinedMessage mjoined=(GameJoinedMessage) message;
                this.view.setGameID(mjoined.getGameId());
                break;
            case SEND_GAME_LIST: //game list received, print all the games
                SendGameList m=(SendGameList) message;
                m.printGameList();
                break;

        }

    }
}
