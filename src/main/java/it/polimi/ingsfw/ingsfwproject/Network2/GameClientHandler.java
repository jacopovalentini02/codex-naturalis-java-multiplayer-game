package it.polimi.ingsfw.ingsfwproject.Network2;
import it.polimi.ingsfw.ingsfwproject.Controller.GameController;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GameClientHandler extends UnicastRemoteObject implements GameClientHandlerInterface {

    private long lastHeartbeatReceived;

    private final GameController gameController;

    protected GameClientHandler(GameController gc) throws RemoteException {
        this.gameController = gc;
        lastHeartbeatReceived = System.currentTimeMillis();
        startDisconnectionCheckThread();
    }
    @Override
    public String sum(Player a, int b) throws RemoteException{
        return "a + b";
    }

    @Override
    public String chooseObjectiveCard(String username, int cardID) throws java.rmi.RemoteException, TurnException, GamePhaseException, CardNotPresentException {
        gameController.chooseObjectiveCard(username, cardID);
        return "Objective card successfully chosen";
    }

    @Override
    public void registerClient(ClientCallbackInterface client, String username) throws RemoteException {
        gameController.addClient(username, client);
    }

    @Override
    public String playCard(String username, int cardID, boolean upwards, Coordinate coord) throws RemoteException, TurnException, GamePhaseException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException {
        gameController.playCard(username, cardID, upwards, coord);
        return "Card succesfully played";
    }

    @Override
    public String DrawDisplayedPlayableCard(String username, int cardID) throws RemoteException, TurnException, GamePhaseException, CardNotPresentException, DeckEmptyException {
        gameController.DrawDisplayedPlayableCard(username, cardID);
        return "Card drawn successfully";
    }

    @Override
    public String draw(String username, boolean resourceDeck) throws RemoteException, TurnException, GamePhaseException, DeckEmptyException, DeckException {
        gameController.draw(username, resourceDeck);
        return "Card successfully drawn";
    }

    @Override
    public String chooseColor(String username, PlayerColor color) throws RemoteException, TurnException, GamePhaseException, ColorNotAvailableException, DeckEmptyException {
        gameController.chooseColor(username, color);
        return "Color successfully chosen";
    }

    @Override
    public void heartbeat() throws RemoteException {
        lastHeartbeatReceived = System.currentTimeMillis();
    }

    private void startDisconnectionCheckThread(){

        Thread disconnectionCheck = new Thread(()-> {
            while (true){

                try{
                    long timeSinceLastHeartBeat = System.currentTimeMillis() - lastHeartbeatReceived;
                    if (timeSinceLastHeartBeat > 15000){
                        gameController .clientDisconnected();
                    }
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        disconnectionCheck.start();
    }



}
