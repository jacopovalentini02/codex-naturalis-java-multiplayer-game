package it.polimi.ingsfw.ingsfwproject.View2.GUI;

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
        Scene scene = new Scene(root);
        stage.setTitle("Choose connection");
        stage.setScene(scene);
        stage.show();
    }



}
