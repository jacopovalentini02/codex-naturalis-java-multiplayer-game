package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Model.GameState;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.View.VirtualView;

import java.io.IOException;

public abstract class Client {
    private String ip; //Server IP address
    private int port; //Server port
    private String nickname;
    private int clientID;
    private VirtualView view;


    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.view = new VirtualView();
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

    public abstract void receiveMessage() throws IOException, ClassNotFoundException;

    public void handleMessage(Message message){
        switch (message.getType()){
            case FIRST_MESSSAGE: //set clientID in Client e view
                FirstMessage firstMsg=(FirstMessage) message;
                this.clientID=firstMsg.getClientID();
            case GAME_JOINED: //set state and gameId
                this.view.setState(GameState.WAITING_FOR_PLAYERS);
                GameJoinedMessage mjoined=(GameJoinedMessage) message;
                this.view.setGameID(mjoined.getGameId());
                break;
            case SEND_GAME_LIST: //game list received, set dei game nella view
                SendGameList m=(SendGameList) message;
                //PASSARE A RECEIVE MESSAGE VIEW
                break;
            case STARTER_CARD:
                SendStarterCard starterMsg=(SendStarterCard) message;
                this.view.setState(GameState.CHOOSING_STARTER_CARDS); //Change state
                this.view.getHandCards().add(starterMsg.getStarterCard()); //Add starter card to hand
                break;
            case GOLD_DECK:
                GoldDeckMessage goldMsg=(GoldDeckMessage) message;
                this.view.setGoldDeck(goldMsg.getGoldDeck());
                break;
            case RESOURCE_DECK:
                ResourceDeckMessage resourceMsg=(ResourceDeckMessage) message;
                this.view.setGoldDeck(resourceMsg.getResourceDeck());
            case DISPLAYED_PLAYABLE_CARDS:
                DispPlayCardMessage displayedCardMsg=(DispPlayCardMessage) message;
                this.view.setDisplayedCards(displayedCardMsg.getDisplayedPlayableCard());
                break;
            case CURRENT_PLAYER:
                CurrentPlayerMessage currentPlayerMsg=(CurrentPlayerMessage) message;
                this.view.setCurrentPlayer(currentPlayerMsg.getCurrentPlayer());
                break;


        }

    }
}
