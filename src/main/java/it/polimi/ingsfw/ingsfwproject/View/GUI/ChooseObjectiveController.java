package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Model.ObjectiveCard;
import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.ObjectiveCardChosenMessage;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsfw.ingsfwproject.View.View.client;

public class ChooseObjectiveController extends GUIController implements Initializable {
    @FXML public Text stateLabel;
    @FXML public Label turn;
    @FXML public ImageView firstCard;
    @FXML public ImageView secondCard;
    @FXML public Label labelInstruction;

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
        //stage.setMaximized(true);
        stage.show();
    }

    public static void setGuiView(GUIView view) {
        guiView = view;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Objects.equals(client.getNickname(), client.getVirtualView().getCurrentPlayer())){
            firstCard.setVisible(true);
            secondCard.setVisible(true);
        }
        setTurn();
    }

    public void setTurn(){
        if(Objects.equals(client.getNickname(), client.getVirtualView().getCurrentPlayer())){
            turn.setText("It's your turn!");
            firstCard.setVisible(true);
            secondCard.setVisible(true);
        }else{
            turn.setText("It's "+client.getVirtualView().getCurrentPlayer()+"'s turn");
        }
    }

    public void showObjective(ArrayList<ObjectiveCard> cards){
        int card1 = cards.getFirst().getIdCard();
        String id1 = String.format("%03d", card1);
        String id2=String.format("%03d",cards.getLast().getIdCard());
        Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_front/"+id1+".png")));
        Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_front/"+id2+".png")));
        firstCard.setImage(image1);
        secondCard.setImage(image2);
    }

    public void cardChosen(int cardID) throws IOException {
        ObjectiveCardChosenMessage objectiveCardChosenMessage=new ObjectiveCardChosenMessage(client.getClientID(), client.getNickname(), cardID);
        client.sendMessage(objectiveCardChosenMessage);
        firstCard.setVisible(false);
        secondCard.setVisible(false);
        labelInstruction.setText("Objective Card Chosen.\nWaiting for other players choice");
    }

    @FXML
    public void firstCardChosen(MouseEvent mouseEvent) throws IOException {
        int cardID=client.getVirtualView().getHandObjectives().getFirst().getIdCard();
        cardChosen(cardID);
    }

    @FXML
    public void secondCardChosen(MouseEvent mouseEvent) throws IOException {
        int cardID=client.getVirtualView().getHandObjectives().getLast().getIdCard();
        cardChosen(cardID);
    }



}
