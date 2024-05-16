package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
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

public class CreateGameController {
    @FXML
    private Button createButton;
    @FXML
    public TextField nickname_input;
    @FXML
    public Spinner num_of_players;
    public static GUIView guiView;


    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/createGame.fxml"));

        Parent root = loader.load();
        loader.setController(this);
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
        if(nickname.length()<2){
            guiView.notifyException("Nickname length should be minimum 2 character");
        }else{
            CreateGameMessage createGameMessage=new CreateGameMessage(client.getClientID(), numberOfPlayers, nickname);
            client.sendMessage(createGameMessage);
            guiView.openSetUpGame();
        }

    }


    public void setGuiView(GUIView gui) {
        guiView = gui;
    }
}
