package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import javafx.application.Platform;
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

/**
 * The controller of the scene where a new game is created
 */
public class CreateGameController extends GUIController{
    @FXML
    private Button createButton;
    @FXML
    public TextField nickname_input;
    @FXML
    public Spinner num_of_players;
    public static GUIView guiView;

    /**
     * Initializes and displays the stage for creating a game.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during loading or setting up the scene
     */
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/createGame.fxml"));

        Parent root = loader.load();
        loader.setController(this);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Create Game");
        stage.setScene(scene);
        stage.centerOnScreen();

        stage.show();
        stage.setOnCloseRequest((event->{
            Platform.exit();
            System.exit(0);
        }));
    }

    /**
     * Sends a game creation message with the chosen nickname and number of players.
     *
     * @throws IOException if an error occurs while sending the message
     */
    @FXML
    private void sendCreateGame() throws IOException {
        String nickname = nickname_input.getText();
        int numberOfPlayers = (int) num_of_players.getValue();
        if(nickname.length()<2){
            guiView.notifyException("Nickname length should be minimum 2 characters");
        }else{
            CreateGameMessage createGameMessage=new CreateGameMessage(client.getClientID(), numberOfPlayers, nickname);
            client.sendMessage(createGameMessage);
            guiView.openWaiting();

        }
    }

    /**
     * Sets the GUI view for the application.
     *
     * @param gui the {@link GUIView} instance to be set
     */
    public void setGuiView(GUIView gui) {
        guiView = gui;
    }
}
