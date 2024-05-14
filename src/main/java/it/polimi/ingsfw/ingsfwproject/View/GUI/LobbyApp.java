package it.polimi.ingsfw.ingsfwproject.View.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LobbyApp extends Application {

    @FXML
    private Button refreshButton;
    @FXML
    private Button createGameLobby;

    GUIView guiView;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/Lobby.fxml"));
        Parent root = loader.load();
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Lobby");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void openCreateGame(){
        Platform.runLater(() -> {
            try {
                CreateGameApp createGame=new CreateGameApp();
                createGame.setGuiView(guiView);
                createGame.start(guiView.getStage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void setGuiView(GUIView guiView) {
        this.guiView = guiView;
    }
}
