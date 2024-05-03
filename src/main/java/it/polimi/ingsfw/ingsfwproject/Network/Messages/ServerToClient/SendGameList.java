package it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.MessageType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//Server - client, clients receive the list of the game
public class SendGameList extends Message implements Serializable {
    private HashMap<Integer, Integer> gameList;

    public SendGameList(int clientID, HashMap<Integer, Integer> gameList) {
        super(clientID,MessageType.SEND_GAME_LIST);
        this.gameList = gameList;
    }

    public HashMap<Integer, Integer> getGameList() {
        return gameList;
    }

    public void printGameList() {
        if (gameList.isEmpty()) {
            System.out.println("No games in progress");
        } else {
            System.out.println(gameList.size());
            for (Map.Entry<Integer, Integer> entry : gameList.entrySet()) {
                int gameId = entry.getKey();
                System.out.println("Game ID: " + gameId);
            }
        }
    }

}
