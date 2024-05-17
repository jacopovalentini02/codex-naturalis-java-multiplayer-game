package it.polimi.ingsfw.ingsfwproject.View.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SetUpGameController   {

    @FXML
    private TextArea newPlayerJoined;

    public static GUIView guiView;

    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/SetupGame.fxml"));
        Parent root = loader.load();
        SetUpGameController setUpGameController = loader.getController();
        guiView.setSetUpGameController(setUpGameController);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Set Up Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void setGuiView(GUIView view) {
        guiView = view;
    }

    public TextArea getNewPlayerJoined() {
        return newPlayerJoined;
    }

    public void addNickname(String nickname){
        newPlayerJoined.appendText(nickname + " has joined the game\n");
    }

}


