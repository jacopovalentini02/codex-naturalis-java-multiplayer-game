package it.polimi.ingsfw.ingsfwproject.View.GUI;

import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static it.polimi.ingsfw.ingsfwproject.View.View.client;


import java.net.ConnectException;
import java.net.URI;

/**
 * The controller of the scene where you have to put the server's IP and choose the type of connection
 */
public class ChooseConnectionController extends Application  {
    @FXML public TextField serverIP;
    @FXML
    private Button socketButton;
    @FXML
    private Button rmiButton;
    public static GUIView guiView;

    private static final double MIN_WIDTH = 449.0;
    private static final double MIN_HEIGHT = 441.0;

    /**
     * Initializes and displays the primary stage for the connection selection UI.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML file or setting the scene
     */
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsfw/ingsfwproject/ChooseConnection.fxml"));

        Parent root = loader.load();
        loader.setController(this);
        guiView.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Choose connection");
        stage.setScene(scene);

        // Ascoltatore per gestire le dimensioni della finestra
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() < MIN_WIDTH) {
                stage.setMinWidth(newValue.doubleValue());
            } else {
                stage.setMinWidth(MIN_WIDTH);
            }
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() < MIN_HEIGHT) {
                stage.setMinHeight(newValue.doubleValue());
            } else {
                stage.setMinHeight(MIN_HEIGHT);
            }
        });
        stage.show();
        stage.setOnCloseRequest((event->{
            Platform.exit();
            System.exit(0);
        }));
    }

    /**
     * Handles the socket connection to the server.
     * This method retrieves the server IP address from the input field, validates it,
     * and attempts to establish a socket connection to the server using the specified IP and port.
     * If the IP address is invalid, it notifies the user. If the connection attempt fails,
     * it shows a connection error to the user. If the connection is successful, it opens the lobby view.
     */
    @FXML
    private void handleSocketConnection(){
        String ip=serverIP.getText();
        if(validateIp(ip)){
            try {
                client = new SocketClient(ip, 1337, guiView);
                client.startConnection();
                if (client.isConnected()) {
                    guiView.openLobby();
                } else {
                    guiView.showConnectionError();
                }
            } catch (ConnectException e) {
                guiView.showConnectionError();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            guiView.notifyException("IP Address not valid!");
        }
    }

    /**
     * This method retrieves the server IP address from the input field, validates it,
     * and attempts to establish an RMI connection to the server using the specified IP and port.
     * If the IP address is invalid, it notifies the user. If the connection attempt fails,
     * it shows a connection error to the user.
     */
    @FXML
    private void handleRMIConnection() {
        String ip=serverIP.getText();
        if(validateIp(ip)){
            try {
                client = new RMIClient(ip, 1099, guiView);
                client.startConnection();
                guiView.openLobby();
            } catch (Exception e) {
                guiView.showConnectionError();
            }
        }else{
            guiView.notifyException("IP Address not valid!");
        }


    }

    /**
     * Validates the given IPv4 address by using a regex.
     *
     * @param ip string of the ip address to be validated
     * @return {@code true} if the ip is valid, {@code false} otherwise.
     */
    public boolean validateIp(String ip){
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        if (ip.matches(regex)) {
            return true;
        } else {
            try {
                new URI(ip);
                return true;
            }
            catch (Exception e) {
                return false;
            }

        }
    }

    /**
     * Sets the GUI view for the application.
     *
     * @param gui the {@link GUIView} instance to be set
     */
    public static void setGuiView(GUIView gui) {
        guiView = gui;
    }


}
