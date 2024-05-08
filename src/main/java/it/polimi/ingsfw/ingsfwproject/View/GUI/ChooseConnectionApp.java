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


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/prova-connection.fxml"));
        Parent root = loader.load();
        GUIView controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Choose connection");
        stage.setScene(scene);

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

        stage.show();
    }



}
