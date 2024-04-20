package it.polimi.ingsfw.ingsfwproject.Network.Server;

import it.polimi.ingsfw.ingsfwproject.Controller.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.lang.*;
public class SocketHandler implements Runnable {
    private String nickname;
    private int idGame;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    @Override
    public void run() {

    }
}
