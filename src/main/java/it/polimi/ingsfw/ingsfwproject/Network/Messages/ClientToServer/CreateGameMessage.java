package it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer;

import it.polimi.ingsfw.ingsfwproject.Controller.Controller;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;

import java.io.Serializable;

/**
 * Message from client to server to request a new game creation.
 */
public class CreateGameMessage extends ClientToServerMessage implements Serializable {
    private int numPlayer;
    private String nickname;

    /**
     * Constructs a CreateGameMessage with the specified client ID, number of players, and nickname.
     *
     * @param clientID   the ID of the client sending the message
     * @param numPlayer  the number of players for the new game
     * @param nickname   the nickname of the player creating the game
     */
    public CreateGameMessage(int clientID, int numPlayer, String nickname) {
        super(clientID, true);
        this.numPlayer = numPlayer;
        this.nickname = nickname;
    }

    /**
     * Gets the nickname of the player creating the game.
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the player creating the game.
     *
     * @param nickname the new nickname of the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Game creation
     *
     * @param controller the controller on which to execute the message
     */
    @Override
    public void execute(Controller controller)  {
        LobbyController lobbyController=(LobbyController) controller;
        lobbyController.createGame(this.numPlayer,this.nickname,this.getClientID());
    }
}
