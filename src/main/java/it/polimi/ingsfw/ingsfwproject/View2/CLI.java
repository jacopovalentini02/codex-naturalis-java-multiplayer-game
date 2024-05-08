/*package it.polimi.ingsfw.ingsfwproject.View2;

import it.polimi.ingsfw.ingsfwproject.Model.Coordinate;
import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.JoinGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;

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
        input= input.toLowerCase();

        try {
            switch (input) {
                case "creategame":
                    int numOfPlayers = askForIntInput("insert the number of players between 2 and 4", 2, 4);
                    name = askForStringInput("insert your nickname");
                    messageToSend = new CreateGameMessage(client.getClientID(), numOfPlayers, name);
                    client.sendMessage(messageToSend);
                    break;
                case "joingame":
                    int gameID = askForIntInput("insert the game ID", 0, Integer.MAX_VALUE);
                    name = askForStringInput("insert your nickname");
                    messageToSend = new JoinGameMessage(client.getClientID(), name, gameID);
                    client.sendMessage(messageToSend);
                    break;
                case "getgamelist":
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

        //todo: bloccare i comandi CreateGame, JoinGame e GetGameList direttamente da qui, oppure la cli li manda e poi ci penserà il lobbycontroller?

        String lobbyCommands = "now you can insert one of the following commands:" + "\n\t-> CreateGame" + "\n\t-> JoinGame" + "\n\t-> GetGameList";
        String GameStartingCommands = "now you can do one of the following actions: \n\t-> ChooseObjectiveCard \n\t-> getColorAvailable \n\t-> chooseColor";
        String gameCommands = "now you can do one of the following actions: \n\t-> PlayCard \n\t-> DrawCard \n\t-> PickCard  \n\t-> SkipTurn";

        switch(message.getType()){
            case FIRST_MESSSAGE:
                FirstMessage firstMessage = (FirstMessage) message;
                System.out.println("the connection was successfully established, your ClientID is: " + message.getClientID());
                System.out.println(lobbyCommands);
                break;
            case GAME_JOINED:
                System.out.println("you have joined the game correctly. Your ID is: " + message.getClientID());
                break;
            case NEW_PLAYER_JOINED:
                //todo: stampare le info del player che ha joinato la partita
                System.out.println("a player has joined the game");
                break;
            case STARTER_CARD:
                //todo: stampare la carta
                System.out.println("you have a new starter card");
                break;
            case GOLD_DECK:
                //todo: forse da eliminare??
                System.out.println("the gold deck has been changed");
                break;
            case RESOURCE_DECK:
                //todo: forse da eliminare??
                System.out.println("the resource deck has been changed");
                break;
            case DISPLAYED_PLAYABLE_CARDS:
                //todo: stampare le displayed card nuove!
                System.out.println("the displayed cards have been changed");
                break;
            case CURRENT_PLAYER:
                CurrentPlayerMessage currentPlayerMessage = (CurrentPlayerMessage) message;
                System.out.println("it' now " + currentPlayerMessage.getCurrentPlayer() + "'s turn");
                //todo: se è il mio turno, devo stampare la stringa gameCommands;
                break;
            case COORDINATES_AVAILABLE:
                CoordinatesAvailableMessage coordinatesAvailableMessage = (CoordinatesAvailableMessage) message;
                if(coordinatesAvailableMessage.getCoords().isEmpty()) {
                    System.out.println("you have no more coordinates available!");
                    break;
                }
                System.out.println("your available positions have changed: now you can play a card in the following coordinates:");
                for(Coordinate c : coordinatesAvailableMessage.getCoords())
                    System.out.println(c);
                break;
            case HAND_OBJECTIVE:
                //todo: che cosa deve fare? viene ricevuto quando ha scelto la carta o quando gli vengono assegnate tutte e due?
                break;
            case GAME_STATE:
                Message gameStateMessage = (GameStateMessage) message;
                //todo: scrivere lo stato in cui viene cambiato il gioco
                System.out.println("the game state has been changed to: " );
                break;
            case GRID:
                GridMessage gridMsg=(GridMessage) message;
                //todo: stampare la nuova grid aggiornata
                System.out.println("Your grid has been changed to: " );
                break;
            case RESOURCES:
                ResourcesMessage resourcesMessage=(ResourcesMessage) message;
                //todo: controllare che la stampa delle risorse sia corretta
                System.out.println("You now have these resources:  " + resourcesMessage.getResources());
                break;
            case WINNER:
                WinnerMessage winnerMessage=(WinnerMessage) message;
                System.out.println("the winner is " + winnerMessage.getNickname() + "!!!");
                break;
            case SEND_GAME_LIST:
                SendGameListMessage sendGameListMessage = (SendGameListMessage) message;
                System.out.println("the currently available games are:");
                HashMap<Integer, Integer> gameList = sendGameListMessage.getGameList();
                System.out.println("ID\tNumberOfPlayers");
                for(Integer gameID : gameList.keySet())
                    System.out.println(gameID + "\t" + gameList.get(gameID));
                System.out.println(lobbyCommands);
                break;


        }

    }

    @Override
    public void chooseConnectionMethod() {

        int choice = askForIntInput("Choose a connection method\n1. Socket\n2. RMI", 1, 2);

        //todo: a fine sviluppo va chiesto all'utente!
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
                if (choice < lowerBound || choice > upperBound)
                    System.out.println(errorString);
                System.out.println(stringToPrompt);
                choice = scanner.nextInt();
                scanner.nextLine();
            }catch (InputMismatchException e){
                System.out.println(errorString);
                choice--;
            }
        }while(choice <lowerBound || choice > upperBound);

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


}*/
