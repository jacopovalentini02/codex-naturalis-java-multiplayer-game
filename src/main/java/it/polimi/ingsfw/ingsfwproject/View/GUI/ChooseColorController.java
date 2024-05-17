package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetColorAvailableMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.WantThatColorMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.ColorAvailableMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsfw.ingsfwproject.View.View.client;

public class ChooseColorController extends GUIController implements Initializable {
    public static GUIView guiView;
    @FXML public Text stateLabel;
    @FXML public Label turn;
    @FXML public Label labelInstruction;
    @FXML public Rectangle blueRectangle;
    @FXML public Rectangle greenRectangle;
    @FXML public Rectangle redRectangle;
    @FXML public Rectangle yellowRectangle;


    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/ChooseColor.fxml"));
        Parent root = loader.load();
        ChooseColorController chooseColorController = loader.getController();
        guiView.setChooseColorController(chooseColorController);
        guiView.setStage(stage);
        Scene scene = new Scene(root);

        stage.setTitle("Choosing Color");
        stage.setScene(scene);
        stage.show();

    }

    public static void setGuiView(GUIView view) {
        guiView = view;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            client.sendMessage(new GetColorAvailableMessage(client.getClientID()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setTurn();
    }

    public void setTurn(){
        if(Objects.equals(client.getNickname(), client.getVirtualView().getCurrentPlayer())){
            turn.setText("It's your turn!");

        }else{
            turn.setText("It's "+client.getVirtualView().getCurrentPlayer()+"'s turn");
        }
    }

    public void setColors(List<PlayerColor> colors){
        if(colors.contains(PlayerColor.BLUE)){
            blueRectangle.setVisible(true);
        }

        if(colors.contains(PlayerColor.RED)){
            redRectangle.setVisible(true);
        }

        if(colors.contains(PlayerColor.GREEN)){
            greenRectangle.setVisible(true);
        }

        if(colors.contains(PlayerColor.YELLOW)){
            yellowRectangle.setVisible(true);
        }
    }

    public void blueChosen() throws IOException {
        PlayerColor colorChosen=PlayerColor.BLUE;
        sendColor(colorChosen);
    }
    public void yellowChosen() throws IOException {
        PlayerColor colorChosen=PlayerColor.YELLOW;
        sendColor(colorChosen);
    }
    public void redChosen() throws IOException {
        PlayerColor colorChosen=PlayerColor.RED;
        sendColor(colorChosen);
    }
    public void greenChosen() throws IOException {
        PlayerColor colorChosen=PlayerColor.GREEN;
        sendColor(colorChosen);
    }

    public void sendColor(PlayerColor colorChosen) throws IOException {
        WantThatColorMessage wantThatColorMessage=new WantThatColorMessage(client.getClientID(), client.getNickname(),colorChosen);
        client.sendMessage(wantThatColorMessage);

        blueRectangle.setVisible(false);
        greenRectangle.setVisible(false);
        redRectangle.setVisible(false);
        yellowRectangle.setVisible(false);
        labelInstruction.setText("Color Chosen.\nWaiting for other players choice");
    }



}
