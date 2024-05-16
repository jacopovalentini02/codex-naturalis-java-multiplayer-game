package it.polimi.ingsfw.ingsfwproject.View.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class LobbyGUIController {

    @FXML
    public Button createButtonLobby;
    public TableView tableView;
    public TableColumn gameIdColumn;
    public TableColumn playerCountColumn;
    public TableColumn joinColumn;
    @FXML
    private Button refreshButton;


    public static GUIView guiView;

    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        System.out.println(guiView);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/Lobby.fxml"));

        Parent root = loader.load();
        loader.setController(this);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Lobby");
        stage.setScene(scene);
        System.out.println(guiView);
        stage.show();
        System.out.println(guiView);

    }

    @FXML
    public void openCreateGame(){
        System.out.println(guiView);
       guiView.openCreateGameWindow();
    }


    public static void setGuiView(GUIView view) {
        guiView = view;
    }
}
