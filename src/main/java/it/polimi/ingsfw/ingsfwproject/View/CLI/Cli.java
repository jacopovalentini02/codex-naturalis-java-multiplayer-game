package it.polimi.ingsfw.ingsfwproject.View.CLI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.HashMap;
import java.util.Scanner;

import it.polimi.ingsfw.ingsfwproject.View.View;

public class Cli extends View implements Runnable {

    Scanner scanner;

    public Cli(){
        this.scanner = new Scanner(System.in);
        super.messages = new ConcurrentLinkedQueue<>();
    }

    public void init(){
        System.out.println("  ____          _             _   _       _                   _ _     \n" +
                " / ___|___   __| | _____  __ | \\ | | __ _| |_ _   _ _ __ __ _| (_)___ \n" +
                "| |   / _ \\ / _` |/ _ \\ \\/ / |  \\| |/ _` | __| | | | '__/ _` | | / __|\n" +
                "| |__| (_) | (_| |  __/>  <  | |\\  | (_| | |_| |_| | | | (_| | | \\__ \\\n" +
                " \\____\\___/ \\__,_|\\___/_/\\_\\ |_| \\_|\\__,_|\\__|\\__,_|_|  \\__,_|_|_|___/");
        System.out.println("\nYou are now playing Codex Naturalis\n");
    }

    public void run(){
        init();
        chooseConnection();
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

    private Coordinate askForCoordinateInput(){
        System.out.println("Where do you want to play the chosen card?");
        //TODO: COME SI PASSA L'ECCEZIONE? FACCIO UNA PROVA
        //TODO: Ora returno un valore a caso solo per fare push
        return new Coordinate(0,0);
    }

    private boolean askForFaceToPlay(){
        int iFace = askForIntInput("Which side?\n1)Front\n2)Back", 1, 2);
        return (iFace == 1)? true : false;
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
    };

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
        String lobbyCommands = "now you can insert one of the following commands:" + "\n\t-> CreateGame" + "\n\t-> JoinGame" + "\n\t-> GetGameList";
        String GameStartingCommands = "now you can do one of the following actions: \n\t-> ChooseObjectiveCard \n\t-> getColorAvailable \n\t-> chooseColor";
        String gameCommands = "now you can do one of the following actions: \n\t-> PlayCard \n\t-> DrawCard \n\t-> PickCard  \n\t-> SkipTurn";

        switch (message.getType()) {
            case FIRST_MESSSAGE:
                FirstMessage firstMessage = (FirstMessage) message;
                System.out.println("the connection was successfully established, your ClientID is: " + message.getClientID());
                System.out.println(lobbyCommands);
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
            case GAME_JOINED:
                System.out.println("You have joined the game correctly. Your ID is: " + message.getClientID());
                break;
            case NEW_PLAYER_JOINED:
                //TODO: recuperare username del player che ha joinato e farne display
                System.out.println("A new player joined the game!");
                break;
            case STARTER_CARD:
                //todo: stampare la carta
                System.out.println("Starter card selected correctly");
                break;
            case COLORS_AVAILABLE:
                ColorAvailableMessage colorAvailableMessage = (ColorAvailableMessage) message;
                System.out.println("The following colors are available :");
                for(PlayerColor color : colorAvailableMessage.getTokenAvailable()){
                    System.out.println(color.name());
                }
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
                CurrentPlayerMessage currentPlayerMessage = (CurrentPlayerMessage) message;
                System.out.println("it' now " + currentPlayerMessage.getCurrentPlayer() + "'s turn");
                //todo: se è il mio turno, devo stampare la stringa gameCommands;
                if(client.getClientID() == currentPlayerMessage.getClientID()){
                    System.out.println(gameCommands);
                }
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
                System.out.println("Now you have to choose between these two objectives. The choice will be your secret objective!\n");
                for(ObjectiveCard c : ((HandObjectiveMessage) message).getCards() ){
                    //TODO: stampare gli obiettivi
                }
                break;
            case GAME_STATE:
                System.out.println("Game state updated to: '" +((GameStateMessage) message).getGameState() +"'\n" );
                break;
            case GRID:
                System.out.println("Grid updated. Here's how: ");
                //TODO: Stampare grid
                break;
            case RESOURCES:
                System.out.println("Your resources count has been updated to: ");
                for(Content c : ((ResourcesMessage) message).getResources().keySet()){
                    System.out.println(c +": "+ ((ResourcesMessage) message).getResources().get(c));
                }
                System.out.println("\n");
                break;
            case WINNER:
                System.out.println("The winner is... " + ((WinnerMessage) message).getNickname() +"! Congrats!!!");
                break;
            case HAND_CARDS:
                System.out.println("Your hand has just been updated. Look at what you have: \n");
                for(PlayableCard c : ((HandCardsMessage) message).getHandCards()){
                    //TODO: Stampare la carta (sia fronte che retro)
                }
                break;
        }
    }

    @Override
    public void handleInput(String input) {
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
                case "getcoloravailable":
                    messageToSend = new GetColorAvailableMessage(client.getClientID());
                    client.sendMessage(messageToSend);
                    break;
                case "choosecolor" :
                    PlayerColor colorChoosen = PlayerColor.valueOf(askForStringInput("What color do you want among those?"));
                    messageToSend = new WantThatColorMessage(client.getClientID(), client.getNickname(), colorChoosen);
                    client.sendMessage(messageToSend);
                    break;
                case "chooseobjectivecard" :
                    //TODO : fare un'altro metodo al posto di askForIntInput che mi permetta di lanciare
                    // un eccezione se l'id della carta non è presente tra le due mostrate
                    int objWanted = askForIntInput("What objective do you prefer?", 1, 120);
                    messageToSend = new ObjectiveCardChosenMessage(client.getClientID(), client.getNickname(), idCard);
                    client.sendMessage(messageToSend);
                case "playcard" :
                    //TODO : Com'è il flusso per la visualizzazione delle coordinate disponibili?
                    //TODO: fare altro metodo al posto di askForIntInput che mi permetta di choosare fra gli
                    //TODO: delle carte in mano, altrimenti da errore.
                    int idCard = askForIntInput("Among the cards in your hand, which one do you want to play?", 1, 120);
                    boolean face = askForFaceToPlay();
                    System.out.println("Give me the coordinates where you want to play the card:");
                    scanner.
                    messageToSend = new PlayCardMessage(client.getClientID(),idCard, face, );
                    break;
                case "drawcard" :

                    break;
                case "pickcard" :

                    break;
                case "skipturn" :

                    break;
            }
        }catch(Exception e) {
            e.printStackTrace();
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
