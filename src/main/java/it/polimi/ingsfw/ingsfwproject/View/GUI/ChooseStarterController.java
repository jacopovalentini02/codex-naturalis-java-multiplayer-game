package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.ChatMessage;
import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Model.PlayableCard;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.PlayCardMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.sendChatMessage;
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
    private AnchorPane chatPane;

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
        //stage.setMaximized(true);
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

    public void showStarter(PlayableCard card){
        String id="0"+card.getIdCard();
        String pathFront=card.getFront().getImagePath();
        String pathBack=card.getBack().getImagePath();

        System.out.println(pathBack);
        Image imageBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathBack)));
        Image imageFront = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pathFront)));
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


    @FXML
    private void changeChat(ActionEvent event){
        updateCurrentChat();
    }

    private void updateCurrentChat() {
        String selectedChat = chatSelector.getSelectionModel().getSelectedItem();
        if (selectedChat != null && guiView.getChats().containsKey(selectedChat)) {
            currentChat.setItems(guiView.getChats().get(selectedChat));
        }
    }

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

    @FXML
    public void toggleChatMenu(ActionEvent actionEvent) {
        if (chatPane.isVisible()){
            chatPane.setVisible(false);
            showChat.setText("Show Chat");
        } else {
            chatPane.setVisible(true);
            showChat.setText("Hide chat");
        }
    }

}
