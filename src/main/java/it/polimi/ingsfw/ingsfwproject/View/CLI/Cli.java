package it.polimi.ingsfw.ingsfwproject.View.CLI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;

import java.io.IOException;
import java.util.*;

import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.ingsfw.ingsfwproject.View.View;

public class Cli extends View implements Runnable {

    Scanner scanner;

    public Cli(){
        this.scanner = new Scanner(System.in);
        super.messages = new LinkedBlockingQueue<>();
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

    private Coordinate askForCoordinateInput(ArrayList<Coordinate> availablePositions) {
        Coordinate coord = new Coordinate(-9999, -9999);
        String errorString = "Invalid coordinates! Please insert valid ones.\n";
        System.out.println("Choose the coordinates where you want to play the card between these:");
        for (Coordinate c : availablePositions) {
            System.out.println("[" + c.getX() + ", " + c.getY() + "]");
        }
        do {
            try {
                System.out.println("Insert X and Y coordinates:");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                coord = new Coordinate(x, y);
                if (!availablePositions.contains(coord)) {
                    System.out.println(errorString);
                }
            } catch (InputMismatchException e) {
                System.out.println(errorString);
                scanner.nextLine();
            }
        } while (!availablePositions.contains(coord));

        return coord;
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

    private int askForIdCardInput(String stringToPrompt, ArrayList<PlayableCard> cards){
        int choice = -1;
        Set<Integer> idCards = new HashSet<>();
        for(PlayableCard c : cards){
            idCards.add(c.getIdCard());
        }
        System.out.println(stringToPrompt);
        String errorString = "Error: you have to insert a card id among the displayed cards!\n";
        do{
            try{
                for(PlayableCard c : cards){
                    System.out.println(c.getIdCard());
                    printFacePlayed(c.getFront());
                }
                System.out.println("Insert the id of the card chosen: ");
                choice = scanner.nextInt();
                if (!idCards.contains(choice)){
                    System.out.println(errorString);
                }
            }catch (InputMismatchException e){
                System.out.println(errorString);
            }
        }while(!idCards.contains(choice));
        return choice;
    }
    private int askForIdObjectiveInput(String stringToPrompt, ArrayList<ObjectiveCard> cards){
        int choice = -1;
        Set<Integer> idCards = new HashSet<>();
        for(Card c : cards){
            idCards.add(c.getIdCard());
        }
        System.out.println(stringToPrompt);
        String errorString = "Error: you have to insert a card id among the displayed cards!\n";
        do{
            try{
                for(Card c : cards){
                    System.out.println(c.getIdCard());
                    //TODO: Stampare fronte delle carte date come parametro d'ingresso
                }
                System.out.println("Insert the id of the card chosen: ");
                choice = scanner.nextInt();
                if (!idCards.contains(choice)){
                    System.out.println(errorString);
                }
            }catch (InputMismatchException e){
                System.out.println(errorString);
            }
        }while(!idCards.contains(choice));
        return choice;
    }

    private String askForStringInput(String stringToPrompt){
        System.out.println(stringToPrompt);
        return scanner.nextLine();
    };


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
    public void handleMessage(Message message) {
        String lobbyCommands = "now you can insert one of the following commands:" + "\n\t-> CreateGame" + "\n\t-> JoinGame" + "\n\t-> GetGameList";
        //TODO : Forse conviene gestire questo output in base al game state -> se è in choosing colors mando gli ultimi due,
        // se è in choose objectives solo il primo
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
                //todo: se è il mio turno, devo stampare la stringa gameCommands; <-> Non sono sicuro di averlo gestito bene
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
                //TODO: Se il game è nelle fasi iniziali devo poter stampare i relativi comandi
                if(client.getVirtualView().getState()==GameState.CHOOSING_STARTER_CARDS){
                    System.out.println("now you can do the following actions: \n\t-> PlayCard");
                }
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
                    int objWanted = askForIdObjectiveInput("What objective do you prefer?", (client.getVirtualView().getHandObjectives()));
                    messageToSend = new ObjectiveCardChosenMessage(client.getClientID(), client.getNickname(), objWanted);
                    client.sendMessage(messageToSend);
                case "playcard" :
                    int idCard = askForIdCardInput("Among the cards in your hand, which one do you want to play? Here the cards' id:\n", client.getVirtualView().getHandCards());
                    boolean face = askForFaceToPlay();
                    Coordinate coords = askForCoordinateInput(client.getVirtualView().getAvailablePositions());
                    messageToSend = new PlayCardMessage(client.getClientID(),idCard, face,coords, client.getNickname());
                    client.sendMessage(messageToSend);
                    break;
                case "drawcard" :
                    //defalt value for deckWanted
                    boolean deckWanted = true;
                    int idDeck = askForIntInput("From which deck you want to draw?\n1)Resource deck\n2)Gold deck", 1, 2);
                    switch(idDeck){
                        case 1 -> deckWanted = true;
                        case 2 -> deckWanted = false;
                    }
                    messageToSend = new DrawMessage(client.getClientID(), client.getNickname(), deckWanted);
                    client.sendMessage(messageToSend);
                    break;
                case "pickcard" :
                    idCard = askForIdCardInput("Among those, which card do you want?", client.getVirtualView().getDisplayedCards());
                    messageToSend = new PickMessage(client.getClientID(), idCard, client.getNickname());
                    client.sendMessage(messageToSend);
                    break;
                case "skipturn" :
                    messageToSend = new SkipTurnMessage(client.getClientID(), client.getNickname());
                    client.sendMessage(messageToSend);
                    break;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void printGrid(PlayerGround playerGround) {

    }
    public void printFacePlayed(Face face){
        int i;
        AnsiColor cardType = getCardType(face);
        //TODO: PRIMA RIGA!
        //upper-left corner
        printCorner(face.getTL(), 0, cardType);
        //space
        System.out.print(cardType.getFormattedCharacter());
        if(face instanceof GoldFront){
            printGoldFrontPoints(((GoldFront) face), cardType);
        }
        else{
            for(i=0; i<2; i++){
                System.out.print(cardType.getFormattedCharacter());
            }
        }
        //upper-right corner
        printCorner(face.getTR(), 1, cardType);
        //TODO: SECONDA RIGA!
        if(face instanceof GoldFront){
            printGoldBorder(cardType, 0);
        }
        else{
            System.out.print(cardType.getFormattedCharacter());
        }
        System.out.print(cardType.getFormattedCharacter());
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
        System.out.print(cardType.getFormattedCharacter());
        if(face instanceof GoldFront){
            printGoldBorder(cardType, 1);
        }
        else{
            System.out.println(cardType.getFormattedCharacter());
        }
        //TODO : TERZA RIGA
        //lower-left corner
        printCorner(face.getBL(), 0, cardType);
        //space
        for (i=0; i<3; i++){
            System.out.print(cardType.getFormattedCharacter());
        }
        //lower-right corner
        printCorner(face.getBR(), 1, cardType);
    }

    public void printGoldFrontPoints(GoldFront face, AnsiColor cardType){
        switch(face.getPoints()){
            case 1 -> System.out.print(AnsiColor.POINT_ONE.getFormattedCharacter());
            case 2 -> System.out.println(AnsiColor.POINT_TWO.getFormattedCharacter());
            case 3 -> System.out.println(AnsiColor.POINT_THREE.getFormattedCharacter());
        }
        if(face.getObjectNeeded() != null){
            printCorner(face.getObjectNeeded(),0, cardType);
        }
        else{
            System.out.print(cardType.getFormattedCharacter());
        }
    }

    public void printGoldBorder(AnsiColor cardType, int bORe){
        if(bORe ==0){
            switch(cardType){
                case PLANT_BACKGROUND -> System.out.print(AnsiColor.B_GOLD_PLANT_BACKGROUND);
                case ANIMAL_BACKGROUND -> System.out.print(AnsiColor.B_GOLD_ANIMAL_BACKGROUND);
                case INSECT_BACKGROUND -> System.out.print(AnsiColor.B_GOLD_INSECT_BACKGROUND);
                case FUNGI_BACKGROUND -> System.out.print(AnsiColor.B_GOLD_FUNGI_BACKGROUND);
            }
        }
        if(bORe == 1){
            switch(cardType){
                case PLANT_BACKGROUND -> System.out.println(AnsiColor.E_GOLD_PLANT_BACKGROUND);
                case ANIMAL_BACKGROUND -> System.out.println(AnsiColor.E_GOLD_ANIMAL_BACKGROUND);
                case INSECT_BACKGROUND -> System.out.println(AnsiColor.E_GOLD_INSECT_BACKGROUND);
                case FUNGI_BACKGROUND -> System.out.println(AnsiColor.E_GOLD_FUNGI_BACKGROUND);
            }
        }
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
        assert background != null;
        return switch(background){
            case FUNGI_KINGDOM -> AnsiColor.FUNGI_BACKGROUND;
            case PLANT_KINGDOM ->  AnsiColor.PLANT_BACKGROUND;
            case INSECT_KINGDOM -> AnsiColor.INSECT_BACKGROUND;
            case ANIMAL_KINGDOM -> AnsiColor.ANIMAL_BACKGROUND;
            case QUILL -> AnsiColor.EMPTY_TEXT;
            case INKWELL -> AnsiColor.EMPTY_TEXT;
            case MANUSCRIPT -> AnsiColor.EMPTY_TEXT;
            case EMPTY -> AnsiColor.EMPTY_TEXT;
            case HIDDEN -> AnsiColor.EMPTY_TEXT;
        };
    }

    public void printPlayerHand(Player player){
        int i =1;
        for (PlayableCard c :player.getHandCard()){
            System.out.println("Front of card "+ i + ":");
            printFacePlayed(c.getFront());
            if(c.getFront() instanceof GoldFront){
                System.out.print("Costs: ");
                for(Content cost : ((GoldFront)c.getFront()).getCost()){
                    System.out.print(cost.toString() +" ");
                }
                System.out.println("");
            }
            System.out.println("Back of card "+ i + ":");
            printFacePlayed(c.getBack());
            i++;
        }
    }
}
