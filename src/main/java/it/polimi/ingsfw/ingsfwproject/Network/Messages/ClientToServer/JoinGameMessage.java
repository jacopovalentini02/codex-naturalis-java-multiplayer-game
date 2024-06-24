package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;

import java.io.Serializable;

/**
 * Message from client to server to request joining an existing game.
 */
public class JoinGameMessage extends ClientToServerMessage implements Serializable {
    private String nickname;
    private int gameID;

    /**
     * Constructs a JoinGameMessage with the specified parameters.
     *
     * @param clientID the ID of the client sending the message
     * @param nickname the nickname of the client requesting to join
     * @param gameID   the ID of the game to join
     */
    public JoinGameMessage(int clientID, String nickname, int gameID) {
        super(clientID, true);
        this.nickname=nickname;
        this.gameID=gameID;
    }

//    public String getNickname() {
//        return nickname;
//    }

    /**
     * Process the client's request to join an existing game.
     *
     * @param controller the controller on which to execute the message
     */
    @Override
    public void execute(Controller controller){
        LobbyController lobbyController=(LobbyController) controller;
        lobbyController.joinExistingGame(this.nickname,this.gameID,this.getClientID());
    }
}
