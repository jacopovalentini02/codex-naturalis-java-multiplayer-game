package it.polimi.ingsfw.ingsfwproject.View.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChooseConnectionApp extends Application {

    private static final double MIN_WIDTH = 449.0;
    private static final double MIN_HEIGHT = 441.0;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/prova-connection.fxml"));
        Parent root = loader.load();
        GUIView controller = loader.getController();
        controller.setStage(stage);
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

//        stage.setOnCloseRequest(e->{
//            try {
//                new LobbyApp().start(new Stage());
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        });

//        Button lobbyButton1 = (Button) scene.lookup("#socketButton");
//        Button lobbyButton2 = (Button) scene.lookup("#rmiButton");
//
//        // Aggiungere lo stesso gestore di eventi a entrambi i pulsanti
//        EventHandler<ActionEvent> goToLobby = event -> {
//            try {
//                new LobbyApp().start(new Stage());
//                stage.close(); // Chiudi la finestra corrente dopo aver avviato la LobbyApp
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        };
//
//        lobbyButton1.setOnAction(goToLobby);
//        lobbyButton2.setOnAction(goToLobby);




    }



}
