package it.polimi.ingsfw.ingsfwproject.View.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WaitingController extends GUIController{

    @FXML
    public Text stateLabel;
    @FXML
    public Label playerNickname;
    @FXML
    private TextArea newPlayerJoined;

    public static GUIView guiView;

    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/Waiting.fxml"));
        Parent root = loader.load();
        WaitingController waitingController = loader.getController();
        guiView.setSetUpGameController(waitingController);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Waiting");
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

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname.setText("Welcome "+playerNickname+"!");
    }
}


