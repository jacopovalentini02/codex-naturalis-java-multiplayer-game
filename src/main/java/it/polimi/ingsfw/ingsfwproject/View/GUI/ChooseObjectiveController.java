package it.polimi.ingsfw.ingsfwproject.View.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChooseObjectiveController extends GUIController {
    @FXML public Text stateLabel;
    @FXML public Label turn;
    @FXML public ImageView firstCard;
    @FXML public ImageView secondCard;

    public static GUIView guiView;
    @Override
    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/ChooseObjective.fxml"));
        Parent root = loader.load();
        ChooseObjectiveController chooseObjectiveController = loader.getController();
        guiView.setChooseObjectiveController(chooseObjectiveController);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Choosing Objective Card");
        stage.setScene(scene);
        stage.show();
    }

    public static void setGuiView(GUIView view) {
        guiView = view;
    }

}
