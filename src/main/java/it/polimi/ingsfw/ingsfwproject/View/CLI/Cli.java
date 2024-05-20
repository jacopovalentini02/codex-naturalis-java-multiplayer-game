package it.polimi.ingsfw.ingsfwproject.View.CLI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.*;

import java.util.*;

import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.ingsfw.ingsfwproject.View.View;

public class Cli extends View implements Runnable {

    Scanner scanner;
    String lobbyCommands = "now you can insert one of the following commands:" + "\n\t-> CreateGame" + "\n\t-> JoinGame" + "\n\t-> GetGameList";
    String gameCommands = "now you can do one of the following actions: \n\t-> PlayCard \n\t-> SkipTurn";
    String drawCommands = "now you can do one of the following actions: \n\t-> DrawCard \n\t-> PickCard";
    String chooseColorCommands = "now you can do one of the following actions: \n\t-> GetColorAvailable \n\t-> ChooseColor";
    String notMyTurnCommands = "now you can do one of the following actions: \n\t-> ShowMyGrid \n\t-> ShowGrid \n\t-> showScores \n\t-> showDisplayed \n\t-> showDecksTop \n\t-> showHand \n\t-> showObjective";
    String chooseObjectiveCommands = "now you can do one of the following actions: \n\t-> ShowObjective \n\t-> chooseObjective";
    String chooseStarterCommands = "now you can do one of the following actions: \n\t-> ShowHand \n\t-> PlayCard";


    public static final String RED = "\033[0;31m";

    public static final String RESET = "\033[0m";

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

        //Thread readMessageThread = new Thread(super::receiveMessage);
        //readMessageThread.start();

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
                scanner.nextLine();
                int y = scanner.nextInt();
                scanner.nextLine();
                coord = new Coordinate(x, y);
                if (!availablePositions.contains(coord)) {
                    System.out.println(errorString);
                }
                System.out.println("coord inserite");
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


    private int askForIntInput(String stringToPrompt, int lowerBound, int upperBound) {
        int choice = lowerBound;
        String errorString = "Error: you have to insert a number between " + lowerBound + " and " + upperBound + "! Retry";

        do {
            try {
                System.out.println(stringToPrompt);
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < lowerBound || choice > upperBound) {
                    System.out.println(errorString);
                }
            } catch (InputMismatchException e) {
                System.out.println(errorString);
                scanner.nextLine(); // Clear the invalid input
            }
        } while (choice < lowerBound || choice > upperBound);

        return choice;
    }

    private int askForIdCardInput(String stringToPrompt, ArrayList<PlayableCard> cards) {
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
                scanner.nextLine();
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
                scanner.nextLine();
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
        } catch (java.lang.Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void displayFirstMessage(int clientID){
        System.out.println("the connection was successfully established, your ClientID is: " + clientID);
        System.out.println(lobbyCommands);
    }

    @Override
    public void displayGameList(HashMap<Integer, Integer> gameList){
        System.out.println("the currently available games are:");
        System.out.println("ID\tNumberOfPlayers");
        for(Integer gameID : gameList.keySet())
            System.out.println(gameID + "\t" + gameList.get(gameID));
        System.out.println(lobbyCommands);
    }

    @Override
    public void notifyGameJoined(int idGame){
        System.out.println("You have joined the game " + idGame+ " correctly.");
        if(client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS)
            System.out.println("Wait for all the players to join the game...");
    }

    @Override
    public void notifyNewPlayerJoined(ArrayList<String> nicknames){
        System.out.println("A new player has joined the game.");
        System.out.println("Players in this lobby: ");

        for (String s : nicknames) {
            System.out.print(s + " ");
        }
        System.out.println();
    }

    @Override
    public void notifyStarterCard(){
        System.out.println("Starter card selected successfully");
    }

    @Override
    public void notifyColorsAvailable(List<PlayerColor> colors){
        System.out.println("The following colors are available :");
        for(PlayerColor color : colors){
            System.out.println(color.name());
        }
    }

    @Override
    public void notifyGoldDeckUpdate(){
        System.out.println("Gold deck updated!");
    }

    @Override
    public void notifyResourceDeckUpdate() {
        System.out.println("Resourced deck updated!");
    }

    @Override
    public void notifyDisplayedCardsUpdate(ArrayList<PlayableCard> displayedCards) {
        System.out.println("Displayed playable cards updated! Here's how:\n");
        //TODO: stampare le carte

    }

    @Override
    public void notifyCurrentPlayer(String nickname){
        System.out.println("it' now " + nickname + "'s turn");
        printAvailableCommands();
    }

    @Override
    public void notifyAvailablePositions(ArrayList<Coordinate> coord) {
        if (coord.isEmpty()){
            System.out.println("You have no more cooedinates available!");
            return;
        }
        System.out.println("your available positions have changed: now you can play a card in the following coordinates:");
        for(Coordinate c : coord)
            System.out.println(c);
    }

    @Override
    public void notifyHandObjectives(ArrayList<ObjectiveCard> cards){
        System.out.println("your objective card has been selected correctly!");
    }

    @Override
    public void notifyGameState(GameState state){
        System.out.println("Game state updated to: '" + state);

    }

    @Override
    public void notifyGridUpdate(String nickname, Map<Coordinate,Face> grid){
        System.out.println(nickname + "'s grid has been updated. Here's how: ");
        //TODO stampare grid
    }

    @Override
    public void notifyResourcesUpdate(String nickname, HashMap<Content, Integer> resources) {
        System.out.println(nickname + "'s resource count has been updated to: ");
        for (Content c : resources.keySet())
            System.out.println(c + ": " + resources.get(c));
        System.out.println("\n");
    }

    @Override
    public void notifyWinnerUpdate(String nick){
        System.out.println("The winner is " + nick + "! Congrats!");
    }

    @Override
    public void notifyHandCardsUpdate(ArrayList<PlayableCard> cards){
        System.out.println("Your hand has just been updated. Look at what you have: \n");
        printPlayerHand();
        //TODO: STAMPARE LE CARTE
    }

    @Override
    public void notifyDisplayedObjectives(List<ObjectiveCard> cards){
        System.out.println("Displayed objective cards updated. Here they are: ");
        for (ObjectiveCard o : cards)
            System.out.print(o.getIdCard() + " ");
        System.out.println("\n");
    }

    @Override
    public void notifyScores(Map<String, Integer> scores){
        printScores();
    }

    @Override
    public void notifyColorChosen(PlayerColor color){
        System.out.println(color + " successfully set. ");
    }

    public void printAvailableCommands(){
        if(!client.getVirtualView().getCurrentPlayer().equals(client.getNickname())){ //non è il mio turno
            if(client.getVirtualView().getState() == GameState.STARTED)
                System.out.println(notMyTurnCommands);
        }else { // è il mio turno
            switch (client.getVirtualView().getState()){
                case GameState.CHOOSING_STARTER_CARDS:
                    System.out.println(chooseStarterCommands);
                    break;
                case GameState.CHOOSING_OBJECTIVES:
                    System.out.println(chooseObjectiveCommands);
                    break;
                case GameState.CHOOSING_COLORS:
                    System.out.println(chooseColorCommands);
                    break;
                case GameState.STARTED:
                    if(!client.getVirtualView().isCurrentPlayerhasPlayed()){ //se non ho ancora giocato
                        System.out.println(gameCommands);
                    }else {
                        System.out.println(drawCommands);
                    }
            }

        }
    }


    @Override
    public void notifyChatMessage(ChatMessage message){
        System.out.println(RED + "New chat message from " + message.getSender() + " : " + message.getMessage() + RESET);
    }

    //TODO: rimuovere
    @Override//TODO: stampare gli obiettivi
    public void handleMessage(Message message) {
        String lobbyCommands = "now you can insert one of the following commands:" + "\n\t-> CreateGame" + "\n\t-> JoinGame" + "\n\t-> GetGameList";
        //TODO : Forse conviene gestire questo output in base al game state -> se è in choosing colors mando gli ultimi due,
        // se è in choose objectives solo il primo
        String GameStartingCommands = "now you can do one of the following actions: \n\t-> ChooseObjectiveCard \n\t-> getColorAvailable \n\t-> chooseColor";
        String gameCommands = "now you can do one of the following actions: \n\t-> PlayCard \n\t-> DrawCard \n\t-> PickCard  \n\t-> SkipTurn";

    }


    public void handleInput(String input) {
        Message messageToSend;
        String name;
        input = input.toLowerCase();

        try {
            switch (input) {
                case "creategame":
                    int numOfPlayers = askForIntInput("insert the number of players between 2 and 4", 2, 4);
                    name = askForStringInput("insert your nickname");
                    messageToSend = new CreateGameMessage(client.getClientID(), numOfPlayers, name);
                    client.sendMessage(messageToSend);
                    break;
                case "joingame":
                    int gameID = askForIntInput("insert the game ID", 0, 1000);
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
                case "choosecolor":
                    PlayerColor colorChoosen = PlayerColor.valueOf(askForStringInput("What color do you want among those?").toUpperCase());
                    messageToSend = new WantThatColorMessage(client.getClientID(), client.getNickname(), colorChoosen);
                    client.sendMessage(messageToSend);
                    break;
                case "chooseobjective":
                    int objWanted = askForIdObjectiveInput("What objective do you prefer?", (client.getVirtualView().getHandObjectives()));
                    messageToSend = new ObjectiveCardChosenMessage(client.getClientID(), client.getNickname(), objWanted);
                    client.sendMessage(messageToSend);
                    break;
                case "playcard":
                    int idCard = askForIdCardInput("Among the cards in your hand, which one do you want to play? Here the cards' id:", client.getVirtualView().getHandCards());
                    boolean face = askForFaceToPlay();
                    Coordinate coords = askForCoordinateInput(client.getVirtualView().getAvailablePositions());
                    messageToSend = new PlayCardMessage(client.getClientID(), idCard, face, coords, client.getNickname());
                    System.out.println(client.getNickname());
                    client.sendMessage(messageToSend);
                    break;
                case "drawcard":
                    //defalt value for deckWanted
                    boolean deckWanted = true;
                    int idDeck = askForIntInput("From which deck you want to draw?\n1)Resource deck\n2)Gold deck", 1, 2);
                    switch (idDeck) {
                        case 1 -> deckWanted = true;
                        case 2 -> deckWanted = false;
                    }
                    messageToSend = new DrawMessage(client.getClientID(), client.getNickname(), deckWanted);
                    client.sendMessage(messageToSend);
                    break;
                case "pickcard":
                    idCard = askForIdCardInput("Among those, which card do you want?", client.getVirtualView().getDisplayedCards());
                    messageToSend = new PickMessage(client.getClientID(), idCard, client.getNickname());
                    client.sendMessage(messageToSend);
                    break;
                case "skipturn":
                    messageToSend = new SkipTurnMessage(client.getClientID(), client.getNickname());
                    client.sendMessage(messageToSend);
                    break;
                case "showgrid":
                    //todo: chiedere di chi e stampare la relativa grid
                    Set<String> otherPlayersNick = client.getVirtualView().getScores().keySet();
                    otherPlayersNick.remove(client.getNickname());
                    String playerAsked;
                    System.out.println("Among the fields of the players, which one do you want to look?");
                    for (String p : otherPlayersNick) {
                        System.out.print(p + " ");
                        System.out.println("");
                    }
                    do {
                        playerAsked = scanner.nextLine();
                        if(!otherPlayersNick.contains(playerAsked)){
                            System.out.println("The requested player doesn't exist! Retry!");
                        }
                    } while (!otherPlayersNick.contains(playerAsked));
                    //TODO : Inserire qua il metodo di stampa della grid del player
                    break;
                case "showmygrid":
                    //TODO: STAMPARE DIRETTAMENTE GRID DEL PLAYER RICHIEDENTE
                    break;
                case "showscores":
                    printScores();
                    break;
                case "showdisplayed":
                    //todo: cambiare il metodo di stampa delle carte -> stampa lines in grid
                    for (PlayableCard c : client.getVirtualView().getDisplayedCards()) {
                        System.out.println(printFacePlayed(c.getFront()));
                    }
                    break;
                case "showdekstop":
                    //todo: cambiare il metodo di stampa delle carte -> stampa lines in grid
                    System.out.println("The back of the top card of the resource deck is: ");
                    System.out.println(printFacePlayed(((ResourceCard) client.getVirtualView().getResourceDeck().getCardList().getFirst()).getBack()));
                    System.out.println("The back of the top card of the gold deck is: ");
                    System.out.println(printFacePlayed(((GoldCard) client.getVirtualView().getGoldDeck().getCardList().getFirst()).getBack()));
                    break;
                case "showhand":
                    printPlayerHand();
                    break;
                case "showobjective":
                    printObjectiveCards();
                    break;
                case "globalchat":
                    for (ChatMessage m : client.getVirtualView().getGlobalChat()) {
                        System.out.println(RED + m.getSender() + ": " + m.getMessage() + RESET);
                    }
                    break;
                case "chat":
                    String playerNick = askForStringInput("Which chat you want to show? Type the nick of the other player: " + printOtherPlayers());
                    if (client.getVirtualView().getPrivateChat(playerNick) != null){
                        for (ChatMessage m: client.getVirtualView().getPrivateChat(playerNick))
                            System.out.println(RED + m.getSender() + ": " + m.getMessage() + RESET);
                    } else {
                        System.out.println("There's no such player");
                    }
                    break;
                case "sendmessage":
                    String recipient = askForStringInput("Who do you want to send the message to? Type the nick of the other player, global for globalchat: " + printOtherPlayers());
                    String chatMessage = askForStringInput("Type your message: ");
                    client.getVirtualView().sendPrivateMessage(new ChatMessage(client.getNickname(), recipient, chatMessage));
                    client.sendMessage(new sendChatMessage(client.getClientID(), client.getNickname(), recipient, chatMessage));
                    break;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String printOtherPlayers(){
        String toReturn = "";
        for (String s : client.getVirtualView().getListOfPlayers()){
            if (!(s.equals(client.getNickname())))
               toReturn = toReturn.concat(s + " ");
        }
        return toReturn;
    }

    @Override
    public void notifyException(String message) {
        System.out.println(message);
    }


   /* public void printGrid(PlayerGround ground) {
        int minX = getMinX(ground.getGrid().keySet());
        int maxY = getMaxY(ground.getGrid().keySet());
        Iterator<Map.Entry<Coordinate, Face>> iterator = ground.topToBottomIterator();
        StringBuffer buffer = new StringBuffer();
        while (iterator.hasNext()) {
            Map.Entry<Coordinate, Face> entry = iterator.next(); // Prendi la prossima entry una volta per iterazione
            int x = entry.getKey().getX();
            int y = entry.getKey().getY();
            int xspace = ((x - minX) * 6 - minX-1) * 3;
            int yspace = (maxY - y) * 2 + 1;
            buffer.append(faceInGrid(entry.getValue(), printSpace(xspace)));
        }
        System.out.print(buffer.toString());
    }*/
   public void printGrid(PlayerGround ground) {
       int minX = getMinX(ground.getGrid().keySet());
       int maxX = getMaxX(ground.getGrid().keySet());
       int minY = getMinY(ground.getGrid().keySet());
       int maxY = getMaxY(ground.getGrid().keySet());

       int width = ((maxX - minX) * 5 + 6) * 3;
       int height = (maxY - minY) * 2 + 3;

       // Initialize the buffer with empty spaces
       StringBuilder buffer = new StringBuilder();
       for (int i = 0; i < height; i++) {
           for (int j = 0; j < width; j++) {
               buffer.append(" ");
           }
           buffer.append("\n");
       }

       System.out.println("Buffer initialized with width: " + width + " and height: " + height);

       Iterator<Map.Entry<Coordinate, Face>> iterator = ground.topToBottomIterator();
       while (iterator.hasNext()) {
           Map.Entry<Coordinate, Face> entry = iterator.next();
           int x = entry.getKey().getX();
           int y = entry.getKey().getY();
           int xspace = ((x - minX) * 6 - x) * 3;
           int yspace = (maxY - y) * 2 + 1;
           System.out.println("Placing face at xspace: " + xspace + ", yspace: " + yspace);
           replaceInBuffer(buffer, entry.getValue(), xspace, yspace, width);
       }

       System.out.print(buffer.toString());
   }

    // Helper method to replace part of the buffer with the card's face
    private void replaceInBuffer(StringBuilder buffer, Face face, int xspace, int yspace, int width) {
       int i;
        String card = printFacePlayed(face);
        String[] cardLines = card.split("\n");
        String[] lines = new String[3];
        for(i=0; i<cardLines.length; i++){
            lines[i] = cardLines[i]+"\n";
        }
        for(i=0; i<lines.length; i++){
            int startIndex = (yspace+i)*(width+1)+xspace;
            for(int j=0; j<lines[i].length(); j++){
                buffer.setCharAt(startIndex, lines[i].charAt(j));
                startIndex++;
            }
        }
    }

    // Helper methods for calculating grid boundaries
    public int getMinX(Set<Coordinate> coords){
        int min = Integer.MAX_VALUE;
        for (Coordinate coord : coords) {
            if (coord.getX() < min) {
                min = coord.getX();
            }
        }
        return min;
    }

    public int getMaxX(Set<Coordinate> coords) {
        int max = Integer.MIN_VALUE;
        for (Coordinate coord : coords) {
            if (coord.getX() > max) {
                max = coord.getX();
            }
        }
        return max;
    }

    public int getMinY(Set<Coordinate> coords) {
        int min = Integer.MAX_VALUE;
        for (Coordinate coord : coords) {
            if (coord.getY() < min) {
                min = coord.getY();
            }
        }
        return min;
    }

    public int getMaxY(Set<Coordinate> coords) {
        int max = Integer.MIN_VALUE;
        for (Coordinate coord : coords) {
            if (coord.getY() > max) {
                max = coord.getY();
            }
        }
        return max;
    }

    public String printFacePlayed(Face face) {
        StringBuilder builder = new StringBuilder();
        AnsiColor cardType = getCardType(face);
        int i;

        // First row
        builder.append(printCorner(face.getTL(), 0, cardType));
        builder.append(cardType.getFormattedCharacter());
        if (face instanceof GoldFront) {
            builder.append(printGoldFrontPoints((GoldFront) face, cardType));
            builder.append(printCorner(((GoldFront) face).getObjectNeeded(), 0, cardType));
        } else {
            for (i = 0; i < 2; i++) {
                builder.append(cardType.getFormattedCharacter());
            }
        }
        builder.append(printCorner(face.getTR(), 1, cardType));
        builder.append("\n");

        // Second row
        if (face instanceof GoldFront) {
            builder.append(printGoldBorder(cardType, 0));
        } else {
            builder.append(cardType.getFormattedCharacter());
        }
        builder.append(cardType.getFormattedCharacter());
        if (face instanceof NormalBack) {
            switch (((NormalBack) face).getCenter()) {
                case FUNGI_KINGDOM -> builder.append(AnsiColor.FUNGI_TEXT.getFormattedCharacter());
                case PLANT_KINGDOM -> builder.append(AnsiColor.PLANT_TEXT.getFormattedCharacter());
                case INSECT_KINGDOM -> builder.append(AnsiColor.INSECT_TEXT.getFormattedCharacter());
                case ANIMAL_KINGDOM -> builder.append(AnsiColor.ANIMAL_TEXT.getFormattedCharacter());
            }
        } else {
            builder.append(cardType.getFormattedCharacter());
        }
        builder.append(cardType.getFormattedCharacter());
        if (face instanceof GoldFront) {
            builder.append(printGoldBorder(cardType, 1));
        } else {
            builder.append(cardType.getFormattedCharacter());
        }
        builder.append("\n");

        // Third row
        builder.append(printCorner(face.getBL(), 0, cardType));
        for (i = 0; i < 3; i++) {
            builder.append(cardType.getFormattedCharacter());
        }
        builder.append(printCorner(face.getBR(), 1, cardType));
        builder.append("\n");

        return builder.toString();
    }

    public static String printGoldFrontPoints(GoldFront face, AnsiColor cardType) {
        StringBuilder result = new StringBuilder();

        switch (face.getPoints()) {
            case 1 -> result.append(AnsiColor.POINT_ONE.getFormattedCharacter());
            case 2 -> result.append(AnsiColor.POINT_TWO.getFormattedCharacter());
            case 3 -> result.append(AnsiColor.POINT_THREE.getFormattedCharacter());
        }

        if (face.getObjectNeeded() != null) {
            result.append(printCorner(face.getObjectNeeded(), 0, cardType));
        } else {
            result.append(cardType.getFormattedCharacter());
        }

        return result.toString();
    }

    public static String printGoldBorder(AnsiColor cardType, int bORe) {
        StringBuilder result = new StringBuilder();

        if (bORe == 0) {
            switch (cardType) {
                case PLANT_BACKGROUND -> result.append(AnsiColor.B_GOLD_PLANT_BACKGROUND.getFormattedCharacter());
                case ANIMAL_BACKGROUND -> result.append(AnsiColor.B_GOLD_ANIMAL_BACKGROUND.getFormattedCharacter());
                case INSECT_BACKGROUND -> result.append(AnsiColor.B_GOLD_INSECT_BACKGROUND.getFormattedCharacter());
                case FUNGI_BACKGROUND -> result.append(AnsiColor.B_GOLD_FUNGI_BACKGROUND.getFormattedCharacter());
            }
        } else if (bORe == 1) {
            switch (cardType) {
                case PLANT_BACKGROUND -> result.append(AnsiColor.E_GOLD_PLANT_BACKGROUND.getFormattedCharacter());
                case ANIMAL_BACKGROUND -> result.append(AnsiColor.E_GOLD_ANIMAL_BACKGROUND.getFormattedCharacter());
                case INSECT_BACKGROUND -> result.append(AnsiColor.E_GOLD_INSECT_BACKGROUND.getFormattedCharacter());
                case FUNGI_BACKGROUND -> result.append(AnsiColor.E_GOLD_FUNGI_BACKGROUND.getFormattedCharacter());
            }
        }

        return result.toString();
    }

    public static String printCorner(Content content, int rOrl, AnsiColor cardType) {
        StringBuilder result = new StringBuilder();

        if (rOrl == 0) {
            switch (content) {
                case FUNGI_KINGDOM -> result.append(AnsiColor.FUNGI_TEXT.getFormattedCharacter());
                case PLANT_KINGDOM -> result.append(AnsiColor.PLANT_TEXT.getFormattedCharacter());
                case INSECT_KINGDOM -> result.append(AnsiColor.INSECT_TEXT.getFormattedCharacter());
                case ANIMAL_KINGDOM -> result.append(AnsiColor.ANIMAL_TEXT.getFormattedCharacter());
                case EMPTY -> result.append(AnsiColor.EMPTY_TEXT.getFormattedCharacter());
                case HIDDEN -> result.append(cardType.getFormattedCharacter());
                case QUILL -> result.append(AnsiColor.QUILL_TEXT.getFormattedCharacter());
                case MANUSCRIPT -> result.append(AnsiColor.MANUSCRIPT_TEXT.getFormattedCharacter());
                case INKWELL -> result.append(AnsiColor.INKWELL_TEXT.getFormattedCharacter());
            }
        } else {
            switch (content) {
                case FUNGI_KINGDOM -> result.append(AnsiColor.FUNGI_TEXT.getFormattedCharacter());
                case PLANT_KINGDOM -> result.append(AnsiColor.PLANT_TEXT.getFormattedCharacter());
                case INSECT_KINGDOM -> result.append(AnsiColor.INSECT_TEXT.getFormattedCharacter());
                case ANIMAL_KINGDOM -> result.append(AnsiColor.ANIMAL_TEXT.getFormattedCharacter());
                case EMPTY -> result.append(AnsiColor.EMPTY_TEXT.getFormattedCharacter());
                case HIDDEN -> result.append(cardType.getFormattedCharacter());
                case QUILL -> result.append(AnsiColor.QUILL_TEXT.getFormattedCharacter());
                case MANUSCRIPT -> result.append(AnsiColor.MANUSCRIPT_TEXT.getFormattedCharacter());
                case INKWELL -> result.append(AnsiColor.INKWELL_TEXT.getFormattedCharacter());
            }
        }

        return result.toString();
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

    public void printPlayerHand(){

        int i =1;
        for (PlayableCard c :client.getVirtualView().getHandCards()){
            System.out.println("Front of card "+ i + ":");
            System.out.println(printFacePlayed(c.getFront()));
            if(c.getFront() instanceof GoldFront){
                System.out.print("Costs: ");
                for(Content cost : ((GoldFront)c.getFront()).getCost()){
                    System.out.print(cost.toString() +" ");
                }
                System.out.println();
            }
            System.out.println("Back of card "+ i + ":");
            System.out.println(printFacePlayed(c.getBack()));
            i++;
        }
        System.out.println();
    }

    public void printObjectiveCards(){
        //TODO: stampare la carta effettiva e non l'ID
        for (ObjectiveCard oc: client.getVirtualView().getHandObjectives())
            System.out.println(oc.getIdCard());
    }

    public void printScores(){
        System.out.println("There's been a change in scores. The new ones are: ");
        for (Map.Entry<String, Integer> e : client.getVirtualView().getScores().entrySet())
            System.out.println(e.getKey() + ": " + e.getValue());
    }
}
