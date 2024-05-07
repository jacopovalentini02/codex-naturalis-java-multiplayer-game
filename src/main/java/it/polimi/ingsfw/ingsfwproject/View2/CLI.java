package it.polimi.ingsfw.ingsfwproject.View2;

import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.JoinGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.FirstMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.SendGameListMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CLI extends View implements Runnable {
    Scanner scanner;

    public CLI(){
        this.scanner = new Scanner(System.in);
        super.messages=new ConcurrentLinkedQueue<Message>();
        Thread receive=new Thread(super::receiveMessage);
        receive.start();
    }

    @Override
    public void chooseFirstAction(){
        int choice = 0;
        Message messageToSend;

        //------------------------SCELTA DELLA PRIMA AZIONE (CREA, JOINA...)

            do {
            System.out.println("Scegli un'opzione:");
            System.out.println("1. Crea una nuova partita");
            System.out.println("2. Unisciti a una partita esistente");
            System.out.print("Inserisci il numero dell'opzione (1 o 2): ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice == 1 || choice == 2) {
                    break;
                }
            } else {
                scanner.next();
            }
            System.out.println("Input non valido. Riprova.");
        } while (true);


        //------------------------CREAZIONE DELLA PARTITA o SCELTA DELLA PARTITA GIà IN CORSO

        scanner.nextLine();

        if (choice == 1) {
            createGame();
        }else if (choice == 2) {
            messageToSend = new GetGameListMessage(client.getClientID());
            try {
                client.sendMessage(messageToSend);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


}

    @Override
    public void chooseConnectionMethod(){
        String ip;
        int port;
        int option = 1;

        do {
            if(option <1 || option > 2)
                System.out.println("Opzione non valida");
            System.out.println("\nVuoi usare Socket o RMI? \n1) Socket \n2) RMI");
            if(scanner.hasNextInt())
                option = scanner.nextInt();
            scanner.nextLine();

        }while(option <1 || option > 2);

        //todo: togliere questa parte commentata per far inserire questi dati all'utente a fine sviluppo
        /*
        System.out.println("Inserisci l'IP del server: ");
        ip = scanner.nextLine();

        System.out.println("Inserisci la porta a cui connettersi");
        port = scanner.nextInt();

        scanner.nextLine();
        */

        ip = "localhost";
        port = (option ==2? 1337 : 1099);

        try {
            this.client = (option == 2? new RMIClient(ip,port,this) : new SocketClient(ip,port,this));
            client.startConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void createGame() {
        String username;
        Message messageToSend;


        System.out.println("Inserisci il numero di giocatori per questa partita, da 2 a 4: ");
        int numOfPlayers = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Inserisci il tuo username:");
        username = scanner.nextLine();

        messageToSend = new CreateGameMessage(client.getClientID(), numOfPlayers, username);
        try {
            client.sendMessage(messageToSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void chooseGameAndJoin(HashMap<Integer, Integer> gamesMap) {
        String username;
        Message messageToSend;

        System.out.printf("%-10s %-20s %n", "ID", "Numero di Giocatori");
        for (HashMap.Entry<Integer, Integer> entry : gamesMap.entrySet()) {
            System.out.printf("%-10d %-20d %n", entry.getKey(), entry.getValue());
        }
        System.out.println("Inserisci l'ID della partita a cui vuoi unirti: ");
        int GameID = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Inserisci il tuo username: ");
        username = scanner.nextLine();

        messageToSend = new JoinGameMessage(client.getClientID(), username, GameID);
        try {
            client.sendMessage(messageToSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
        this.chooseConnectionMethod();
        this.chooseFirstAction();

        Thread commandThread = new Thread(this::receiveInput);
        commandThread.start();
    }

    public void receiveInput(){
        while(true){
            String command = scanner.nextLine();
            executeCommand(command);

        }
    }

    public void executeCommand(String command){
        switch(command){
            case "prova":
                System.out.println("prova recepita");
                break;
        }

    }

    @Override
    public void handleMessage(Message message){
        switch (message.getType()) {
            case FIRST_MESSSAGE:
                FirstMessage firstM=(FirstMessage) message;
                System.out.println(firstM.getClientID());
                break;
            case SEND_GAME_LIST:
                chooseGameAndJoin(((SendGameListMessage) message).getGameList());
                break;
            case GAME_JOINED:
                //todo:stampare le info della partita
                System.out.println("game joined");
                break;
            case STARTER_CARD:
                //todo: stampare la carta
                System.out.println("starter card selected correctly");
                break;
            case GOLD_DECK:


        }
    }


}
