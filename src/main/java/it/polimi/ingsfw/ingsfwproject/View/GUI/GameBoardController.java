package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.DrawMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.PickMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.PlayCardMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

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
    @FXML public GridPane playerGround;
    @FXML public Button showGrid;
    @FXML public AnchorPane pane;
    @FXML
    public ScrollPane scrollpane;

    private int offsetX;
    private int offsetY;
    private boolean[] faceShowed;

    private Map<PlayerColor, String> colorImageMap;
    private HashMap<Rectangle, Coordinate> rectangleCoordinateHashMap;

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
        //stage.setMaximized(true);
        stage.show();
        offsetX=0;
        offsetY=0;

    }
    public static void setGuiView(GUIView view) {
        guiView = view;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        faceShowed = new boolean[]{true, true, true}; //
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

        rectangleCoordinateHashMap = new HashMap<>();
        initializeScore();
        updatePane();
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
    public void turnFirstCard(){

    }

    @FXML
    private void dragCard1(MouseEvent event){
        Dragboard db = handCard1.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        int cardID = client.getVirtualView().getHandCards().getFirst().getIdCard();
        String data = cardID + ";" + faceShowed[0];
        content.putString(data);
        db.setContent(content);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setTransform(new Scale(0.7, 0.7));
        WritableImage snapshot = handCard1.snapshot(parameters, null);
        db.setDragView(snapshot);
        System.out.println("Drag rilevato");
        event.consume();
    }

    @FXML
    private void dragCard2(MouseEvent event){
        Dragboard db = handCard2.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        int cardID = client.getVirtualView().getHandCards().get(1).getIdCard();
        String data = cardID + ";" + faceShowed[1];
        content.putString(data);
        db.setContent(content);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setTransform(new Scale(0.7, 0.7));
        WritableImage snapshot = handCard2.snapshot(parameters, null);
        db.setDragView(snapshot);
        System.out.println("Drag rilevato");
        event.consume();
    }

    @FXML
    private void dragCard3(MouseEvent event){
        Dragboard db = handCard3.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        int cardID = client.getVirtualView().getHandCards().getLast().getIdCard();
        String data = cardID + ";" + faceShowed[2];
        content.putString(data);
        db.setContent(content);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setTransform(new Scale(0.7, 0.7));
        WritableImage snapshot = handCard3.snapshot(parameters, null);
        db.setDragView(snapshot);
        System.out.println("Drag rilevato");
        event.consume();
    }

    @FXML
    private void clickHandCard1(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY){
            String id = String.format("%03d",client.getVirtualView().getHandCards().getFirst().getIdCard());
            if(faceShowed[0]){ //change to back image
                handCard1.setImage(getImageBack(id));
                faceShowed[0]=false;
            }else{
                handCard1.setImage(getImageFront(id));
                faceShowed[0]=true;
            }
        }
    }
    @FXML
    private void clickHandCard2(MouseEvent event){
        if (event.getButton() == MouseButton.PRIMARY){
            String id = String.format("%03d",client.getVirtualView().getHandCards().get(1).getIdCard());
            if(faceShowed[1]){ //change to back image
                handCard2.setImage(getImageBack(id));
                faceShowed[1]=false;
            }else{
                handCard2.setImage(getImageFront(id));
                faceShowed[1]=true;
            }
        }
    }

    @FXML
    private void clickHandCard3(MouseEvent event){
       if (event.getButton() == MouseButton.PRIMARY){
            String id = String.format("%03d",client.getVirtualView().getHandCards().getLast().getIdCard());
            if(faceShowed[2]){ //change to back image
                handCard3.setImage(getImageBack(id));
                faceShowed[2]=false;
            }else{
                handCard3.setImage(getImageFront(id));
                faceShowed[2]=true;
            }
        }
    }

    @FXML
    public void pickResource1(MouseEvent mouseEvent)throws IOException {
        int cardID=client.getVirtualView().getDisplayedCards().getFirst().getIdCard();
        cardToPick(cardID);
    }

    @FXML
    public void pickResource2(MouseEvent mouseEvent)throws IOException {
        int cardID=client.getVirtualView().getDisplayedCards().get(1).getIdCard();
        cardToPick(cardID);
    }
    @FXML
    public void pickGold1(MouseEvent mouseEvent)throws IOException {
        int cardID=client.getVirtualView().getDisplayedCards().get(2).getIdCard();
        cardToPick(cardID);
    }
    @FXML
    public void pickGold2(MouseEvent mouseEvent)throws IOException {
        int cardID=client.getVirtualView().getDisplayedCards().getLast().getIdCard();
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

    private int findXmin(){
        Map<Coordinate, Face> grid = client.getVirtualView().getGrids().get(client.getNickname());
        Coordinate coord=grid.keySet().stream().min((Comparator.comparingInt(Coordinate::getX))).orElse(null);
        assert coord != null;
        return coord.getX();
    }

    private int findYmin(){
        Map<Coordinate, Face> grid = client.getVirtualView().getGrids().get(client.getNickname());
        Coordinate coord=grid.keySet().stream().min((Comparator.comparingInt(Coordinate::getY))).orElse(null);
        assert coord != null;
        return coord.getY();
    }

    public void updateGrid(){
        playerGround.getChildren().clear();

        Map<Coordinate, Face> grid = client.getVirtualView().getGrids().get(client.getNickname());

        int centerX = playerGround.getColumnCount() / 2;
        int centerY = playerGround.getRowCount() / 2;

        offsetX=Math.abs(findXmin());
        offsetY=Math.abs(findYmin());
        System.out.println(offsetX);
        System.out.println(offsetY);
        // Itera su tutte le voci della mappa e aggiungi le immagini alla griglia
        for (Map.Entry<Coordinate, Face> entry : grid.entrySet()) {

            Coordinate coordinate = entry.getKey();
            Face face = entry.getValue();

            int gridX = centerX+offsetX + coordinate.getX();
            int gridY = centerY+offsetY - coordinate.getY();

            System.out.println("x grid"+gridX);
            System.out.println(gridY);

            String imagePath = face.getImagePath();

            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            ImageView imageView = new ImageView(image);

            imageView.setFitHeight(110);
            imageView.setFitWidth(150);

            playerGround.add(imageView, gridX, gridY);
        }
    }

    public void updatePane(){
        double centerX=pane.getPrefWidth()/2;
        double centerY=pane.getPrefHeight()/2;

        double maxX = 0.0;
        double maxY = 0.0;

        double lastCardPosX = 0.0;
        double lastCardPosY = 0.0;

        Map<Coordinate, Face> grid = client.getVirtualView().getGrids().get(client.getNickname());
        Iterator<Map.Entry<Coordinate, Face>> iterator = grid.entrySet().iterator();
        System.out.println("centro x: "+centerX);

        pane.getChildren().clear();

        while (iterator.hasNext()) {
            Map.Entry<Coordinate, Face> entry = iterator.next();
            Coordinate coordinate = entry.getKey();
            Face face = entry.getValue();

            String imagePath = face.getImagePath();

            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            ImageView imageView = new ImageView(image);

            imageView.setFitHeight(110);
            imageView.setFitWidth(150);

            double cardPosX=centerX-75+coordinate.getX() * 121;
            double cardPosY=centerY+55- coordinate.getY() * 66;

            if (cardPosX > maxX)
                maxX = cardPosX;

            if (cardPosY > maxY)
                maxY = cardPosY;


            imageView.setLayoutX(cardPosX);
            imageView.setLayoutY(cardPosY);


            pane.getChildren().add(imageView);

            if (!iterator.hasNext()){ //printing the last card
               lastCardPosX = cardPosX;
               lastCardPosY = cardPosY;
            }
        }
        double newWidth = Math.max(pane.getPrefWidth(), maxX + 150);
        double newHeight = Math.max(pane.getPrefHeight(), maxY + 110);
        pane.setPrefHeight(newHeight);
        pane.setPrefWidth(newWidth);

        double hValue = (lastCardPosX - (scrollpane.getWidth() / 2)) / (pane.getPrefWidth() - scrollpane.getWidth());
        double vValue = (lastCardPosY - (scrollpane.getHeight() / 2)) / (pane.getPrefHeight() - scrollpane.getHeight());

        hValue = Math.max(0, Math.min(hValue, 1));
        vValue = Math.max(0, Math.min(vValue, 1));

        scrollpane.setPannable(true);
        scrollpane.setHvalue(hValue);
        scrollpane.setVvalue(vValue);
        System.out.println("settati");

        rectangleCoordinateHashMap.clear();

        for (Coordinate c: client.getVirtualView().getAvailablePositions()){
            double rectangleX = centerX - 75 + c.getX() * 121;
            double rectangleY = centerY + 55 - c.getY()*66;

            Rectangle rectangle = new Rectangle((int) rectangleX, (int) rectangleY, 150, 110);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStroke(Color.web("#5F3075"));
            rectangle.setStrokeWidth(5);

            rectangle.setOnDragOver(event -> {
                if (event.getGestureSource() != rectangle && event.getDragboard().hasString() && Objects.equals(client.getNickname(), client.getVirtualView().getCurrentPlayer())){
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            rectangle.setOnDragDropped(event ->{
                Dragboard db = event.getDragboard();
                boolean success = false;
                String[] values = db.getString().split(";");
                int cardID = Integer.parseInt(values[0]);
                boolean isFront = Boolean.parseBoolean(values[1]);
                System.out.println("Id carta: " + cardID + "boolean: " + isFront);
                Coordinate coordinateToPlay = rectangleCoordinateHashMap.get(rectangle);
                PlayCardMessage pm = new PlayCardMessage(client.getClientID(), cardID, isFront, coordinateToPlay, client.getNickname());
                try {
                    client.sendMessage(pm);
                } catch (IOException ignore){}
            });

            pane.getChildren().add(rectangle);
            rectangleCoordinateHashMap.put(rectangle, c);
        }
    }

}
