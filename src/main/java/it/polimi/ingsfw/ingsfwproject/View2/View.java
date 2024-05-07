package it.polimi.ingsfw.ingsfwproject.View2;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class View implements Runnable{

    Client client;
    public ConcurrentLinkedQueue<Message> messages;


    //public abstract void chooseFirstAction();

    public abstract void chooseConnectionMethod();

    //public abstract void createGame();

    //public abstract void chooseGameAndJoin(HashMap<Integer, Integer> gamesMap);

    public void receiveMessage() {
        while(true){
            if(!messages.isEmpty()){
                Message message = messages.poll();
                handleMessage(message);
            }
        }

        /*
        while(true){
            synchronized(messages){
                while(messages.isEmpty()){
                    try {
                        messages.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                handleMessage(messages.poll());
            }
        }
         */

    }

    public abstract void handleMessage(Message message);

    public void addToQueue(Message message){
        messages.add(message);

        /*
        synchronized (messages){
            messages.add(message);
            messages.notifyAll();
        }
         */
    }

}
