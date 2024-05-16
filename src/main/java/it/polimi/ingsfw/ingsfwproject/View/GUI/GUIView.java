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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GUIView extends View {


    private Stage stage;

    private SetUpGameController setUpGameController;


    public GUIView(){
//        super.messages = new LinkedBlockingQueue<>();
//        Thread readerthread = new Thread(super::receiveMessage);
//        readerthread.start();
        chooseConnection();
    }

    public void openLobby() {
        Platform.runLater(() -> {
            try {
                LobbyGUIController lobbyGUIController =new LobbyGUIController();
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
        setUpGameController =new SetUpGameController();

        Platform.runLater(() -> {
            try {
                SetUpGameController.setGuiView(this);
                setUpGameController.start(this.stage);
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

    }

    @Override
    public void notifyNewPlayerJoined(ArrayList<String> nicknames) {
        System.out.println("notifyNewPlayerJoined chiamato con nicknames: " + nicknames);
        Platform.runLater(() -> {
            if (setUpGameController != null && setUpGameController.getNewPlayerJoined() != null) {
                String lastNickname = nicknames.get(nicknames.size() - 1);
                System.out.println("Aggiungo nickname: " + lastNickname);
                setUpGameController.addNickname(lastNickname);
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

    }

    @Override
    public void notifyAvailablePositions(ArrayList<Coordinate> coord) {

    }

    @Override
    public void notifyHandObjectives(ArrayList<ObjectiveCard> cards) {

    }

    @Override
    public void notifyGameState(GameState state) {

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
}
