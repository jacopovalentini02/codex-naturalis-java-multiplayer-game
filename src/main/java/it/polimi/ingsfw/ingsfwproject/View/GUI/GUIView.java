package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClientMessage;
import it.polimi.ingsfw.ingsfwproject.View.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
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


    public GUIView(){
        chooseConnection();
    }

    public void openLobby() {
        Platform.runLater(() -> {
            try {

                lobbyGUIController =new LobbyGUIController();
                currentController=lobbyGUIController;
                LobbyGUIController.setGuiView(this);
                //lobbyApp.setGuiView(this);
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

    public void openSetUpGame(){
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
    public void handleMessage(Message message) {
        ServerToClientMessage toProcess = (ServerToClientMessage)message;
        toProcess.execute(client);
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
        openSetUpGame();
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
                System.out.println("New player nickname: " + lastNickname);
                waitingController.addNickname(lastNickname);
            } else {
                System.err.println("Errore: setUpGame o newPlayerJoined TextArea è null");
            }
        });


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
        gameBoardController=new GameBoardController();

        if(state==GameState.WAITING_FOR_PLAYERS){
            openSetUpGame();
        }else if(state==GameState.CHOOSING_STARTER_CARDS){
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
        }

    }

    @Override
    public void notifyGridUpdate(String nickname, Map<Coordinate, Face> grid) {

    }

    @Override
    public void notifyResourcesUpdate(String nickname, HashMap<Content, Integer> resources) {

    }

    @Override
    public void notifyWinnerUpdate(String nickname) {

    }

    @Override
    public void notifyHandCardsUpdate(ArrayList<PlayableCard> cards) {
        if(client.getVirtualView().getState()==GameState.CHOOSING_STARTER_CARDS){
            Platform.runLater(() -> {
                if(!cards.isEmpty())
                    chooseStarterController.showStarter(cards.get(0));
            });
        }else if(client.getVirtualView().getState()==GameState.STARTED){
            Platform.runLater(() -> {
                gameBoardController.updateHand();
            });
        }

    }

    @Override
    public void notifyDisplayedObjectives(List<ObjectiveCard> cards) {

    }

    @Override
    public void notifyScores(Map<String, Integer> scores) {

    }

    @Override
    public void notifyColorChosen(PlayerColor color) {

    }

    @Override
    public void notifyChatMessage(ChatMessage message) {

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
