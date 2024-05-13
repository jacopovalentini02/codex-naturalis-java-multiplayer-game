package it.polimi.ingsfw.ingsfwproject.Model;
import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;

import java.util.*;

public class GameManager {
    private HashMap<Integer, Game> gameList;
    int firstAvailableGameID; //indica il primo numero utilizzabile come ID di un nuovo Game

    public GameManager(){
        this.gameList = new HashMap<>();
        this.firstAvailableGameID = 0;
    }

    public int createGame(int numOfPlayers, String username, int clientID) {
        //todo cambio e aggiungo server instance
        GameServerInstance gameServerInstance=new GameServerInstance();
        Game newGame = new Game(gameServerInstance,this, firstAvailableGameID, numOfPlayers, new Player(username,gameServerInstance, clientID));
        gameList.put(firstAvailableGameID, newGame);
        int gameID = firstAvailableGameID;
        firstAvailableGameID++;
        return gameID;
    }

    public void joinGame(String nick, int idGame, int clientID){
        Game gameToJoin = gameList.get(idGame);
        GameServerInstance gameServerInstance = gameToJoin.getGameServerInstance();

        Player newPlayer = new Player(nick, gameServerInstance, clientID);
        gameToJoin.addPlayer(newPlayer);
    }

    public void deleteGame(int idGame){
        gameList.remove(idGame);
    }

    public List<Integer> getGameIDs(){
        return new ArrayList<>(gameList.keySet());
    }

    public HashMap<Integer, Game> getGameList(){
        return gameList;
    }

    public void startGame(int gameID){
        Game gameToStart = gameList.get(gameID);
        gameToStart.setupField();
    }

}
