package it.polimi.ingsfw.ingsfwproject.View2;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class View implements Runnable{
    Client client;
    public ConcurrentLinkedQueue<Message> messages;

    public abstract void scegliPrimaAzione();

    public abstract void scegliMetodoConnessione();

    public abstract void creaPartita();

    public abstract void scegliPartitaEUnisciti(HashMap<Integer, Integer> gamesMap);

    public void receiveMessage() {
        while (true){
            Message toProcess = messages.poll();
            if (toProcess != null){
                handleMessage(toProcess);
            } else {
                //coda vuota
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    System.out.println("Interrupted Exception");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

    }

    public abstract void handleMessage(Message message);

    public void addToQueue(Message message){
        this.messages.add(message);
    }

}
