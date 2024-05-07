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
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CLI extends View implements Runnable {
    Scanner scanner;

    public CLI(){
        this.scanner = new Scanner(System.in);
        super.messages = new ConcurrentLinkedQueue<>();
    }

    public void run(){
        chooseConnectionMethod();
        Thread readUserInputThread = new Thread(this::readInputUser);
        readUserInputThread.start();

        Thread readMessageThread = new Thread(super::receiveMessage);
        readMessageThread.start();

    }

    private void readInputUser(){
        while(true){
            handleInput(scanner.nextLine());
        }
    }

    private void handleInput(String input){
        Message messageToSend;
        String name;
        try {
            switch (input) {
                case "CreateGame":
                    int numOfPlayers = askForIntInput("insert the number of players between 2 and 4", 2, 4);
                    name = askForStringInput("insert your nickname");
                    messageToSend = new CreateGameMessage(client.getClientID(), numOfPlayers, name);
                    client.sendMessage(messageToSend);
                    break;
                case "JoinGame":
                    int gameID = askForIntInput("insert the game ID", 0, Integer.MAX_VALUE);
                    name = askForStringInput("insert your nickname");
                    messageToSend = new JoinGameMessage(client.getClientID(), name, gameID);
                    client.sendMessage(messageToSend);
                    break;
                case "GetGameList":
                    messageToSend = new GetGameListMessage(client.getClientID());
                    client.sendMessage(messageToSend);
                    break;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Message message){
        switch(message.getType()){
            case FIRST_MESSSAGE:
                FirstMessage firstMessage = (FirstMessage) message;
                System.out.println("the connection was successfully established, your ClientID is: " + message.getClientID());
                System.out.println("now you can insert the command:");
                System.out.println("\t-> CreateGame");
                System.out.println("\t-> JoinGame");
                System.out.println("\t-> GetGameList");
                break;
            case SEND_GAME_LIST:
                SendGameListMessage sendGameListMessage = (SendGameListMessage) message;
                System.out.println("the game currently available are:");
                HashMap<Integer, Integer> gameList = sendGameListMessage.getGameList();
                System.out.println("ID\tNumberOfPlayers");
                for(Integer gameID : gameList.keySet())
                    System.out.println(gameID + "\t" + gameList.get(gameID));
                System.out.println("now you can insert the command:");
                System.out.println("\t-> CreateGame");
                System.out.println("\t-> JoinGame");
                System.out.println("\t-> GetGameList");
                break;
            case GAME_JOINED:
                System.out.println("you have joined the game correctly");
                break;
        }

    }

    @Override
    public void chooseConnectionMethod() {

        int choice = askForIntInput("Choose a connection method\n1. Socket\n2. RMI", 1, 2);

        String ip = "localhost";
        int port = choice == 1? 1337 : 1099;

        try {
            super.client = choice == 1? new SocketClient(ip,port, this) : new RMIClient(ip,port, this);
            client.startConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private int askForIntInput(String stringToPrompt, int lowerBound, int upperBound){
        int choice = lowerBound;
        String errorString = "Error: you have to insert a number between " + lowerBound + " and " + upperBound;
        do {
            try {
                if (choice < lowerBound && choice > upperBound)
                    System.out.println(errorString);
                System.out.println(stringToPrompt);
                choice = scanner.nextInt();
                scanner.nextLine();
            }catch (InputMismatchException e){
                System.out.println(errorString);
            }
        }while(choice <lowerBound && choice > upperBound);

        return choice;
    }

    private String askForStringInput(String stringToPrompt){
        System.out.println(stringToPrompt);
        return scanner.nextLine();
    }



    /*
    Scanner scanner;

    public CLI(){
        this.scanner = new Scanner(System.in);
        super.messages=new LinkedBlockingQueue<Message>();
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
                scanner.nextLine();
                if (choice == 1 || choice == 2) {
                    break;
                }
            } else {
                scanner.next();
            }
            System.out.println("Input non valido. Riprova.");
        } while (true);


        //------------------------CREAZIONE DELLA PARTITA o SCELTA DELLA PARTITA GIà IN CORSO

//        scanner.nextLine();

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
            if(scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine();
            }
        }while(option <1 || option > 2);

        //todo: togliere questa parte commentata per far inserire questi dati all'utente a fine sviluppo

        System.out.println("Inserisci l'IP del server: ");
        ip = scanner.nextLine();

        System.out.println("Inserisci la porta a cui connettersi");
        port = scanner.nextInt();

        scanner.nextLine();


        ip = "localhost";
        port = (option ==2? 1099 : 1337);

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
        int gameID = 0;

        System.out.printf("%-10s %-20s %n", "ID", "Numero di Giocatori");
        for (HashMap.Entry<Integer, Integer> entry : gamesMap.entrySet()) {
            System.out.printf("%-10d %-20d %n", entry.getKey(), entry.getValue());
        }

        System.out.println("Inserisci l'ID della partita a cui vuoi unirti: ");
        if(scanner.hasNextInt()) {
            gameID = scanner.nextInt();
            scanner.nextLine();
        }



        System.out.println("Inserisci il tuo username: ");
        username = scanner.nextLine();

        messageToSend = new JoinGameMessage(client.getClientID(), username, gameID);
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
            case "chooseConnectionMethod":
                chooseConnectionMethod();
                break;
            case "chooseFirstAction":
                chooseFirstAction();
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
    */


}
