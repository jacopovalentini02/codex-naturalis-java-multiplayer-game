package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.PlayCardMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsfw.ingsfwproject.View.View.client;

public class ChooseStarterController extends GUIController implements Initializable  {
    @FXML public Text stateLabel;
    @FXML public Label turn;
    @FXML public ImageView starterFront;
    @FXML public ImageView starterBack;
    @FXML public Label labelInstruction;

    public static GUIView guiView;


    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/ChoosingStarter.fxml"));
        Parent root = loader.load();
        ChooseStarterController chooseStarterController = loader.getController();
        guiView.setChooseStarterController(chooseStarterController);
        guiView.setStage(stage);
        Scene scene = new Scene(root);


        stage.setTitle("Choosing Starter Card");
        stage.setScene(scene);
        stage.show();

    }

    public static void setGuiView(GUIView view) {
        guiView = view;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Objects.equals(client.getNickname(), client.getVirtualView().getCurrentPlayer())){
            starterFront.setVisible(true);
            starterBack.setVisible(true);
        }
        setTurn();
    }

    public void showStarter(PlayableCard card){
        String id="0"+card.getIdCard();
        System.out.println(id);
        Image imageBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_back/"+id+".png")));
        Image imageFront = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_front/"+id+".png")));
        starterFront.setImage(imageFront);
        starterBack.setImage(imageBack);
    }

    @FXML
    public void sendFront(MouseEvent mouseEvent) throws IOException {
        PlayCardMessage playCardMessage=new PlayCardMessage(client.getClientID(), client.getVirtualView().getHandCards().getFirst().getIdCard(), true, new Coordinate(0,0), client.getNickname());
        client.sendMessage(playCardMessage);
        cardChosen();
    }

    @FXML
    public void sendBack(MouseEvent mouseEvent) throws IOException {
        PlayCardMessage playCardMessage=new PlayCardMessage(client.getClientID(), client.getVirtualView().getHandCards().getFirst().getIdCard(), false, new Coordinate(0,0), client.getNickname());
        client.sendMessage(playCardMessage);
        cardChosen();
    }

    public void cardChosen(){
        starterFront.setVisible(false);
        starterBack.setVisible(false);
        labelInstruction.setText("Card Chosen.\nWaiting for other players choice");
    }

    public void setTurn(){
        if(Objects.equals(client.getNickname(), client.getVirtualView().getCurrentPlayer())){
            turn.setText("It's your turn!");
            starterFront.setVisible(true);
            starterBack.setVisible(true);
        }else{
            turn.setText("It's "+client.getVirtualView().getCurrentPlayer()+"'s turn");

        }
    }
}
