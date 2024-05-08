package it.polimi.ingsfw.ingsfwproject.View.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChooseConnectionApp extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/Choose Connection.fxml"));
        Parent root = loader.load();
        GUIView controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Choose connection");
        stage.setScene(scene);

        stage.setOnCloseRequest(e->{
            try {
                new LobbyApp().start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


        stage.show();
    }



}
