package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import static it.polimi.ingsfw.ingsfwproject.View.View.client;


import java.net.ConnectException;

public class ChooseConnectionController extends Application  {
    @FXML
    private Button socketButton;
    @FXML
    private Button rmiButton;
    public static GUIView guiView;

    private static final double MIN_WIDTH = 449.0;
    private static final double MIN_HEIGHT = 441.0;
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/ChooseConnection.fxml"));

        Parent root = loader.load();
        loader.setController(this);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Choose connection");
        stage.setScene(scene);

        // Ascoltatore per gestire le dimensioni della finestra
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() < MIN_WIDTH) {
                stage.setMinWidth(newValue.doubleValue());
            } else {
                stage.setMinWidth(MIN_WIDTH);
            }
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() < MIN_HEIGHT) {
                stage.setMinHeight(newValue.doubleValue());
            } else {
                stage.setMinHeight(MIN_HEIGHT);
            }
        });
        stage.show();
        stage.setOnCloseRequest((event->{
            Platform.exit();
            System.exit(0);
        }));
    }

    @FXML
    private void handleSocketConnection(){
        try {
            client = new SocketClient("localhost", 1337, guiView);
            client.startConnection();
            if (client.isConnected()) {
                guiView.openLobby();
            } else {
                guiView.showConnectionError();
            }
        } catch (ConnectException e) {
            guiView.showConnectionError();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRMIConnection() {
        //todo qua c'è un'eccezione che da errore, l'alert viene comunue visualizzato
        try {
            // Codice per la connessione RMI
            client = new RMIClient("localhost", 1099, guiView);
            client.startConnection();
            guiView.openLobby();
        } catch (Exception e) {
            guiView.showConnectionError();
        }

    }


    public static void setGuiView(GUIView gui) {
        guiView = gui;
    }



//    public static void main(String [] args){
//        launch(args);
//    }
}
