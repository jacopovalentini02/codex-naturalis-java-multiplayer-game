package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;
import it.polimi.ingsfw.ingsfwproject.View.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.*;


public class GUIView extends View {


    private Stage stage;

    private WaitingController waitingController;
    private ChooseObjectiveController chooseObjectiveController;
    private ChooseStarterController chooseStarterController;
    private LobbyGUIController lobbyGUIController;

    private GUIController currentController;


    public GUIView(){
        chooseConnection();
    }

    public void openLobby() {
        Platform.runLater(() -> {
            try {

                lobbyGUIController =new LobbyGUIController();
                currentController=lobbyGUIController;
                LobbyGUIController.setGuiView(this);
                //lobbyApp.setGuiView(this);
                lobbyGUIController.start(this.stage);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void openCreateGameWindow(){
        Platform.runLater(() -> {
            try {
                CreateGameController createGameController = new CreateGameController();
                createGameController.setGuiView(this);
                createGameController.start(this.stage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void openSetUpGame(){
        waitingController =new WaitingController();

        Platform.runLater(() -> {
            try {
                WaitingController.setGuiView(this);
                waitingController.start(this.stage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void showConnectionError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText(null);
            alert.setContentText("Connection could not be established. Please retry. Server may be down.");
            alert.showAndWait();
        });

    }

    @Override
    public void chooseConnection() {
        ChooseConnectionController.setGuiView(this);
        // Lanciare ChooseConnectionApp
        new Thread(() -> Application.launch(ChooseConnectionController.class)).start();


    }

    @Override
    public void handleMessage(Message message) {
        ServerToClientMessage toProcess = (ServerToClientMessage)message;
        toProcess.execute(client);
    }
    

    @Override
    public void notifyException(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Game Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @Override
    public void displayFirstMessage(int clientID) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Messaggio");
            alert.setHeaderText(null);
            alert.setContentText("Connessione stabilita");
//            alert.setOnCloseRequest(e->{
//                stage.close();
//            });
            alert.showAndWait();
        });
    }

    @Override
    public void displayGameList(HashMap<Integer, Integer> gameList) {

    }

    @Override
    public void notifyGameJoined(int idGame) {
        Platform.runLater(() -> {
            if (waitingController != null && waitingController.getNewPlayerJoined() != null) {
                waitingController.setPlayerNickname(client.getNickname());
            } else {
                System.err.println("Errore: setUpGame o newPlayerJoined TextArea è null");
            }
        });
    }

    @Override
    public void notifyNewPlayerJoined(ArrayList<String> nicknames) {
        Platform.runLater(() -> {
            if (waitingController != null && waitingController.getNewPlayerJoined() != null) {
                String lastNickname = nicknames.getLast();
                System.out.println("New player nickname: " + lastNickname);
                waitingController.addNickname(lastNickname);
            } else {
                System.err.println("Errore: setUpGame o newPlayerJoined TextArea è null");
            }
        });


    }

    @Override
    public void notifyStarterCard() {


    }

    @Override
    public void notifyColorsAvailable(List<PlayerColor> colors) {

    }

    @Override
    public void notifyGoldDeckUpdate() {

    }

    @Override
    public void notifyResourceDeckUpdate() {

    }

    @Override
    public void notifyDisplayedCardsUpdate(ArrayList<PlayableCard> displayedCards) {

    }

    @Override
    public void notifyCurrentPlayer(String nickname) {
        if(currentController.equals(chooseStarterController)){
            Platform.runLater(() -> {
                chooseStarterController.setTurn();
            });
        }



    }

    @Override
    public void notifyAvailablePositions(ArrayList<Coordinate> coord) {

    }

    @Override
    public void notifyHandObjectives(ArrayList<ObjectiveCard> cards) {

    }

    @Override
    public void notifyGameState(GameState state) {
        chooseStarterController=new ChooseStarterController();
        if(state==GameState.CHOOSING_STARTER_CARDS){
            Platform.runLater(() -> {
                try {
                    ChooseStarterController.setGuiView(this);
                    chooseStarterController.start(this.stage);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

    }

    @Override
    public void notifyGridUpdate(String nickname, Map<Coordinate, Face> grid) {

    }

    @Override
    public void notifyResourcesUpdate(String nickname, HashMap<Content, Integer> resources) {

    }

    @Override
    public void notifyWinnerUpdate(String nickname) {

    }

    @Override
    public void notifyHandCardsUpdate(ArrayList<PlayableCard> cards) {
        if(client.getVirtualView().getState()==GameState.CHOOSING_STARTER_CARDS){
            Platform.runLater(() -> {
                if(!cards.isEmpty())
                    chooseStarterController.showStarter(cards.get(0));
            });
        }

    }

    @Override
    public void notifyDisplayedObjectives(List<ObjectiveCard> cards) {

    }

    @Override
    public void notifyScores(Map<String, Integer> scores) {

    }

    @Override
    public void notifyColorChosen(PlayerColor color) {

    }

    @Override
    public void notifyChatMessage(ChatMessage message) {

    }

    @Override
    public void run() {
         //Application.launch(ChooseConnectionApp.class);
       //chooseConnection();
     }

    public void setClient(Client c){
        super.client = c;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setSetUpGameController(WaitingController waitingController) {
        this.waitingController = waitingController;
        this.currentController=waitingController;
    }

    public void setChooseObjectiveController(ChooseObjectiveController chooseObjectiveController) {
        this.chooseObjectiveController = chooseObjectiveController;
        this.currentController=chooseObjectiveController;
    }

    public void setChooseStarterController(ChooseStarterController chooseStarterController) {
        this.chooseStarterController = chooseStarterController;
        this.currentController=chooseStarterController;
    }


}
