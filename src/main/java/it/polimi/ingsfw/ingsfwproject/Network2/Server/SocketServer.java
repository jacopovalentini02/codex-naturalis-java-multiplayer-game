package it.polimi.ingsfw.ingsfwproject.Network2.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    private int port;
    public SocketServer(int port) {
        this.port = port;
    }
    public void startServer() {
        GameManager manager = new GameManager();
        LobbyController lobbyController = new LobbyController(manager);
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port); } catch (IOException e) {
            System.err.println(e.getMessage()); // porta non disponibile
            return; }
        System.out.println("Server ready"); while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Connessione stabilita con client: "+socket.getInetAddress());
                executor.submit(new ServerClientHandler(socket, lobbyController, manager) );
            } catch(IOException e) {
                break; // entrerei qui se serverSocket venisse chiuso
            } }
        executor.shutdown();
    }
    public static void main(String[] args) {
        SocketServer echoServer = new SocketServer(1337);
        echoServer.startServer();
    }
}
