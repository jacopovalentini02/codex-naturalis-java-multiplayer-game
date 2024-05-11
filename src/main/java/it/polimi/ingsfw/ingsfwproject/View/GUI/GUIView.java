package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.View.GUI.ChooseConnectionApp;
import it.polimi.ingsfw.ingsfwproject.View.View;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GUIView extends View {

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


    @FXML
    private void handleSocketConnection() throws Exception {
        super.client = new SocketClient("localhost", 1337, this);
        super.client.startConnection();
        if (super.client.isConnected()) {
            openLobby();
        } else {
            showConnectionError();
        }
    }

    @FXML
    private void handleRMIConnection() throws Exception {
        super.client = new RMIClient("localhost", 1099, this);
        super.client.startConnection();
        if (super.client.isConnected()) {
            openLobby();
        } else {
            showConnectionError();
        }
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

    private void showConnectionError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText(null);
            alert.setContentText("Connection could be not established. Please retry");
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





    public GUIView(){
        super.messages = new LinkedBlockingQueue<>();
        Thread readerthread = new Thread(super::receiveMessage);
        readerthread.start();
    }





    @Override
    public void chooseConnection() {

    }



    @Override
    public void handleMessage(Message message) {
        switch (message.getType()){
            case FIRST_MESSSAGE: {
                Platform.runLater(() -> {
                    // Mostra una finestra di dialogo con il messaggio
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
        }

    }

    @Override
    public void handleInput(String input) {

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
