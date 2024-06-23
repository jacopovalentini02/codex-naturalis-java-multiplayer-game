package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.JoinGameMessage;
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

/**
 * The controller of the scene where you have to choose between creating a new game or joining an old one
 */
public class LobbyGUIController extends GUIController implements Initializable {

    @FXML
    public Button createButtonLobby;
    @FXML
    public TableView<Map.Entry<Integer, Integer>> tableView;
    @FXML
    public TableColumn<Map.Entry<Integer, Integer>, Integer> gameIdColumn;
    @FXML
    public TableColumn<Map.Entry<Integer, Integer>, Integer> playerCountColumn;
    @FXML public TextField nickanme;
    @FXML private Button refreshButton;



    public static GUIView guiView;

    public void start(Stage stage) throws Exception {
        setGuiView(guiView);
        stage.centerOnScreen();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/Lobby.fxml"));
        Parent root = loader.load();
        LobbyGUIController lobbyGUIController = loader.getController();
        guiView.setLobbyGUIController(lobbyGUIController);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Lobby");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Opens the window to create a new game.
     */
    @FXML
    public void openCreateGame(){
       guiView.openCreateGameWindow();
    }

    /**
     * Sets the GUI view instance for the application.

     * @param view the GUI view instance to set
     */
    public static void setGuiView(GUIView view) {
        guiView = view;
    }

    /**
     * Initializes the table columns and refreshes the list of games.
     *
     * @param url the location used to resolve relative paths for the root object, or null if not known
     * @param resourceBundle the resources for the root object, or null if there are no resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameIdColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getKey()));
        playerCountColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getValue()));
        refreshGames();
    }

    /**
     * Displays the list of games in the TableView.
     *
     * Clears the existing items in the TableView and populates it with the games
     * provided in the given HashMap. Each game entry is represented by its ID in the table.
     * Allows the user to click on a game ID to send a join request, provided a valid nickname
     * is entered in the nickname field. If the nickname is not valid, it notifies the user.
     *
     *
     * @param gameList a HashMap containing game IDs mapped to the number of players in each game
     */
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
                if(nickanme.getText()!=null && nickanme.getText().length()>=2){
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
                }else{
                    guiView.notifyException("You need to enter a valid nickname!");
                }

            });

            return cell;
        });
    }

/**
 * Refreshes the list of games by requesting an updated game list from the server.
 */
    public void refreshGames(){
        try {
            client.sendMessage(new GetGameListMessage(client.getClientID()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a join request to the server for a specific game.
     * Constructs a JoinGameMessage containing the client's ID, nickname, and the ID of the game
     * to join, then sends this message to the server using the client's connection.
     *
     * @param gameId the ID of the game to join
     * @throws IOException if an I/O error occurs while sending the join request message
     */
    public void sendJoinRequest(int gameId) throws IOException {
        JoinGameMessage joinGameMessage=new JoinGameMessage(client.getClientID(),nickanme.getText(),gameId);
        client.sendMessage(joinGameMessage);
    }


}
