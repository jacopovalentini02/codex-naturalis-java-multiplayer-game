package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetColorAvailableMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.JoinGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.GameJoinedMessage;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static it.polimi.ingsfw.ingsfwproject.View.View.client;

public class LobbyGUIController extends GUIController implements Initializable {

    @FXML
    public Button createButtonLobby;
    @FXML
    public TableView<Map.Entry<Integer, Integer>> tableView;
    @FXML
    public TableColumn<Map.Entry<Integer, Integer>, Integer> gameIdColumn;
    @FXML
    public TableColumn<Map.Entry<Integer, Integer>, Integer> playerCountColumn;
    @FXML public TableColumn joinColumn;
    @FXML public TextField nickanme;
    @FXML private Button refreshButton;



    public static GUIView guiView;

    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/Lobby.fxml"));
        Parent root = loader.load();
        LobbyGUIController lobbyGUIController = loader.getController();
        guiView.setLobbyGUIController(lobbyGUIController);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Lobby");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void openCreateGame(){
        System.out.println(guiView);
       guiView.openCreateGameWindow();
    }


    public static void setGuiView(GUIView view) {
        guiView = view;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getKey()));
        playerCountColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getValue()));



        refreshGames();
    }

    public void showGames(HashMap<Integer, Integer> gameList){
        tableView.getItems().clear();

        for (Map.Entry<Integer, Integer> entry : gameList.entrySet()) {
            tableView.getItems().add(entry);
        }
        gameIdColumn.setCellFactory(tc -> {
            TableCell<Map.Entry<Integer, Integer>, Integer> cell = new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.valueOf(item));
                    }
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    // Ottieni l'ID del gioco associato a questa riga
                    Map.Entry<Integer, Integer> entry = cell.getTableView().getItems().get(cell.getIndex());
                    Integer gameId = entry.getKey();
                    // Stampa l'ID del gioco
                    System.out.println("Game ID: " + gameId);
                    try {
                        sendJoinRequest(gameId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            return cell;
        });
    }

    public void refreshGames(){
        try {
            client.sendMessage(new GetGameListMessage(client.getClientID()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendJoinRequest(int gameId) throws IOException {
        JoinGameMessage joinGameMessage=new JoinGameMessage(client.getClientID(),nickanme.getText(),gameId);
        client.sendMessage(joinGameMessage);
    }


}
