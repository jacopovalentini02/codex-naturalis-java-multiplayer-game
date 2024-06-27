package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.ChatMessage;
import it.polimi.ingsfw.ingsfwproject.Model.PlayerColor;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetColorAvailableMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.WantThatColorMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.sendChatMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsfw.ingsfwproject.View.View.client;

/**
 * The controller of the scene where you have to choose the color
 */
public class ChooseColorController extends GUIController implements Initializable {
    public static GUIView guiView;
    @FXML public Text stateLabel;
    @FXML public Label turn;
    @FXML public Label labelInstruction;
    @FXML public Rectangle blueRectangle;
    @FXML public Rectangle greenRectangle;
    @FXML public Rectangle redRectangle;
    @FXML public Rectangle yellowRectangle;

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

    public Text newMessage;

    /**
     * Initializes and displays the primary stage for the color selection UI.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML file or setting the scene
     */
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
        stage.centerOnScreen();
        stage.show();
        stage.setOnCloseRequest((event->{
            Platform.exit();
            System.exit(0);
        }));
    }

    /**
     * Sets the GUI view instance for the application.

     * @param view the GUI view instance to set
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
        setTurn();

        chatSelector.setItems(guiView.getChatOptions());
        chatSelector.getSelectionModel().select("global");
        chatSelector.setOnAction(this::changeChat);
        currentChat.setCellFactory(param -> new ListCell<>() {
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
     * Sets turn's text based on the current player.
     *  @throws RuntimeException if an IOException occurs while sending the message to retrieve available colors
     */
    public void setTurn(){
        if(Objects.equals(client.getNickname(), client.getVirtualView().getCurrentPlayer())){
            turn.setText("It's your turn!");
            try {
                client.sendMessage(new GetColorAvailableMessage(client.getClientID()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else{
            turn.setText("It's "+client.getVirtualView().getCurrentPlayer()+"'s turn");
        }
    }
    /**
     * Set rectangle's visibility to true if the corresponding color is present in the available colors
     * @param colors a list of color the player can choose
     * */
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

    /**
     * It passes color BLUE as a parameter to the sendColor method.
     *
     * @throws IOException if an I/O error occurs while sending the selected color
     */
    public void blueChosen() throws IOException {
        PlayerColor colorChosen=PlayerColor.BLUE;
        sendColor(colorChosen);
    }
    /**
     * It passes color YELLOW as a parameter to the sendColor method.
     *
     * @throws IOException if an I/O error occurs while sending the selected color
     */
    public void yellowChosen() throws IOException {
        PlayerColor colorChosen=PlayerColor.YELLOW;
        sendColor(colorChosen);
    }
    /**
     * It passes color RED as a parameter to the sendColor method.
     *
     * @throws IOException if an I/O error occurs while sending the selected color
     */
    public void redChosen() throws IOException {
        PlayerColor colorChosen=PlayerColor.RED;
        sendColor(colorChosen);
    }
    /**
     * It passes color GREEN as a parameter to the sendColor method.
     *
     * @throws IOException if an I/O error occurs while sending the selected color
     */
    public void greenChosen() throws IOException {
        PlayerColor colorChosen=PlayerColor.GREEN;
        sendColor(colorChosen);
    }

    /**
     * Sends a message with the chosen player color to the server and updates the UI accordingly.
     *
     * @param colorChosen the color chosen by the player
     * @throws IOException if an I/O error occurs while sending the message
     */
    public void sendColor(PlayerColor colorChosen) throws IOException {
        WantThatColorMessage wantThatColorMessage=new WantThatColorMessage(client.getClientID(), client.getNickname(),colorChosen);
        client.sendMessage(wantThatColorMessage);

        blueRectangle.setVisible(false);
        greenRectangle.setVisible(false);
        redRectangle.setVisible(false);
        yellowRectangle.setVisible(false);
        labelInstruction.setText("Color Chosen.\nWaiting for other players choice");
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
