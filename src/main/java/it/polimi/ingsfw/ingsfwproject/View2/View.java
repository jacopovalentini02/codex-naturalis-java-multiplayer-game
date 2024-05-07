package it.polimi.ingsfw.ingsfwproject.View2;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class View implements Runnable{
    Client client;
    //todo: usare blockinglinkedqueue come tipo di coda
    public LinkedBlockingQueue<Message> messages;

    public abstract void chooseFirstAction();

    public abstract void chooseConnectionMethod();

    public abstract void createGame();

    public abstract void chooseGameAndJoin(HashMap<Integer, Integer> gamesMap);

    public void receiveMessage() {
        //todo: cambiare questo ciclo con un metodo piu intelligente
        while (true){
            try {
                Message toProcess = messages.take();
                handleMessage(toProcess);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            /*
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

             */
        }

    }

    public abstract void handleMessage(Message message);

    public void addToQueue(Message message){
        this.messages.add(message);
    }

}
