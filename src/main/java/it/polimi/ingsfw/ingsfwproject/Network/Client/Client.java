package it.polimi.ingsfw.ingsfwproject.Network.Client;

import it.polimi.ingsfw.ingsfwproject.Model.GameState;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.View.VirtualView;
import it.polimi.ingsfw.ingsfwproject.View.View;

import java.io.IOException;

public abstract class Client {
    private String ip; //Server IP address
    private int port; //Server port
    private String nickname;
    private int clientID;
    private VirtualView virtualView;
    private View view;


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


    public void handleMessage(Message message){
        switch (message.getType()){
            case FIRST_MESSSAGE: //set clientID in Client e view
                FirstMessage firstMsg=(FirstMessage) message;
                this.clientID=firstMsg.getClientID();
                break;
            case GAME_JOINED: //set state and gameId
                this.virtualView.setState(GameState.WAITING_FOR_PLAYERS);
                GameJoinedMessage mjoined=(GameJoinedMessage) message;
                this.virtualView.setGameID(mjoined.getGameId());
                break;
            case STARTER_CARD:
                SendStarterCardMessage starterMsg=(SendStarterCardMessage) message;
                this.virtualView.getHandCards().add(starterMsg.getStarterCard()); //Add starter card to hand
                break;
            case GOLD_DECK:
                GoldDeckMessage goldMsg=(GoldDeckMessage) message;
                this.virtualView.setGoldDeck(goldMsg.getGoldDeck());
                break;
            case RESOURCE_DECK:
                ResourceDeckMessage resourceMsg=(ResourceDeckMessage) message;
                this.virtualView.setGoldDeck(resourceMsg.getResourceDeck());
                break;
            case DISPLAYED_PLAYABLE_CARDS:
                DispPlayCardMessage displayedCardMsg=(DispPlayCardMessage) message;
                this.virtualView.setDisplayedCards(displayedCardMsg.getDisplayedPlayableCard());
                break;
            case CURRENT_PLAYER:
                CurrentPlayerMessage currentPlayerMsg=(CurrentPlayerMessage) message;
                this.virtualView.setCurrentPlayer(currentPlayerMsg.getCurrentPlayer());
                break;
            case COORDINATES_AVAILABLE:
                CoordinatesAvailableMessage coordMsg=(CoordinatesAvailableMessage) message;
                this.virtualView.getPlayer().getGround().setAvailablePositions(coordMsg.getCoords());
                break;
            case HAND_OBJECTIVE:
                HandObjectiveMessage handObjectiveMsg=(HandObjectiveMessage) message;
                this.virtualView.setHandObjectives(handObjectiveMsg.getCards());
                break;
            case GAME_STATE:
                GameStateMessage gameStateMsg=(GameStateMessage) message;
                this.virtualView.setState(gameStateMsg.getGameState());
                break;
        }

        this.view.addToQueue(message);

    }

    public int getClientID(){
        return this.clientID;
    }


    public void setClientID(int clientID){this.clientID = clientID;}
}
