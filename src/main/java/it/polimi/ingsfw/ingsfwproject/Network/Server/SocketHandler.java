package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.FirstMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The SocketHandler class represents a handler for client connections via sockets in the server.
 * It manages communication with a specific client.
 */
public class SocketHandler extends AbstractHandler implements Runnable{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /**
     * Constructs a new SocketHandler for a client connection.
     *
     * @param socket   The Socket representing the client connection.
     * @param clientID The unique ID assigned to the client.
     * @param server   The Server instance managing this handler.
     * @throws IOException If there is an error initializing input or output streams.
     */
    public SocketHandler(Socket socket, int clientID, Server server) throws IOException {
        super(clientID, server);
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
        FirstMessage firstMessage=new FirstMessage(clientID);
        this.sendMessage(firstMessage);
        System.out.println("Server client handler inizializzato");
    }

    /**
     * Sends a message to the client associated with this handler.
     *
     * @param message The Message object to send.
     */
    @Override
    public void sendMessage(Message message) {
        try {
            if(this.getClientID()==message.getClientID() || message.getClientID()==-10){
                out.writeObject(message);
                out.flush();
                out.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
            //disconnect(); //TODO creare metodo disconnect
        }
    }

    /**
     * Runs the thread for handling incoming messages from the client.
     */
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message m = (Message) in.readObject();
                handleMessageIn(m);
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            //System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
