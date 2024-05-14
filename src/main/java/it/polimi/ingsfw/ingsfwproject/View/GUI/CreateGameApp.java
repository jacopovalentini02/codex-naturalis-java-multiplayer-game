package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static it.polimi.ingsfw.ingsfwproject.View.View.client;

public class CreateGameApp extends Application {
    @FXML
    private Button createButton;
    @FXML
    public TextField nickname_input;
    @FXML
    public Spinner num_of_players;
    public GUIView guiView;


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/createGame.fxml"));
        Parent root = loader.load();
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Create Game");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void sendCreateGame() throws IOException {
        String nickname = nickname_input.getText();
        int numberOfPlayers = (int) num_of_players.getValue();
        CreateGameMessage createGameMessage=new CreateGameMessage(client.getClientID(), numberOfPlayers, nickname);
        System.out.println(nickname);
        client.sendMessage(createGameMessage);
    }


    public void setGuiView(GUIView guiView) {
        this.guiView = guiView;
    }
}
