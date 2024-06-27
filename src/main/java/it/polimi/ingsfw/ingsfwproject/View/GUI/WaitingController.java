package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.ChatMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.sendChatMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;


import static it.polimi.ingsfw.ingsfwproject.View.View.client;

/**
 * The controller of the scene where players wait for the game to start
 */
public class WaitingController extends GUIController{

    @FXML
    public Text stateLabel;
    @FXML
    public Label playerNickname;
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

    @FXML
    private TextArea newPlayerJoined;


    public static GUIView guiView;

    /**
     * Initializes and displays the "Waiting" screen.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during initialization or scene setting
     */
    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/Waiting.fxml"));
        Parent root = loader.load();
        WaitingController waitingController = loader.getController();
        guiView.setSetUpGameController(waitingController);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Waiting");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setOnCloseRequest((event->{
            Platform.exit();
            System.exit(0);
        }));
        stage.show();
    }

    @FXML
    private void initialize(){
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
     * Sets the GUI view for the application.
     *
     * @param view the {@link GUIView} instance to be set
     */
    public static void setGuiView(GUIView view) {
        guiView = view;
    }

    /**
     * Returns the TextArea used for displaying new player join messages.
     *
     * @return the TextArea for new player join messages
     */
    public TextArea getNewPlayerJoined() {
        return newPlayerJoined;
    }

    /**
     * Appends a new player's nickname to the TextArea for displaying join messages.
     *
     * @param nickname the nickname of the player who joined the game
     */
    public void addNickname(String nickname){
        newPlayerJoined.appendText(nickname + " has joined the game\n");
    }

    /**
     * Sets the player's nickname in a text field.
     *
     * @param playerNickname the nickname of the player to display
     */
    public void setPlayerNickname(String playerNickname) {
        this.playerNickname.setText("Welcome "+playerNickname+"!");
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


