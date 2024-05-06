package it.polimi.ingsfw.ingsfwproject.View2;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.util.HashMap;
import java.util.Queue;

public abstract class View implements Runnable{
    Client client;
    Queue<Message> messages;

    public abstract void scegliPrimaAzione();

    public abstract void scegliMetodoConnessione();

    public abstract void creaPartita();

    public abstract void scegliPartitaEUnisciti(HashMap<Integer, Integer> gamesMap);

    public void receiveMessage(Message message) {
        synchronized (messages) {
            messages.add(message);
            messages.notifyAll();
        }
    }

    public abstract void handleMessage(Message message);
}
