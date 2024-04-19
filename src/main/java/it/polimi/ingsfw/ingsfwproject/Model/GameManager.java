package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.GameFullException;
import it.polimi.ingsfw.ingsfwproject.Exceptions.IDAlreadyTakenException;

import java.rmi.RemoteException;
import java.util.*;

public class GameManager {
    private HashMap<Integer, Game> gameList;
    int firstAvailableGameID; //indica il primo numero utilizzabile come ID di un nuovo Game

    public GameManager(){
        this.gameList = new HashMap<>();
        this.firstAvailableGameID = 0;
    }
    //TODO: rivedere dopo modifica costruttore game
    public void createGame(int numOfPlayers, String username) throws RemoteException {
        Game newGame = new Game(firstAvailableGameID, numOfPlayers, new Player(username));
        newGame.setIdGame(firstAvailableGameID);
        newGame.setNumOfPlayers(numOfPlayers);
        gameList.put(firstAvailableGameID, newGame);
        firstAvailableGameID++;
    }

    public void joinGame(String nick, int idGame) throws IDAlreadyTakenException, GameFullException {
        Game gameToJoin = gameList.get(idGame);

        if (gameToJoin.getListOfPlayers().size() == gameToJoin.getNumOfPlayers()) //checks if game is already full
            throw new GameFullException("Game " + idGame + " is full.");

        for (Player p: gameToJoin.getListOfPlayers()){ //controllo che username non sia già stato preso
            if (p.getUsername().equals(nick))
                throw new IDAlreadyTakenException("Nickname : " + nick + "is already taken");
        }
        Player newPlayer = new Player(nick);
        //gameToJoin.addPlayer(newPlayer);
    }

    public void deleteGame(int idGame){

    }

}
