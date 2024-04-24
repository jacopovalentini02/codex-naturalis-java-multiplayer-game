package it.polimi.ingsfw.ingsfwproject.Model;
import java.util.*;

public class GameManager {
    private HashMap<Integer, Game> gameList;
    int firstAvailableGameID; //indica il primo numero utilizzabile come ID di un nuovo Game

    public GameManager(){
        this.gameList = new HashMap<>();
        this.firstAvailableGameID = 0;
    }

    public int createGame(int numOfPlayers, String username) {
        Game newGame = new Game(this, firstAvailableGameID, numOfPlayers, new Player(username));
        gameList.put(firstAvailableGameID, newGame);
        int gameID = firstAvailableGameID;
        firstAvailableGameID++;
        return gameID;
    }

    public void joinGame(String nick, int idGame){
        Game gameToJoin = gameList.get(idGame);

        Player newPlayer = new Player(nick);
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

}
