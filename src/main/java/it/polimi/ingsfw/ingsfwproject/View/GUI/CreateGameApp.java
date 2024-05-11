package it.polimi.ingsfw.ingsfwproject.View.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateGameApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/createGame.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Create Game");
        stage.setScene(scene);
        stage.show();
    }


}
