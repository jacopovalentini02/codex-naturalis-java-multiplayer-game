package it.polimi.ingsfw.ingsfwproject.Controller;

import it.polimi.ingsfw.ingsfwproject.Exceptions.GameFullException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.GameNotExistingException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NickAlreadyTakenException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;
import it.polimi.ingsfw.ingsfwproject.Model.Game;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Model.Player;

import java.rmi.RemoteException;

public class LobbyController {
    private GameManager lobby;

    public LobbyController(GameManager lobby){
        this.lobby = lobby;
    }

    public int createGame(int numOfPlayers, String thisPlayer) throws NotValidNumOfPlayerException {
        if(numOfPlayers < 2 || numOfPlayers > 4)
            throw new NotValidNumOfPlayerException("the player's number must be between 2 and 4, but you entered: " + numOfPlayers);
        int gameID;
        synchronized (lobby){
            gameID = lobby.createGame(numOfPlayers, thisPlayer);
        }
        return gameID;
    }

    public int joinExistingGame(String nick, int idGame) throws GameNotExistingException, GameFullException, NickAlreadyTakenException {
        synchronized (lobby) {

            if (!lobby.getGameIDs().contains(idGame))
                throw new GameNotExistingException("there's no game with ID:" + idGame);

            Game gameToJoin = lobby.getGameList().get(idGame);

            if(gameToJoin.getListOfPlayers().size() == gameToJoin.getNumOfPlayers())
                throw new GameFullException("Game " + idGame + " is full.");

            for(Player player : gameToJoin.getListOfPlayers()) {
                if(player.getUsername().equals(nick))
                    throw new NickAlreadyTakenException("there's another player with the nick: " + nick);
            }
            lobby.joinGame(nick, idGame);
        }
        return idGame;
    }

    public void deleteGame(int idGame) throws GameNotExistingException {
        synchronized (lobby) {
            if (!lobby.getGameIDs().contains(idGame))
                throw new GameNotExistingException("there's no game with ID:" + idGame);
            lobby.deleteGame(idGame);
        }
    }



}
