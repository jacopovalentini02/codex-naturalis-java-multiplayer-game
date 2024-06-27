package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;

import it.polimi.ingsfw.ingsfwproject.View.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.*;

/**
 * GUI representation of the client-side view.
 */
public class GUIView extends View {
    private Stage stage;
    private WaitingController waitingController;
    private ChooseObjectiveController chooseObjectiveController;
    private ChooseStarterController chooseStarterController;
    private LobbyGUIController lobbyGUIController;
    private ChooseColorController chooseColorController;
    private GameBoardController gameBoardController;
    private GUIController currentController;
    private ObservableList<String> chatOptions;
    private Map<String, ObservableList<ChatMessage>> chats;

    /**
     * Initializes GUIView with default chat options and a global chat.
     */
    public GUIView(){
        chooseConnection();
        chatOptions = FXCollections.observableArrayList();
        chatOptions.add("global");
        chats = new HashMap<>();
        chats.put("global", FXCollections.observableArrayList());

    }

    /**
     * Retrieves the list of available chat options.
     * @return ObservableList of chat options.
     */
    public ObservableList<String> getChatOptions(){
        return this.chatOptions;
    }

    /**
     * Retrieves the map of chat messages
     * @return Map of chat messages.
     */
    public Map<String, ObservableList<ChatMessage>> getChats(){
        return this.chats;
    }

    /**
     * Opens the lobby GUI.
     */
    public void openLobby() {
        Platform.runLater(() -> {
            try {
                lobbyGUIController =new LobbyGUIController();
                currentController=lobbyGUIController;
                LobbyGUIController.setGuiView(this);
                lobbyGUIController.start(this.stage);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Opens the window for creating a new game.
     */
    public void openCreateGameWindow(){
        Platform.runLater(() -> {
            try {
                CreateGameController createGameController = new CreateGameController();
                createGameController.setGuiView(this);
                createGameController.start(this.stage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Opens the waiting screen.
     */
    public void openWaiting(){
        waitingController =new WaitingController();

        Platform.runLater(() -> {
            try {
                WaitingController.setGuiView(this);
                waitingController.start(this.stage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Displays a connection error alert.
     */
    public void showConnectionError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText(null);
            alert.setContentText("Connection could not be established. Please retry.");
            alert.showAndWait();
        });

    }

    /**
     * Initiates the connection process by setting up the GUI view for connection and launching the ChooseConnectionController.
     * This method starts a new thread to handle the application launch for the connection controller.
     */
    @Override
    public void chooseConnection() {
        ChooseConnectionController.setGuiView(this);
        new Thread(() -> Application.launch(ChooseConnectionController.class)).start();
    }

    /**
     * Notifies the user of a game-related exception by displaying an error alert.
     *
     * @param message The error message to display in the alert.
     */
    @Override
    public void notifyException(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Game Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Displays the first message upon successful connection, showing an information alert.
     *
     * @param clientID The ID of the client for which the connection was established.
     */
    @Override
    public void displayFirstMessage(int clientID) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("Connection established");
            alert.showAndWait();
        });
    }

    /**
     * Displays the list of available games in the lobby GUI controller.
     *
     * @param gameList A HashMap containing the list of game IDs and their respective player counts.
     */
    @Override
    public void displayGameList(HashMap<Integer, Integer> gameList) {
        Platform.runLater(() -> {
            lobbyGUIController.showGames(gameList);
        });
    }

    /**
     * Notifies the GUI that the client has joined a game.
     * Opens the waiting screen and sets the player nickname in the waiting controller if available.
     *
     * @param idGame The ID of the game that the client has joined.
     */
    @Override
    public void notifyGameJoined(int idGame) {
        openWaiting();
        Platform.runLater(() -> {
            if (waitingController != null && waitingController.getNewPlayerJoined() != null) {
                waitingController.setPlayerNickname(client.getNickname());
            }
        });
    }

    /**
     * Notifies the GUI that new players have joined the game.
     * Updates the waiting screen with the newly joined players' nicknames and adds them to the chat list.
     *
     * @param nicknames The list of nicknames of the new players who have joined.
     */
    @Override
    public void notifyNewPlayerJoined(ArrayList<String> nicknames) {
        Platform.runLater(() -> {
            if (waitingController != null && waitingController.getNewPlayerJoined() != null) {
                String lastNickname = nicknames.getLast();
                waitingController.addNickname(lastNickname);
                for (String s: nicknames)
                    addChat(s);
            }
        });


    }

    /**
     * Adds a new chat tab for the specified nickname if it doesn't already exist.
     *
     * @param nickname The nickname of the player to add a new chat tab for.
     */
    public void addChat(String nickname){
        if (!Objects.equals(nickname, client.getNickname()) && !(chats.containsKey(nickname))){
            chatOptions.add(nickname);
            chats.put(nickname, FXCollections.observableArrayList());
        }
    }

    /**
     * Notifies the GUI that the starter card has been chosen.
     */
    @Override
    public void notifyStarterCard() {
        Platform.runLater(() -> {
            chooseStarterController.cardChosen();
        });
    }

    /**
     * Notifies the GUI that the available player colors have been updated.
     *
     * @param colors The list of available player colors.
     */
    @Override
    public void notifyColorsAvailable(List<PlayerColor> colors) {
        Platform.runLater(() -> {
            chooseColorController.setColors(colors);
        });
    }

    /**
     * Notifies the GUI that the gold deck has been updated.
     */
    @Override
    public void notifyGoldDeckUpdate() {
        if(currentController.equals(gameBoardController)){
            Platform.runLater(() -> {
                gameBoardController.updateGoldDeck();
            });
        }
    }

    /**
     * Notifies the GUI that the resource deck has been updated.
     */
    @Override
    public void notifyResourceDeckUpdate() {
        if(currentController.equals(gameBoardController)){
            Platform.runLater(() -> {
                gameBoardController.updateResourceDeck();
            });
        }
    }

    /**
     * Notifies the GUI that the displayed cards have been updated.
     *
     * @param displayedCards The list of playable cards to be displayed.
     */
    @Override
    public void notifyDisplayedCardsUpdate(ArrayList<PlayableCard> displayedCards) {
        if(currentController.equals(gameBoardController)){
            Platform.runLater(() -> {
                gameBoardController.updateDisplayedCard();
            });
        }

    }

    /**
     * Notifies the GUI of the current player's turn.
     * It checks which controller is currently active and sets the turn accordingly.
     *
     * @param nickname The nickname of the current player.
     */
    @Override
    public void notifyCurrentPlayer(String nickname) {
        if(currentController.equals(chooseStarterController)){
            Platform.runLater(() -> {
                chooseStarterController.setTurn();
            });
        }else if(currentController.equals(chooseColorController)){
            Platform.runLater(() -> {
                chooseColorController.setTurn();
            });
        }else if(currentController.equals(chooseObjectiveController)){
            Platform.runLater(() -> {
                chooseObjectiveController.setTurn();
            });
        }else if(currentController.equals(gameBoardController)){
            Platform.runLater(() -> {
                gameBoardController.setTurn();
            });
        }

    }

    /**
     * Notifies the GUI that the available positions have been changed.
     * @param coord the List of available position.
     */
    @Override
    public void notifyAvailablePositions(ArrayList<Coordinate> coord) {

    }

    /**
     * Notifies the GUI about updates to the hand of objective cards.
     *
     * @param cards The list of objective cards to be displayed in the GUI.
     */
    @Override
    public void notifyHandObjectives(ArrayList<ObjectiveCard> cards) {
        Platform.runLater(() -> {
            if(currentController.equals(chooseObjectiveController)){
                chooseObjectiveController.showObjective(cards);
            }

        });
    }

    /**
     * Notifies the GUI about changes in the game state and updates the corresponding views accordingly.
     * *
     * @param state The current state of the game.
     */
    @Override
    public void notifyGameState(GameState state) {
        chooseStarterController=new ChooseStarterController();
        chooseColorController=new ChooseColorController();
        chooseObjectiveController=new ChooseObjectiveController();

        if(state==GameState.WAITING_FOR_PLAYERS){
            openWaiting();
        }else if(state==GameState.CHOOSING_STARTER_CARDS){
            gameBoardController=new GameBoardController();
            Platform.runLater(() -> {
                try {
                    ChooseStarterController.setGuiView(this);
                    chooseStarterController.start(this.stage);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }else if(state==GameState.CHOOSING_COLORS){

            Platform.runLater(() -> {
                try {
                    ChooseColorController.setGuiView(this);
                    chooseColorController.start(this.stage);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }else if(state==GameState.CHOOSING_OBJECTIVES){

            Platform.runLater(() -> {
                try {
                    ChooseObjectiveController.setGuiView(this);
                    chooseObjectiveController.start(this.stage);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }else if(state==GameState.STARTED){
            Platform.runLater(() -> {
                try {
                    GameBoardController.setGuiView(this);
                    gameBoardController.start(this.stage);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        } else if (state==GameState.ENDING) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Ending");
                alert.setHeaderText(null);
                alert.setContentText("Last turn for each player!");
                alert.showAndWait();
            });
        }else if(state==GameState.ENDED){
            Platform.runLater(()->{
                try {
                    openLobby();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });


        }

    }

    /**
     * Notifies the GUI about updates to the grid for a specific player.
     *
     * @param nickname The nickname of the player whose grid is being updated.
     * @param grid     The updated grid
     */
    @Override
    public void notifyGridUpdate(String nickname, Map<Coordinate, Face> grid) {
        if(gameBoardController.equals(currentController)){
            Platform.runLater(() -> {
                try {
                    gameBoardController. updatePane();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

    }

    /**
     * Notified the GUI that the resource count has been changed.
     * @param nickname the nickname of the player whose resources have been changed
     * @param resources the new resources count
     */
    @Override
    public void notifyResourcesUpdate(String nickname, HashMap<Content, Integer> resources) {

    }

    /**
     * Notifies the GUI about the winner of the game and displays final scores in an alert dialog.
     *
     * @param nickname The nickname of the player who has won the game.
     */
    @Override
    public void notifyWinnerUpdate(String nickname) {
        System.out.println("Winner: "+nickname);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Messaggio");
            alert.setHeaderText(null);
            StringBuilder message = new StringBuilder();

            if (nickname.equals("tie")){
                message.append("It's a tie! \n");
            } else {
                message.append(nickname).append(" has won! \n");
            }

            message.append("Final scores: \n");

            for (Map.Entry<String, Integer> entry: client.getVirtualView().getScores().entrySet())
                message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");

            Label label = new Label(message.toString());
            label.setWrapText(true);

            alert.getDialogPane().setContent(label);
            alert.showAndWait();
        });


    }

    /**
     * Notifies the GUI about updates to the hand cards of the current player.
     *
     * @param cards The list of playable cards in the current player's hand.
     */
    @Override
    public void notifyHandCardsUpdate(ArrayList<PlayableCard> cards) {
        if(client.getVirtualView().getState()==GameState.CHOOSING_STARTER_CARDS){
            Platform.runLater(() -> {
                if(!cards.isEmpty())
                    chooseStarterController.showStarter(cards.get(0));
            });
        }else if(client.getVirtualView().getState()==GameState.STARTED || client.getVirtualView().getState()==GameState.ENDING){
            Platform.runLater(() -> {
                System.out.println(cards.getFirst().getIdCard() + cards.get(1).getIdCard() + cards.getLast().getIdCard());
                gameBoardController.updateHand();
            });
        }

    }

    /**
     * Notifies the GUI about updates to the common objective cards.
     * @param cards the List of new common objective cards
     */
    @Override
    public void notifyDisplayedObjectives(List<ObjectiveCard> cards) {

    }

    /**
     * Notifies the GUI about updates to the scores of players in the game.
     *
     * @param scores A map containing player nicknames as keys and their corresponding scores as values.
     */
    @Override
    public void notifyScores(Map<String, Integer> scores) {
         if(gameBoardController.equals(currentController)){
            Platform.runLater(() -> {
                try {
                    gameBoardController.setTokenImage();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

    }
    /**
     * Notifies the GUI that a player chose a color
     * @param color the color chosen
     */
    @Override
    public void notifyColorChosen(PlayerColor color) {

    }

    /**
     * Notifies the GUI about a new chat message received.
     *
     * @param message The chat message to be displayed.
     */
    @Override
    public void notifyChatMessage(ChatMessage message) {

        if(currentController.equals(chooseStarterController)){
            Platform.runLater(() -> {
                if(!chooseStarterController.chatPane.isVisible()){
                    chooseStarterController.newMessage.setVisible(true);
                }
                chooseStarterController.addMessageToChat(message);
            });
        }else if(currentController.equals(chooseColorController)){
            Platform.runLater(() -> {
                if(!chooseColorController.chatPane.isVisible()){
                    chooseColorController.newMessage.setVisible(true);
                }
                chooseColorController.addMessageToChat(message);
            });
        }else if(currentController.equals(chooseObjectiveController)){
            Platform.runLater(() -> {
                if(!chooseObjectiveController.chatPane.isVisible()){
                    chooseObjectiveController.newMessage.setVisible(true);
                }
                chooseObjectiveController.addMessageToChat(message);
            });
        }else if(currentController.equals(gameBoardController)){
            Platform.runLater(() -> {
                if(!gameBoardController.chatPane.isVisible()){
                    gameBoardController.newMessage.setVisible(true);
                }
                gameBoardController.addMessageToChat(message);
            });
        }else{
            Platform.runLater(() -> {
                if(!waitingController.chatPane.isVisible()){
                    waitingController.newMessage.setVisible(true);
                }

                waitingController.addMessageToChat(message);
            });
        }
    }

    /**
     * Notifies the CLI when the player has played a card
     * @param bool {@code boolean} {@code true} if the player has played, {@code false} otherwise
     */
    @Override
    public void notifyCurrentPlayerHasPlayed(boolean bool){
    }

    /**
     * Sets the client instance.
     * @param c Client instance to set.
     */
    public void setClient(Client c){
        super.client = c;
    }

    /**
     * Sets the stage for the GUI.
     * @param stage Stage to set.
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }

    /**
     * Retrieves the current stage of the GUI.
     * @return Current stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the controller for setting up the game and assigns it as the current controller.
     * @param waitingController The WaitingController instance to set.
     */
    public void setSetUpGameController(WaitingController waitingController) {
        this.waitingController = waitingController;
        this.currentController=waitingController;
    }

    /**
     * Sets the controller for choosing objective cards and assigns it as the current controller.
     * @param chooseObjectiveController The ChooseObjectiveController instance to set.
     */
    public void setChooseObjectiveController(ChooseObjectiveController chooseObjectiveController) {
        this.chooseObjectiveController = chooseObjectiveController;
        this.currentController=chooseObjectiveController;
    }

    /**
     * Sets the controller for choosing starter cards and assigns it as the current controller.
     * @param chooseStarterController The ChooseStarterController instance to set.
     */
    public void setChooseStarterController(ChooseStarterController chooseStarterController) {
        this.chooseStarterController = chooseStarterController;
        this.currentController=chooseStarterController;
    }

    /**
     * Sets the controller for choosing player colors and assigns it as the current controller.
     * @param chooseColorController The ChooseColorController instance to set.
     */
    public void setChooseColorController(ChooseColorController chooseColorController) {
        this.chooseColorController = chooseColorController;
        this.currentController=chooseColorController;
    }

    /**
     * Sets the controller for the lobby interface and assigns it as the current controller.
     * @param lobbyGUIController The LobbyGUIController instance to set.
     */
    public void setLobbyGUIController(LobbyGUIController lobbyGUIController) {
        this.lobbyGUIController = lobbyGUIController;
        this.currentController=lobbyGUIController;
    }

    /**
     * Sets the controller for the game board interface and assigns it as the current controller.
     * @param gameBoardController The GameBoardController instance to set.
     */
    public void setGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
        this.currentController=gameBoardController;
    }

    @Override
    public void run() {

    }
}
