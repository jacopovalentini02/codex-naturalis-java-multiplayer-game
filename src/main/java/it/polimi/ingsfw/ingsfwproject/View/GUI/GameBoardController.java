package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.DrawMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.PickMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.PlayCardMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.sendChatMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

import static it.polimi.ingsfw.ingsfwproject.View.View.client;

/**
 * Controller to manage the game GUI.
 */
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

    @FXML public AnchorPane pane;
    @FXML
    public ScrollPane scrollpane;
    public ImageView pin12;
    public ImageView pin13;
    public ImageView pin14;
    public ImageView pin22;
    public ImageView pin23;
    public ImageView pin24;
    public ImageView pin32;
    public ImageView pin33;
    public ImageView pin34;
    public ImageView pin42;
    public ImageView pin43;
    public ImageView pin44;
    public ImageView pin51;
    public ImageView pin52;
    public ImageView pin53;
    public ImageView pin54;
    public ImageView pin61;
    public ImageView pin62;
    public ImageView pin63;
    public ImageView pin64;
    public ImageView pin71;
    public ImageView pin72;
    public ImageView pin73;
    public ImageView pin74;
    public ImageView pin81;
    public ImageView pin82;
    public ImageView pin83;
    public ImageView pin84;
    public ImageView pin91;
    public ImageView pin92;
    public ImageView pin93;
    public ImageView pin94;
    public ImageView pin101;
    public ImageView pin102;
    public ImageView pin103;
    public ImageView pin104;
    public ImageView pin111;
    public ImageView pin112;
    public ImageView pin113;
    public ImageView pin114;
    public ImageView pin121;
    public ImageView pin122;
    public ImageView pin123;
    public ImageView pin124;
    public ImageView pin131;
    public ImageView pin132;
    public ImageView pin133;
    public ImageView pin134;
    public ImageView pin141;
    public ImageView pin142;
    public ImageView pin143;
    public ImageView pin144;
    public ImageView pin151;
    public ImageView pin152;
    public ImageView pin153;
    public ImageView pin154;
    public ImageView pin161;
    public ImageView pin162;
    public ImageView pin163;
    public ImageView pin164;
    public ImageView pin171;
    public ImageView pin172;
    public ImageView pin173;
    public ImageView pin174;
    public ImageView pin181;
    public ImageView pin182;
    public ImageView pin183;
    public ImageView pin184;
    public ImageView pin191;
    public ImageView pin192;
    public ImageView pin193;
    public ImageView pin194;
    public ImageView pin201;
    public ImageView pin202;
    public ImageView pin203;
    public ImageView pin204;
    public ImageView pin211;
    public ImageView pin212;
    public ImageView pin213;
    public ImageView pin214;
    public ImageView pin221;
    public ImageView pin222;
    public ImageView pin223;
    public ImageView pin224;
    public ImageView pin231;
    public ImageView pin232;
    public ImageView pin233;
    public ImageView pin234;
    public ImageView pin241;
    public ImageView pin242;
    public ImageView pin243;
    public ImageView pin244;
    public ImageView pin251;
    public ImageView pin252;
    public ImageView pin253;
    public ImageView pin254;
    public ImageView pin261;
    public ImageView pin262;
    public ImageView pin263;
    public ImageView pin264;
    public ImageView pin271;
    public ImageView pin272;
    public ImageView pin273;
    public ImageView pin274;
    public ImageView pin281;
    public ImageView pin282;
    public ImageView pin283;
    public ImageView pin284;
    public ImageView pin291;
    public ImageView pin294;
    public ImageView pin293;
    public ImageView pin292;
    public Button showGrid4;
    public Button showGrid3;
    public Button showGrid2;
    public Button showMyGrid;
    public Text newMessage;
    private boolean dragging;
    private boolean[] faceShowed;
    private Map<PlayerColor, String> colorImageMap;
    private HashMap<Rectangle, Coordinate> rectangleCoordinateHashMap;
    @FXML
    public ComboBox<String> chatSelector;
    @FXML
    public TextField chatTextField;
    @FXML
    public Button sendButton;
    @FXML
    private ListView<ChatMessage> currentChat;

    @FXML
    private Button showChat;

    @FXML
    public AnchorPane chatPane;

    /**
     * Initializes the JavaFX application, loads the GameBoard.fxml file, and sets up the main stage.
     *
     * @param stage The primary stage of the JavaFX application.
     * @throws Exception If there is an error during initialization or loading of resources.
     */
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
        stage.centerOnScreen();
        stage.show();

        stage.setOnCloseRequest((event->{
            Platform.exit();
            System.exit(0);
        }));
    }

    /**
     * Sets the GUI view for the application.
     *
     * @param view the {@link GUIView} instance to be set
     */
    public static void setGuiView(GUIView view) {
        guiView = view;
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is called once all FXML elements have been processed and are ready to use.
     * It sets initial values, initializes GUI components, and sets up event handlers.
     *
     * @param url           The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the resource is not found.
     */
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

        initializeButtons();
        dragging = false;

        chatSelector.setItems(guiView.getChatOptions());
        chatSelector.getSelectionModel().select("global");
        chatSelector.setOnAction(this::changeChat);


        currentChat.setCellFactory(param -> new ListCell<ChatMessage>() {
            @Override
            protected void updateItem(ChatMessage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        updateCurrentChat();
    }

    /**
     * Initializes the buttons for displaying player grids in the GUI.
     * Sets up button styles and text based on player colors and nicknames.
     */
    public void initializeButtons(){
        Button[] gridsButtons = {showMyGrid, showGrid2, showGrid3, showGrid4};
        Map<String, PlayerColor> playerColorMap = client.getVirtualView().getPlayerColorMap();

        PlayerColor ownColor = playerColorMap.get(client.getNickname());
        String ownColorStyle = "-fx-background-color: " +colorButtons(ownColor);
        int i=0;
        gridsButtons[i].setStyle(ownColorStyle);
        gridsButtons[i].setText(client.getNickname());
        i++;
        for (String playerName : client.getVirtualView().getListOfPlayers()) {
            if(!playerName.equals(client.getNickname())){
                gridsButtons[i].setText(playerName);
                gridsButtons[i].setVisible(true);

                PlayerColor playerColor = playerColorMap.get(playerName);
                String colorStyle = "-fx-background-color: " + colorButtons(playerColor);
                gridsButtons[i].setStyle(colorStyle);

                i++;
            }

        }
    }

    /**
     * Determines the color code for styling buttons based on the given player color.
     * Returns a hexadecimal color code corresponding to the specified player color.
     *
     * @param playerColor The color of the player for whom the button style is determined.
     * @return A string representing the hexadecimal color code for the button style.
     */
    public String colorButtons(PlayerColor playerColor){
        if(playerColor.equals(PlayerColor.BLUE))
            return "#4290f5";
        if(playerColor.equals(PlayerColor.RED))
            return "#ed0c00";
        if(playerColor.equals(PlayerColor.GREEN))
            return "#6dd160";

        return "#dece3c";

    }

    /**
     * Sets turn's text based on the current player.
     *  @throws RuntimeException if an IOException occurs while sending the message to retrieve available colors
     */
    public void setTurn(){
        if(Objects.equals(client.getNickname(), client.getVirtualView().getCurrentPlayer())){
            turn.setText("It's your turn!");
        }else{
            turn.setText("It's "+client.getVirtualView().getCurrentPlayer()+"'s turn");
        }
    }

    /**
     * Updates the display of the player's hand cards on the GUI.
     * Retrieves the current hand cards from the virtual view and updates the corresponding
     * ImageView components with the front images of the cards.
     */
    public void updateHand(){
        List<ImageView> cardViews = List.of(handCard1, handCard2, handCard3);

        for (int i = 0; i < client.getVirtualView().getHandCards().size() && i < cardViews.size(); i++) {
            String id = String.format("%03d", client.getVirtualView().getHandCards().get(i).getIdCard());
            cardViews.get(i).setImage(getImageFront(id));

            cardViews.get(i).setVisible(true);

            faceShowed[i]=true;
        }

        if(client.getVirtualView().getHandCards().size()==2)
            cardViews.get(2).setVisible(false);
    }

    /**
     * Sets the image of the personal objective card based on the first card in the player's hand objectives.
     * Retrieves the ID of the card, formats it, and sets the corresponding front image to the ImageView component.
     */
    public void setPersonalObjective(){
        String id = String.format("%03d", client.getVirtualView().getHandObjectives().getFirst().getIdCard());
        personalObjective.setImage(getImageFront(id));
    }

    /**
     * Updates the image of the gold deck card based on the first card in the gold deck.
     * Retrieves the ID of the card, formats it, and sets the corresponding back image to the ImageView component.
     */
    public void updateGoldDeck(){
        if(!client.getVirtualView().getResourceDeck().getCardList().isEmpty()){
            String id = String.format("%03d", client.getVirtualView().getGoldDeck().getCardList().getFirst().getIdCard());
            goldDeck.setImage(getImageBack(id));
        }else{
            resourceDeck.setVisible(false);
        }
    }

    /**
     * Updates the image of the resource deck card based on the first card in the resource deck.
     * Retrieves the ID of the card, formats it, and sets the corresponding back image to the ImageView component.
     */
    public void updateResourceDeck(){
        if(!client.getVirtualView().getResourceDeck().getCardList().isEmpty()){
            String id = String.format("%03d", client.getVirtualView().getResourceDeck().getCardList().getFirst().getIdCard());
            resourceDeck.setImage(getImageBack(id));
        }else{
            resourceDeck.setVisible(false);
        }

    }

    /**
     * Updates the displayed resource and gold cards on the board.
     */
    public void updateDisplayedCard(){
        ArrayList<PlayableCard> displayedCards=client.getVirtualView().getDisplayedCards();
        List<ImageView> cardViews = List.of(resourceDisplayed1, resourceDisplayed2, goldDisplayed1, goldDisplayed2);

        for (int i = 0; i < displayedCards.size(); i++) {
            String id = String.format("%03d", displayedCards.get(i).getIdCard());
            cardViews.get(i).setImage(getImageFront(id));
            cardViews.get(i).setVisible(true);
        }

        for (int i = displayedCards.size(); i < cardViews.size(); i++) {
            cardViews.get(i).setVisible(false);
        }
    }

    /**
     * Sets the displayed objective cards images.
     */
    public void setObjectiveDisplayed(){
        List<ObjectiveCard> objectiveCards=client.getVirtualView().getDisplayedObjectiveCards();
        List<ImageView> cardViews = List.of(objectiveDisplayed1,objectiveDisplayed2);

        for (int i = 0; i < objectiveCards.size(); i++) {
            String id = String.format("%03d", objectiveCards.get(i).getIdCard());
            cardViews.get(i).setImage(getImageFront(id));
        }
    }

    /**
     * Retrieves the image of the back side of a card with the specified ID.
     *
     * @param id The ID of the card for which the back image is requested.
     * @return The Image object representing the back side of the card.
     * @throws NullPointerException If the specified resource for the card back image is null.
     */
    public Image getImageBack(String id){
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_back/" + id + ".png")));
    }

    /**
     * Retrieves the image of the front side of a card with the specified ID.
     *
     * @param id The ID of the card for which the front image is requested.
     * @return The Image object representing the front side of the card.
     * @throws NullPointerException If the specified resource for the card front image is null.
     */
    public Image getImageFront(String id){
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(
                "/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_front/" + id + ".png")));
    }

    /**
     * Initializes the score display by associating player colors with corresponding pin images.
     */
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

    /**
     * Sets token images based on player scores, using predefined pinGroups and colorImageMap.
     */
    public void setTokenImage(){
        ImageView[][] pinGroups = {
                {pin1, pin2, pin3, pin4},
                {pin11, pin12, pin13, pin14},
                {pin21, pin22, pin23, pin24},
                {pin31, pin32, pin33, pin34},
                {pin41, pin42, pin43, pin44},
                {pin51, pin52, pin53, pin54},
                {pin61, pin62, pin63, pin64},
                {pin71, pin72, pin73, pin74},
                {pin81, pin82, pin83, pin84},
                {pin91, pin92, pin93, pin94},
                {pin101, pin102, pin103, pin104},
                {pin111, pin112, pin113, pin114},
                {pin121, pin122, pin123, pin124},
                {pin131, pin132, pin133, pin134},
                {pin141, pin142, pin143, pin144},
                {pin151, pin152, pin153, pin154},
                {pin161, pin162, pin163, pin164},
                {pin171, pin172, pin173, pin174},
                {pin181, pin182, pin183, pin184},
                {pin191, pin192, pin193, pin194},
                {pin201, pin202, pin203, pin204},
                {pin211, pin212, pin213, pin214},
                {pin221, pin222, pin223, pin224},
                {pin231, pin232, pin233, pin234},
                {pin241, pin242, pin243, pin244},
                {pin251, pin252, pin253, pin254},
                {pin261, pin262, pin263, pin264},
                {pin271, pin272, pin273, pin274},
                {pin281, pin282, pin283, pin284},
                {pin291, pin292, pin293, pin294},
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

            if(score<=29){
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
    }


    //Gestione input
    /**
     * Handles the drag event when a card image is detected with a primary mouse button press.
     *
     * @param event The MouseEvent representing the drag event.
     */
    @FXML
    private void dragCardDetected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            ImageView cardImage = (ImageView) event.getSource();
            int cardIndex = Integer.parseInt(cardImage.getId().substring(cardImage.getId().length() - 1)) - 1; // Extract the index from the ID
            PlayableCard card = client.getVirtualView().getHandCards().get(cardIndex);

            Dragboard db = cardImage.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            int cardID = card.getIdCard();
            String data = cardID + ";" + faceShowed[cardIndex];
            content.putString(data);
            db.setContent(content);
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setTransform(new Scale(0.7, 0.7));
            WritableImage snapshot = cardImage.snapshot(parameters, null);
            db.setDragView(snapshot);
            event.consume();
        }

    }

    /**
     * Show the other face of the card
     *
     * @param event The MouseEvent representing the click event.
     */
    @FXML
    private void handleHandCardClick(MouseEvent event) {
        ImageView clickedImageView = (ImageView) event.getSource();
        int cardIndex = Integer.parseInt(clickedImageView.getUserData().toString());

        if (event.getButton() == MouseButton.SECONDARY && !dragging) {
            PlayableCard card = client.getVirtualView().getHandCards().get(cardIndex);
            String id = String.format("%03d", card.getIdCard());

            if (faceShowed[cardIndex]) {
                clickedImageView.setImage(getImageBack(id));
                faceShowed[cardIndex] = false;
            } else {
                clickedImageView.setImage(getImageFront(id));
                faceShowed[cardIndex] = true;
            }
        }
    }

    /**
     * Handles the event of picking a card from the displayed cards by clicking on its ImageView.
     * Constructs and sends a PickMessage to the server with information about the picked card.
     *
     * @param event The MouseEvent representing the click event on the ImageView.
     * @throws IOException If there is an error sending the PickMessage.
     */
    @FXML
    public void pickCard(MouseEvent event) throws IOException {
        ImageView clickedImage = (ImageView) event.getSource();
        int index = Integer.parseInt(clickedImage.getUserData().toString());

        if (index >= 0 && index < client.getVirtualView().getDisplayedCards().size()) {
            int cardID = client.getVirtualView().getDisplayedCards().get(index).getIdCard();
            PickMessage pickMessage=new PickMessage(client.getClientID(), cardID, client.getNickname());
            client.sendMessage(pickMessage);
        }
    }

    /**
     * Handles the event of drawing a resource card by calling the drawCard method with the deckWanted parameter set to true.
     *
     * @param mouseEvent The MouseEvent representing the mouse click event triggering the draw action.
     * @throws IOException If there is an error sending the DrawMessage.
     */
    @FXML
    public void drawResource(MouseEvent mouseEvent) throws IOException {
        drawCard(true);
    }

    /**
     * Handles the event of drawing a gold card by calling the drawCard method with the deckWanted parameter set to false.
     *
     * @param mouseEvent The MouseEvent representing the mouse click event triggering the draw action.
     * @throws IOException If there is an error sending the DrawMessage.
     */
    @FXML
    public void drawGold(MouseEvent mouseEvent) throws IOException {
        drawCard(false);
    }

    /**
     * Constructs and sends a DrawMessage to the server based on the specified deckWanted parameter.
     *
     * @param deckWanted A boolean indicating whether the resource deck (true) or gold deck (false) is to be drawn from.
     * @throws IOException If there is an error sending the DrawMessage.
     */
    public void drawCard(Boolean deckWanted) throws IOException {
        DrawMessage messageToSend = new DrawMessage(client.getClientID(), client.getNickname(), deckWanted);
        client.sendMessage(messageToSend);
    }

    /**
     * Updates the game board with rectangles representing available positions for card placement.
     * Rectangles are centered on the pane and styled. Handles drag-and-drop events for card placement.
     * Clears and updates rectangleCoordinateHashMap with current coordinates.
     *
     */
    public void updatePane(){
        double centerX=pane.getPrefWidth()/2;
        double centerY=pane.getPrefHeight()/2;

        showGrid(client.getNickname());
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

    /**
     * Displays the grid for the second player.
     */
    @FXML
    public void secondPlayerGrid(){
        showGrid(showGrid2.getText());
    }

    /**
     * Displays the grid for the third player.
     */
    @FXML
    public void thirdPlayerGrid(){
        showGrid(showGrid3.getText());
    }

    /**
     * Displays the grid for the fourth player.
     */
    @FXML
    public void fourthPlayerGrid(){
        showGrid(showGrid4.getText());
    }


    /**
     * Displays the grid for a specified player.
     * The grid consists of images representing cards placed at specific coordinates.
     * Adjusts the scroll pane to focus on the last card placed.
     *
     * @param nickname The nickname of the player whose grid is to be displayed.
     */
    public void showGrid(String nickname){
        double centerX=pane.getPrefWidth()/2;
        double centerY=pane.getPrefHeight()/2;

        double maxX = 0.0;
        double maxY = 0.0;

        double lastCardPosX = 0.0;
        double lastCardPosY = 0.0;

        Map<Coordinate, Face> grid = client.getVirtualView().getGrids().get(nickname);
        Iterator<Map.Entry<Coordinate, Face>> iterator = grid.entrySet().iterator();

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
                imageView.toFront();

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



    }


    /**
     * Updates the current chat display when triggered by an action event.
     *
     * @param event the ActionEvent triggered by the user's interaction
     */
    @FXML
    private void changeChat(ActionEvent event){
        updateCurrentChat();
    }

    /**
     * Updates the current chat display based on the selected chat from the chat selector.
     */
    private void updateCurrentChat() {
        String selectedChat = chatSelector.getSelectionModel().getSelectedItem();
        if (selectedChat != null && guiView.getChats().containsKey(selectedChat)) {
            currentChat.setItems(guiView.getChats().get(selectedChat));
        }
    }

    /**
     * Adds a message to the appropriate chat and updates the current chat display if necessary.
     *
     * @param message the {@link ChatMessage} to be added to the chat
     */
    public void addMessageToChat(ChatMessage message){
        String sender = message.getSender();
        String key;

        if (sender.equals(client.getNickname()) || message.getRecipient().equals("global")){
            key = message.getRecipient();
        } else {
            key = message.getSender();
        }

        if (guiView.getChats().containsKey(key)){
            guiView.getChats().get(key).add(message);
            if (chatSelector.getSelectionModel().getSelectedItem().equals(key)){
                updateCurrentChat();
            }
        }
    }

    /**
     * Sends a chat message based on the user's input and updates the chat.
     *
     * @param event the {@link ActionEvent} triggered by the user action
     * @throws IOException if an I/O error occurs while sending the message
     */
    @FXML
    public void sendMessage(ActionEvent event) throws IOException {
        String messageText = chatTextField.getText();
        if (messageText != null && !messageText.trim().isEmpty()){
            String selectedChat = chatSelector.getSelectionModel().getSelectedItem();
            String author = client.getNickname();
            ChatMessage message = new ChatMessage(author, selectedChat, messageText);
            if (message.getRecipient() != "global"){
                addMessageToChat(message);
            }
            client.sendMessage(new sendChatMessage(client.getClientID(),author, selectedChat, messageText));
            chatTextField.clear();
        }
    }

    /**
     * Toggles the visibility of the chat pane and updates the button text accordingly.
     *
     * @param actionEvent the {@link ActionEvent} triggered by the user action
     */
    @FXML
    public void toggleChatMenu(ActionEvent actionEvent) {
        if (chatPane.isVisible()){
            chatPane.setVisible(false);
            showChat.setText("Show Chat");
        } else {
            chatPane.setVisible(true);
            showChat.setText("Hide chat");
            newMessage.setVisible(false);
        }
    }


}
