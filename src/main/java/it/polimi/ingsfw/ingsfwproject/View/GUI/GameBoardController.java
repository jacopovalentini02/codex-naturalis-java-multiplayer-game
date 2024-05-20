package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Model.ObjectiveCard;
import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.DrawMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.PickMessage;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static it.polimi.ingsfw.ingsfwproject.View.View.client;

public class GameBoardController extends GUIController implements Initializable {
    @FXML public Label turn;
    @FXML public ImageView goldDeck;
    @FXML public ImageView resourceDeck;
    @FXML public ImageView resourceDisplayed1;
    @FXML public ImageView resourceDisplayed2;
    @FXML public ImageView goldDisplayed1;
    @FXML public ImageView goldDisplayed2;
    @FXML public ImageView objectiveDisplayed1;
    @FXML public ImageView objectiveDisplayed2;
    @FXML public ImageView personalObjective;
    @FXML public ImageView handCard1;
    @FXML public ImageView handCard2;
    @FXML public ImageView handCard3;
    @FXML public ImageView pin1;
    @FXML public ImageView pin2;
    @FXML public ImageView pin3;
    @FXML public ImageView pin4;
    @FXML public ImageView pin11;
    @FXML public ImageView pin21;
    @FXML public ImageView pin31;
    @FXML public ImageView pin41;

    public static GUIView guiView;

    private Map<PlayerColor, String> colorImageMap;


    //todo: aggiungere un arraylist con i colori degli altri giocatori
    @Override
    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/GameBoard.fxml"));
        Parent root = loader.load();
        GameBoardController gameBoardController = loader.getController();
        guiView.setGameBoardController(gameBoardController);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Game");
        stage.setScene(scene);
        stage.show();

    }
    public static void setGuiView(GUIView view) {
        guiView = view;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTurn();
        updateHand();
        setPersonalObjective();
        updateGoldDeck();
        updateResourceDeck();
        updateDisplayedCard();
        setObjectiveDisplayed();

        colorImageMap=new HashMap<>();
        colorImageMap.put(PlayerColor.BLUE, "/it/polimi/ingsfw/ingsfwproject/Images/CODEX_pion_bleu.png");
        colorImageMap.put(PlayerColor.RED, "/it/polimi/ingsfw/ingsfwproject/Images/CODEX_pion_rouge.png");
        colorImageMap.put(PlayerColor.YELLOW, "/it/polimi/ingsfw/ingsfwproject/Images/CODEX_pion_jaune.png");
        colorImageMap.put(PlayerColor.GREEN, "/it/polimi/ingsfw/ingsfwproject/Images/CODEX_pion_vert.png");

        initializeScore();
    }

    public void setTurn(){
        if(Objects.equals(client.getNickname(), client.getVirtualView().getCurrentPlayer())){
            turn.setText("It's your turn!");
        }else{
            turn.setText("It's "+client.getVirtualView().getCurrentPlayer()+"'s turn");
        }
    }

    public void updateHand(){
        List<ImageView> cardViews = List.of(handCard1, handCard2, handCard3);

        for (int i = 0; i < client.getVirtualView().getHandCards().size() && i < cardViews.size(); i++) {
            String id = String.format("%03d", client.getVirtualView().getHandCards().get(i).getIdCard());
            cardViews.get(i).setImage(getImageFront(id));
            cardViews.get(i).setVisible(true);
        }

        if(client.getVirtualView().getHandCards().size()==2)
            cardViews.get(2).setVisible(false);
    }

    public void setPersonalObjective(){
        String id = String.format("%03d", client.getVirtualView().getHandObjectives().getFirst().getIdCard());
        personalObjective.setImage(getImageFront(id));
    }

    public void updateGoldDeck(){
        String id = String.format("%03d", client.getVirtualView().getGoldDeck().getCardList().getFirst().getIdCard());
        goldDeck.setImage(getImageBack(id));
    }

    public void updateResourceDeck(){
        String id = String.format("%03d", client.getVirtualView().getResourceDeck().getCardList().getFirst().getIdCard());
        resourceDeck.setImage(getImageBack(id));
    }

    public void updateDisplayedCard(){
        ArrayList<PlayableCard> displayedCards=client.getVirtualView().getDisplayedCards();
        List<ImageView> cardViews = List.of(resourceDisplayed1, resourceDisplayed2, goldDisplayed1, goldDisplayed2);

        for (int i = 0; i < displayedCards.size(); i++) {
            String id = String.format("%03d", displayedCards.get(i).getIdCard());
            cardViews.get(i).setImage(getImageFront(id));
            cardViews.get(i).setVisible(true);
        }
    }

    public void setObjectiveDisplayed(){
        List<ObjectiveCard> objectiveCards=client.getVirtualView().getDisplayedObjectiveCards();
        List<ImageView> cardViews = List.of(objectiveDisplayed1,objectiveDisplayed2);

        for (int i = 0; i < objectiveCards.size(); i++) {
            String id = String.format("%03d", objectiveCards.get(i).getIdCard());
            cardViews.get(i).setImage(getImageFront(id));
        }
    }

    public Image getImageBack(String id){
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_back/" + id + ".png")));
    }

    public Image getImageFront(String id){
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_front/" + id + ".png")));
    }

    public void initializeScore(){
        ImageView[] pinGroups = {pin1, pin2, pin3, pin4};
        Map<String, PlayerColor> playerColorMap = client.getVirtualView().getPlayerColorMap();
        int index=0;
        for (Map.Entry<String, PlayerColor> entry : playerColorMap.entrySet()) {
            PlayerColor playerColor = entry.getValue();

            if (playerColor != null && index < playerColorMap.size()) {
                String imagePath = colorImageMap.get(playerColor);
                if (imagePath != null) {
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                    pinGroups[index].setImage(image);
                    index++;
                }
            }
        }
    }

    public void setTokenImage(){
        ImageView[][] pinGroups = {
                {pin1, pin2, pin3, pin4},
                {pin11, pin21, pin31, pin41},
        };

        Map<String, Integer> scores = client.getVirtualView().getScores();
        Map<String, PlayerColor> playerColorMap = client.getVirtualView().getPlayerColorMap();

        for (ImageView[] pinGroup : pinGroups) {
            for (ImageView pin : pinGroup) {
                pin.setImage(null);
            }
        }

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String playerName = entry.getKey();
            PlayerColor playerColor = playerColorMap.get(playerName);
            int score=entry.getValue();

            //System.out.println(playerName+"-"+score);
            String imagePath = colorImageMap.get(playerColor);
            if (imagePath != null) {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                for (ImageView pin : pinGroups[score]) {
                    if (pin.getImage() == null) {
                        pin.setImage(image);
                        break;
                    }
                }
            }



        }
    }


    //Gestione input
    @FXML
    public void firstCardChosen(MouseEvent mouseEvent) throws IOException {
        int cardID=client.getVirtualView().getHandCards().getFirst().getIdCard();
        cardToPlayChosen(cardID);
    }

    //todo input coordinate, face
    public void cardToPlayChosen(int cardID) throws IOException {
        PlayCardMessage playCardMessage=new PlayCardMessage(client.getClientID(),cardID,true, new Coordinate(1,1),client.getNickname());
        client.sendMessage(playCardMessage);
    }
    @FXML
    public void pickResource1(MouseEvent mouseEvent)throws IOException {
        int cardID=client.getVirtualView().getDisplayedCards().getFirst().getIdCard();
        cardToPick(cardID);
    }

    public void cardToPick(int cardID) throws IOException {
        PickMessage pickMessage=new PickMessage(client.getClientID(), cardID, client.getNickname());
        client.sendMessage(pickMessage);
    }

    @FXML
    public void drawResource(MouseEvent mouseEvent) throws IOException {
        drawCard(true);
    }
    @FXML
    public void drawGold(MouseEvent mouseEvent) throws IOException {
        drawCard(false);
    }

    public void drawCard(Boolean deckWanted) throws IOException {
        DrawMessage messageToSend = new DrawMessage(client.getClientID(), client.getNickname(), deckWanted);
        client.sendMessage(messageToSend);
    }




}
