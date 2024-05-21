package it.polimi.ingsfw.ingsfwproject.View;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class View implements Runnable{
    public static Client client;
    //public BlockingQueue<Message> messages;



    public abstract void chooseConnection();

    public abstract void notifyException(String message);

    public abstract void displayFirstMessage(int clientID);

    public abstract void displayGameList(HashMap<Integer, Integer> gameList);

    public abstract void notifyGameJoined(int idGame);

    public abstract void notifyNewPlayerJoined(ArrayList<String> nicknames);

    public abstract void notifyStarterCard();

    public abstract void notifyColorsAvailable(List<PlayerColor> colors);

    public abstract void notifyGoldDeckUpdate();

    public abstract void notifyResourceDeckUpdate();

    public abstract void notifyDisplayedCardsUpdate(ArrayList<PlayableCard> displayedCards);

    public abstract void notifyCurrentPlayer(String nickname);

    public abstract void notifyAvailablePositions(ArrayList<Coordinate> coord);

    public abstract void notifyHandObjectives(ArrayList<ObjectiveCard> cards);

    public abstract void notifyGameState(GameState state);

    public abstract void notifyGridUpdate(String nickname, Map<Coordinate, Face> grid);

    public abstract void notifyResourcesUpdate(String nickname, HashMap<Content, Integer> resources);

    public abstract void notifyWinnerUpdate(String nickname);

    public abstract void notifyHandCardsUpdate(ArrayList<PlayableCard> cards);

    public abstract void notifyDisplayedObjectives(List<ObjectiveCard> cards);

    public abstract void notifyScores(Map<String, Integer> scores);

    public abstract void notifyColorChosen(PlayerColor color);

    public abstract void notifyChatMessage(ChatMessage message);

    public abstract void notifyCurrentPlayerHasPlayed(boolean bool);


}
