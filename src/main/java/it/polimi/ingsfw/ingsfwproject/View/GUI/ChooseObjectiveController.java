package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.ChatMessage;
import it.polimi.ingsfw.ingsfwproject.Model.ObjectiveCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.ObjectiveCardChosenMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.sendChatMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsfw.ingsfwproject.View.View.client;

/**
 * The controller of the scene where the objective card is chosen
 */
public class ChooseObjectiveController extends GUIController implements Initializable {
    @FXML public Text stateLabel;
    @FXML public Label turn;
    @FXML public ImageView firstCard;
    @FXML public ImageView secondCard;
    @FXML public Label labelInstruction;
    @FXML
    public ComboBox<String> chatSelector;
    @FXML
    public TextField chatTextField;
    @FXML
    public Button sendButton;
    public Text newMessage;
    @FXML
    private ListView<ChatMessage> currentChat;
    @FXML
    private Button showChat;
    @FXML
    public AnchorPane chatPane;
    public static GUIView guiView;

    /**
     * Initializes and displays the stage for choosing the objective card.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML file or setting the scene
     */
    @Override
    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/ChooseObjective.fxml"));
        Parent root = loader.load();
        ChooseObjectiveController chooseObjectiveController = loader.getController();
        guiView.setChooseObjectiveController(chooseObjectiveController);
        stage.centerOnScreen();
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Choosing Objective Card");
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
     * Initializes the controller upon loading.
     *
     * <p>Sets up UI components based on the current player, configures chat options,
     * customizes chat message appearance, and updates the current chat display.
     *
     * @param url location used to resolve relative paths for the root object, or null if unknown
     * @param resourceBundle resources for the root object, or null if there are no resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        setTurn();
    }

    /**
     * Sets turn's text based on the current player.
     *  @throws RuntimeException if an IOException occurs while sending the message to retrieve available colors
     */
    public void setTurn(){
        if(Objects.equals(client.getNickname(), client.getVirtualView().getCurrentPlayer())){
            turn.setText("It's your turn!");
            firstCard.setVisible(true);
            secondCard.setVisible(true);
        }else{
            turn.setText("It's "+client.getVirtualView().getCurrentPlayer()+"'s turn");
        }
    }

    /**
     * Displays the objective cards using images retrieved from resources.
     *
     * <p>This method takes an ArrayList of {@link ObjectiveCard} objects and displays
     * the images of the first and last cards in the list. It formats the card IDs and
     * retrieves corresponding images from the application's resources directory.
     *
     * @param cards an ArrayList of {@link ObjectiveCard} objects to display
     */
    public void showObjective(ArrayList<ObjectiveCard> cards){
        int card1 = cards.getFirst().getIdCard();
        String id1 = String.format("%03d", card1);
        String id2=String.format("%03d",cards.getLast().getIdCard());
        Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_front/"+id1+".png")));
        Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_front/"+id2+".png")));
        firstCard.setImage(image1);
        secondCard.setImage(image2);


    }

    /**
     * Sends a message with the chosen objective card ID and updates the UI.
     *
     * @param cardID the ID of the objective card chosen
     * @throws IOException if an error occurs while sending the message
     */
    public void cardChosen(int cardID) throws IOException {
        ObjectiveCardChosenMessage objectiveCardChosenMessage=new ObjectiveCardChosenMessage(client.getClientID(), client.getNickname(), cardID);
        client.sendMessage(objectiveCardChosenMessage);
        firstCard.setVisible(false);
        secondCard.setVisible(false);
        labelInstruction.setText("Objective Card Chosen.\nWaiting for other players choice");
    }

    /**
     * Handles the selection of the first objective card by sending its ID.
     *
     * <p>This method retrieves the ID of the first objective card from the client's virtual view,
     * then calls {@link #cardChosen(int)} to send a message indicating the chosen card.
     *
     * @param mouseEvent the MouseEvent triggered by the user's interaction
     * @throws IOException if an error occurs while sending the message
     */
    @FXML
    public void firstCardChosen(MouseEvent mouseEvent) throws IOException {
        int cardID=client.getVirtualView().getHandObjectives().getFirst().getIdCard();
        cardChosen(cardID);
    }

    /**
     * Handles the selection of the second objective card by sending its ID.
     *
     * <p>This method retrieves the ID of the second objective card from the client's virtual view,
     * then calls {@link #cardChosen(int)} to send a message indicating the chosen card.
     *
     * @param mouseEvent the MouseEvent triggered by the user's interaction
     * @throws IOException if an error occurs while sending the message
     */
    @FXML
    public void secondCardChosen(MouseEvent mouseEvent) throws IOException {
        int cardID=client.getVirtualView().getHandObjectives().getLast().getIdCard();
        cardChosen(cardID);
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
            if (!Objects.equals(message.getRecipient(), "global")){
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
