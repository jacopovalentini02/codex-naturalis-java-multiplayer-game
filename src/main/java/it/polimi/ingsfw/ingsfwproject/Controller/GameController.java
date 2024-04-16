package it.polimi.ingsfw.ingsfwproject.Controller;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import it.polimi.ingsfw.ingsfwproject.Model.*;

public class GameController extends UnicastRemoteObject {
    private Game model;

    public GameController(Game model) throws RemoteException {
        super();
        this.model = model;
    }

    public void playCard(Player player, PlayableCard card, boolean upwards, Coordinate coord){
        //TODO: chiamare effettvamente PlayerGround.playCard(...).
        // con il valore di ritorno, chiamare game.updatePoints(...) per aggiungere i punti di questa giocata al player
    }

    @Override
    public void DrawDisplayedPlayableCard(Player player, PlayableCard card) throws RemoteException {

    }

//    public ArrayList<Coordinate> getAvailablePositions(Player player){
//
//    }

    public void draw(Player player, Deck deck){

    }

    /*public boolean choosingFace(Player player){

    }*/

    public void drawDisplayedPlayableCard(Player player, PlayableCard card){

    }

    public ArrayList<Coordinate> getAvailablePositions(Player player) throws RemoteException {
        return null;
    }
}
