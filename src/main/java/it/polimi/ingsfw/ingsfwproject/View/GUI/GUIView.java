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


    public GUIView(){
        chooseConnection();
        chatOptions = FXCollections.observableArrayList();
        chatOptions.add("global");
        chats = new HashMap<>();
        chats.put("global", FXCollections.observableArrayList());

    }

    public ObservableList<String> getChatOptions(){
        return this.chatOptions;
    }

    public Map<String, ObservableList<ChatMessage>> getChats(){
        return this.chats;
    }

    public void openLobby() {
        Platform.runLater(() -> {
            System.out.println("open lobby");
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

    public void showConnectionError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText(null);
            alert.setContentText("Connection could not be established. Please retry. Server may be down.");
            alert.showAndWait();
        });

    }

    @Override
    public void chooseConnection() {
        ChooseConnectionController.setGuiView(this);
        // Lanciare ChooseConnectionApp
        new Thread(() -> Application.launch(ChooseConnectionController.class)).start();


    }

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

    @Override
    public void displayFirstMessage(int clientID) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Messaggio");
            alert.setHeaderText(null);
            alert.setContentText("Connessione stabilita");
            alert.showAndWait();
        });
    }

    @Override
    public void displayGameList(HashMap<Integer, Integer> gameList) {
        Platform.runLater(() -> {
            lobbyGUIController.showGames(gameList);
        });
    }

    @Override
    public void notifyGameJoined(int idGame) {
        openWaiting();
        Platform.runLater(() -> {
            if (waitingController != null && waitingController.getNewPlayerJoined() != null) {
                waitingController.setPlayerNickname(client.getNickname());
            } else {
                System.err.println("Errore: setUpGame o newPlayerJoined TextArea è null");
            }
        });
    }

    @Override
    public void notifyNewPlayerJoined(ArrayList<String> nicknames) {
        Platform.runLater(() -> {
            if (waitingController != null && waitingController.getNewPlayerJoined() != null) {
                String lastNickname = nicknames.getLast();
                waitingController.addNickname(lastNickname);
                for (String s: nicknames)
                    addChat(s);
            } else {
                System.err.println("Errore: setUpGame o newPlayerJoined TextArea è null");
            }
        });


    }

    public void addChat(String nickname){
        if (!Objects.equals(nickname, client.getNickname()) && !(chats.containsKey(nickname))){
            chatOptions.add(nickname);
            chats.put(nickname, FXCollections.observableArrayList());
        }
    }

    @Override
    public void notifyStarterCard() {
        Platform.runLater(() -> {
            chooseStarterController.cardChosen();
        });
    }

    @Override
    public void notifyColorsAvailable(List<PlayerColor> colors) {
        Platform.runLater(() -> {
            chooseColorController.setColors(colors);
        });
    }

    @Override
    public void notifyGoldDeckUpdate() {
        if(currentController.equals(gameBoardController)){
            Platform.runLater(() -> {
                gameBoardController.updateGoldDeck();
            });
        }
    }

    @Override
    public void notifyResourceDeckUpdate() {
        if(currentController.equals(gameBoardController)){
            Platform.runLater(() -> {
                gameBoardController.updateResourceDeck();
            });
        }
    }

    @Override
    public void notifyDisplayedCardsUpdate(ArrayList<PlayableCard> displayedCards) {
        if(currentController.equals(gameBoardController)){
            Platform.runLater(() -> {
                gameBoardController.updateDisplayedCard();
            });
        }

    }

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

    @Override
    public void notifyAvailablePositions(ArrayList<Coordinate> coord) {

    }

    @Override
    public void notifyHandObjectives(ArrayList<ObjectiveCard> cards) {
        Platform.runLater(() -> {
            if(currentController.equals(chooseObjectiveController))
                chooseObjectiveController.showObjective(cards);
        });
    }

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
                alert.setTitle("Messaggio");
                alert.setHeaderText(null);
                alert.setContentText("Gamestate ending");
                alert.showAndWait();
            });
        }else if(state==GameState.ENDED){
            System.out.println("game ended");
            Platform.runLater(()->{
                try {
                    System.out.println("qua");
                    openLobby();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });


        }

    }

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

    @Override
    public void notifyResourcesUpdate(String nickname, HashMap<Content, Integer> resources) {

    }

    @Override
    public void notifyWinnerUpdate(String nickname) {
        System.out.println("Winner: "+nickname);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Messaggio");
            alert.setHeaderText(null);

            StringBuilder message = new StringBuilder(nickname + "has won! \n" +
                            "Final scores: \n");

            for (Map.Entry<String, Integer> entry: client.getVirtualView().getScores().entrySet())
                message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");

            Label label = new Label(message.toString());
            label.setWrapText(true);

            alert.getDialogPane().setContent(label);
            alert.showAndWait();
        });

    }

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

    @Override
    public void notifyDisplayedObjectives(List<ObjectiveCard> cards) {

    }

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

    @Override
    public void notifyColorChosen(PlayerColor color) {

    }

    @Override
    public void notifyChatMessage(ChatMessage message) {

        if(currentController.equals(chooseStarterController)){
            Platform.runLater(() -> {
                chooseStarterController.addMessageToChat(message);
            });
        }else if(currentController.equals(chooseColorController)){
            Platform.runLater(() -> {
                chooseColorController.addMessageToChat(message);
            });
        }else if(currentController.equals(chooseObjectiveController)){
            Platform.runLater(() -> {
                chooseObjectiveController.addMessageToChat(message);
            });
        }else if(currentController.equals(gameBoardController)){
            Platform.runLater(() -> {
                //gameBoardController.add();
            });
        }
    }

    @Override
    public void notifyCurrentPlayerHasPlayed(boolean bool){
        //TODO: DA FARE
    }

    @Override
    public void run() {
         //Application.launch(ChooseConnectionApp.class);
       //chooseConnection();
     }

    public void setClient(Client c){
        super.client = c;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setSetUpGameController(WaitingController waitingController) {
        this.waitingController = waitingController;
        this.currentController=waitingController;
    }

    public void setChooseObjectiveController(ChooseObjectiveController chooseObjectiveController) {
        this.chooseObjectiveController = chooseObjectiveController;
        this.currentController=chooseObjectiveController;
    }

    public void setChooseStarterController(ChooseStarterController chooseStarterController) {
        this.chooseStarterController = chooseStarterController;
        this.currentController=chooseStarterController;
    }


    public void setChooseColorController(ChooseColorController chooseColorController) {
        this.chooseColorController = chooseColorController;
        this.currentController=chooseColorController;
    }

    public void setLobbyGUIController(LobbyGUIController lobbyGUIController) {
        this.lobbyGUIController = lobbyGUIController;
        this.currentController=lobbyGUIController;
    }

    public void setGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
        this.currentController=gameBoardController;
    }
}
