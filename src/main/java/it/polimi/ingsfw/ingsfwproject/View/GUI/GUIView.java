package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;
import it.polimi.ingsfw.ingsfwproject.View.GUI.ChooseConnectionApp;
import it.polimi.ingsfw.ingsfwproject.View.View;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GUIView extends View {

    @FXML
    public TextField nickname_input;
    @FXML
    public Spinner num_of_players;
    private Stage stage;

    @FXML
    private Button socketButton;

    @FXML
    private Button rmiButton;

    @FXML
    private Button aleButton;

    @FXML
    private Button createButton;

    @FXML
    private Button refreshButton;

    @FXML
    private ImageView ale;


    public GUIView(){
        super.messages = new LinkedBlockingQueue<>();
        Thread readerthread = new Thread(super::receiveMessage);
        readerthread.start();
        //chooseConnection();
    }

    @FXML
    private void handleSocketConnection() throws Exception {
        try {
            super.client = new SocketClient("localhost", 1337, this);
            super.client.startConnection();
            if (super.client.isConnected()) {
                openLobby();
            } else {
                showConnectionError();
            }
        } catch (ConnectException e) {
            showConnectionError();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleRMIConnection() {
        //todo qua c'è un'eccezione che da errore, l'alert viene comunue visualizzato
        try {
            // Codice per la connessione RMI
            super.client = new RMIClient("localhost", 1099, this);
            super.client.startConnection();
            openLobby();
        } catch (Exception e) {
            showConnectionError();
            e.printStackTrace();
        }

    }


    @FXML
    private void sendCreateGame() throws IOException {
        String nickname = nickname_input.getText();
        int numberOfPlayers = (int) num_of_players.getValue();
        CreateGameMessage createGameMessage=new CreateGameMessage(super.client.getClientID(), numberOfPlayers, nickname);
        System.out.println(nickname);
        this.client.sendMessage(createGameMessage);
    }


    private void openLobby() {
        Platform.runLater(() -> {
            try {
                new LobbyApp().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @FXML
    private void openCreateGame(){
        Platform.runLater(() -> {
            try {
                new CreateGameApp().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void showConnectionError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText(null);
            alert.setContentText("Connection could not be established. Please retry. Server may be down.");
            alert.showAndWait();
        });

    }

    @FXML
    private void showAle(){
        ale.setVisible(true);
        RotateTransition rt = new RotateTransition(Duration.seconds(2), ale);
        rt.setByAngle(360);
        rt.setCycleCount(RotateTransition.INDEFINITE);
        rt.play();
    }








    @Override
    public void chooseConnection() {
        ChooseConnectionApp chooseConnectionApp = new ChooseConnectionApp();
        try {
            chooseConnectionApp.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            alert.setOnCloseRequest(e->{
                stage.close();
            });
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
    public void run() {
        Application.launch(ChooseConnectionApp.class);
    }

    public void setClient(Client c){
        super.client = c;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }


}
