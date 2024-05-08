package it.polimi.ingsfw.ingsfwproject.View.CLI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.JoinGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.View.View;

public class Cli extends View implements Runnable {

    Scanner scanner;

    public Cli(){
        this.scanner = new Scanner(System.in);
        super.messages=new ConcurrentLinkedQueue<Message>();
        Thread receive=new Thread(super::receiveMessage);
        receive.start();
    }

    public void init(){
        System.out.println("  ____          _             _   _       _                   _ _     \n" +
                " / ___|___   __| | _____  __ | \\ | | __ _| |_ _   _ _ __ __ _| (_)___ \n" +
                "| |   / _ \\ / _` |/ _ \\ \\/ / |  \\| |/ _` | __| | | | '__/ _` | | / __|\n" +
                "| |__| (_) | (_| |  __/>  <  | |\\  | (_| | |_| |_| | | | (_| | | \\__ \\\n" +
                " \\____\\___/ \\__,_|\\___/_/\\_\\ |_| \\_|\\__,_|\\__|\\__,_|_|  \\__,_|_|_|___/");
        System.out.println("\nYou are now playing Codex Naturalis\n");
    }

    @Override
    public void chooseFirstAction() {
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
    public void chooseConnection() {
        String ip;
        int port;
        int option = 1;
        Message messageToSend;

        do {
            if(option <1 || option > 2)
                System.out.println("Opzione non valida");
            System.out.println("\nVuoi usare Socket o RMI? \n1) Socket \n2) RMI");
            if(scanner.hasNextInt())
                option = scanner.nextInt();
            scanner.nextLine();

        }while(option <1 || option > 2);


        System.out.println("Inserisci l'IP del server: ");
        ip = scanner.nextLine();

        System.out.println("Inserisci la porta a cui connettersi");
        port = scanner.nextInt();

        scanner.nextLine();

        try {
            this.client = (option == 2? new RMIClient(ip,port,this) : new SocketClient(ip,port,this));
            client.startConnection();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
        do{
            System.out.println("Inserisci il tuo username: ");
            username = scanner.nextLine();
            if(username.length()<2){
                System.out.println("Username non valido, la lunghezza minima è di 2 caratteri!\n");
            }
        }while(username.length()<2);

        messageToSend = new CreateGameMessage(client.getClientID(), numOfPlayers, username);
        try {
            client.sendMessage(messageToSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseGameToJoin(HashMap<Integer, Integer> gamesMap) {
        String username;
        Message messageToSend;

        System.out.printf("%-10s %-20s %n", "ID", "Numero di Giocatori");
        for (HashMap.Entry<Integer, Integer> entry : gamesMap.entrySet()) {
            System.out.printf("%-10d %-20d %n", entry.getKey(), entry.getValue());
        }
        System.out.println("Inserisci l'ID della partita a cui vuoi unirti: ");
        int GameID = scanner.nextInt();
        scanner.nextLine();

        do{
            System.out.println("Inserisci il tuo username: ");
            username = scanner.nextLine();
            if(username.length()<2){
                System.out.println("Username non valido, la lunghezza minima è di 2 caratteri!");
            }
        }while(username.length()<2);

        messageToSend = new JoinGameMessage(client.getClientID(), username, GameID);
        try {
            client.sendMessage(messageToSend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case FIRST_MESSSAGE:
                FirstMessage firstM=(FirstMessage) message;
                System.out.println(firstM.getClientID());
                break;
            case SEND_GAME_LIST:
                chooseGameToJoin(((SendGameListMessage) message).getGameList());
                break;
            case GAME_JOINED:
                //todo:stampare le info della partita
                System.out.println("Game joined");
                break;
            case NEW_PLAYER_JOINED:
                //TODO: recuperare username del player che ha joinato e farne display
                System.out.println("A new player joined the game!");
                break;
            case STARTER_CARD:
                //todo: stampare la carta
                System.out.println("Starter card selected correctly");
                break;
            case GOLD_DECK:
                System.out.println("Gold deck updated!");
                break;
            case RESOURCE_DECK:
                System.out.println("Resourced deck updated!");
                break;
            case DISPLAYED_PLAYABLE_CARDS:
                System.out.println("Displayed playable cards updated! Here's how:\n");
                for(PlayableCard c : ((DispayedPlayableCardMessage) message).getDisplayedPlayableCard()){
                    //TODO: Stampare la carta (solo fronte suppongo)
                }
                break;
            case CURRENT_PLAYER:
                System.out.println("Now it's " + /*((CurrentPlayerMessage) message).getCurrentPlayer()*/ client.getVirtualView().getCurrentPlayer() + " turn");
                break;
            case COORDINATES_AVAILABLE:
                System.out.println("Here's the available positions on your player ground: \n" + ((CoordinatesAvailableMessage) message).getCoords());
                break;
            case HAND_OBJECTIVE:
                System.out.println("Now you have to choose between these two objectives. The choice will be your secret objective!\n");
                for(ObjectiveCard c : ((HandObjectiveMessage) message).getCards() ){
                    //TODO: stampare gli obiettivi
                }
                break;
            case GAME_STATE:

                break;
            case GRID:

                break;
            case RESOURCES:

                break;
            case WINNER:

                break;
            case HAND_CARDS:

                break;
        }
    }

    @Override
    public void handleInput(Message message) {

    }

    @Override
    public void run() {
        this.chooseConnection();
        this.init();
        this.chooseFirstAction();

        while (true){
            if(messages.isEmpty()){

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }else
                handleMessage(messages.remove());

        }

    }

    public void printGrid(PlayerGround playerGround) {

    }
    public void printFacePlayed(Face face){
        // TODO: Questo metodo possiamo lasciarlo qui oppure creare una classe printer per la CLI e metterla li
        // TODO: Inoltre, forse non è necessare passare sia Card che Face...da rivedere!
        // TODO: Questo metodo si può usare per tutte le carte della grid tranne che per la back face della starter
        // TODO: -> Consigliato metodo a parte
        int i;
        AnsiColor cardType = getCardType(face);
        //upper-left corner
        printCorner(face.getTL(), 0, cardType);
        //space
        for (i=0; i<9; i++){
            System.out.print(cardType.getFormattedCharacter());
        }
        //upper-right corner
         printCorner(face.getTR(), 1, cardType);
        for(i=0; i<5; i++){
            System.out.print(cardType.getFormattedCharacter());
        }
        //center, if exists
        if(face instanceof NormalBack){
            switch(((NormalBack) face).getCenter()){
                case FUNGI_KINGDOM -> System.out.println(AnsiColor.FUNGI_TEXT.getFormattedCharacter());
                case PLANT_KINGDOM -> System.out.println(AnsiColor.PLANT_TEXT.getFormattedCharacter());
                case INSECT_KINGDOM -> System.out.println(AnsiColor.INSECT_TEXT.getFormattedCharacter());
                case ANIMAL_KINGDOM -> System.out.println(AnsiColor.ANIMAL_TEXT.getFormattedCharacter());
            }
        }
        else{
            System.out.print(cardType.getFormattedCharacter());
        }
        for(i=0; i<4; i++){
            System.out.print(cardType.getFormattedCharacter());
        }
        System.out.println(cardType.getFormattedCharacter());
        //lower-left corner
        printCorner(face.getBL(), 0, cardType);
        //space
        for (i=0; i<9; i++){
            System.out.print(cardType.getFormattedCharacter());
        }
        //lower-right corner
        printCorner(face.getBR(), 1, cardType);
    }

    public void printCorner(Content content, int rOrl, AnsiColor cardType) {
        if (rOrl == 0) {
            switch (content) {
                case FUNGI_KINGDOM -> System.out.print(AnsiColor.FUNGI_TEXT.getFormattedCharacter());
                case PLANT_KINGDOM -> System.out.print(AnsiColor.PLANT_TEXT.getFormattedCharacter());
                case INSECT_KINGDOM -> System.out.print(AnsiColor.INSECT_TEXT.getFormattedCharacter());
                case ANIMAL_KINGDOM -> System.out.print(AnsiColor.ANIMAL_TEXT.getFormattedCharacter());
                case EMPTY -> System.out.print(AnsiColor.EMPTY_TEXT.getFormattedCharacter());
                case HIDDEN -> System.out.print(cardType.getFormattedCharacter());
                case QUILL -> System.out.print(AnsiColor.QUILL_TEXT.getFormattedCharacter());
                case MANUSCRIPT -> System.out.print(AnsiColor.MANUSCRIPT_TEXT.getFormattedCharacter());
                case INKWELL -> System.out.print(AnsiColor.INKWELL_TEXT.getFormattedCharacter());
            }
        } else {
            switch (content) {
                case FUNGI_KINGDOM -> System.out.println(AnsiColor.FUNGI_TEXT.getFormattedCharacter());
                case PLANT_KINGDOM -> System.out.println(AnsiColor.PLANT_TEXT.getFormattedCharacter());
                case INSECT_KINGDOM -> System.out.println(AnsiColor.INSECT_TEXT.getFormattedCharacter());
                case ANIMAL_KINGDOM -> System.out.println(AnsiColor.ANIMAL_TEXT.getFormattedCharacter());
                case EMPTY -> System.out.println(AnsiColor.EMPTY_TEXT.getFormattedCharacter());
                case HIDDEN -> System.out.println(cardType.getFormattedCharacter());
                case QUILL -> System.out.println(AnsiColor.QUILL_TEXT.getFormattedCharacter());
                case MANUSCRIPT -> System.out.println(AnsiColor.MANUSCRIPT_TEXT.getFormattedCharacter());
                case INKWELL -> System.out.println(AnsiColor.INKWELL_TEXT.getFormattedCharacter());
            }
        }
    }

    public AnsiColor getCardType(Face face){
        Content background = Card.getType(face.getIdCard());
        return switch(background){
            case FUNGI_KINGDOM -> AnsiColor.FUNGI_BACKGROUND;
            case PLANT_KINGDOM ->  AnsiColor.PLANT_BACKGROUND;
            case INSECT_KINGDOM -> AnsiColor.INSECT_BACKGROUND;
            case ANIMAL_KINGDOM -> AnsiColor.ANIMAL_BACKGROUND;
            case QUILL -> null;
            case INKWELL -> null;
            case MANUSCRIPT -> null;
            case EMPTY -> null;
            case HIDDEN -> null;
        };
    }

    public void printPlayerHand(Player player){
        int i =1;
        for (PlayableCard c :player.getHandCard()){
            System.out.println("Front of card "+ i + ":");
            printFacePlayed(c.getFront());
            System.out.println("Back of card "+ i + ":");
            printFacePlayed(c.getBack());
            i++;
        }
    }
}
