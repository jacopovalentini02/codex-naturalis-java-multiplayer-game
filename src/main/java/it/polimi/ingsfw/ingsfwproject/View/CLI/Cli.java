package it.polimi.ingsfw.ingsfwproject.View.CLI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.net.ConnectException;
import java.util.*;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

import it.polimi.ingsfw.ingsfwproject.View.View;

public class Cli extends View implements Runnable {

    Scanner scanner;

    public static final String GRAY = "\u001B[37m";

    public static final String RESET = "\033[0m";

    public Cli(){
        this.scanner = new Scanner(System.in);
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

    }

    private void readInputUser(){
        while(true){
            handleInput(scanner.nextLine());
        }
    }

    private Coordinate askForCoordinateInput(ArrayList<Coordinate> availablePositions) {
        Coordinate coord = new Coordinate(Integer.MIN_VALUE, Integer.MIN_VALUE);
        String errorString = "Invalid coordinates! Please insert valid ones.\n";
        System.out.println("Choose the coordinates where you want to play the card between these:");
        for (Coordinate c : availablePositions) {
            System.out.println("[" + c.getX() + ", " + c.getY() + "]");
        }
        do {
            try {
                System.out.println("Insert X and Y coordinates:");
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a number!");
                    scanner.next(); // this is important!
                }
                int x = scanner.nextInt();
                scanner.nextLine();
                System.out.println("x registered");
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a number!");
                    scanner.next(); // this is important!
                }
                int y = scanner.nextInt();
                scanner.nextLine();
                coord = new Coordinate(x, y);
                if (!availablePositions.contains(coord)) {
                    System.out.println(errorString);
                }
                System.out.println("coord inserted");
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
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a number!");
                    scanner.next(); // this is important!
                }
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < lowerBound || choice > upperBound) {
                    System.out.println(errorString);
                }
            } catch (InputMismatchException e) {
                System.out.println(errorString);
                scanner.nextLine(); // Clear the invalid input
            }
        } while(choice < lowerBound || choice > upperBound);

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
                printListOfCards(cards,true,true,true,true);
                System.out.println("Insert the id of the card chosen: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a number!");
                    scanner.next(); // this is important!
                }
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
                   printObjectiveCards((ObjectiveCard) c);
                }
                System.out.println("Insert the id of the card chosen: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a number!");
                    scanner.next(); // this is important!
                }
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

        String ip = askForStringInput("Insert the IP address: ");
        while(!(validate(ip) || ip.equals("localhost"))) {
            ip = askForStringInput("IP address not valid! Insert the IP address: ");
        }
        try {
            int port = choice == 1? 1337 : 1099;
            super.client = choice == 1 ? new SocketClient(ip, port, this) : new RMIClient(ip, port, this);
            client.startConnection();
        } catch (java.lang.Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void notifyNewPlayerJoined(ArrayList<String> nicknames){}

    @Override
    public void notifyGoldDeckUpdate(){}

    @Override
    public void notifyResourceDeckUpdate() {}

    @Override
    public void notifyDisplayedCardsUpdate(ArrayList<PlayableCard> displayedCards) {}

    @Override
    public void notifyAvailablePositions(ArrayList<Coordinate> coord) {}
    @Override
    public void notifyGridUpdate(String nickname, Map<Coordinate,Face> grid){}

    @Override
    public void notifyResourcesUpdate(String nickname, HashMap<Content, Integer> resources) {}

    @Override
    public void notifyHandCardsUpdate(ArrayList<PlayableCard> cards){}

    @Override
    public void notifyDisplayedObjectives(List<ObjectiveCard> cards){}

    @Override
    public void displayFirstMessage(int clientID){
        System.out.println("the connection was successfully established, your ClientID is: " + clientID);
        printAvailableCommands();
    }

    @Override
    public void displayGameList(HashMap<Integer, Integer> gameList){
        if(gameList.isEmpty()){
            System.out.println("There are no game available!");
            printAvailableCommands();
            return;
        }
        System.out.println("the currently available games are:");
        System.out.println("ID\tNumberOfPlayers");
        for(Integer gameID : gameList.keySet())
            System.out.println(gameID + "\t" + gameList.get(gameID));
        printAvailableCommands();
    }

    @Override
    public void notifyGameJoined(int idGame){
        System.out.println("You have joined the game " + idGame+ " correctly.");
        System.out.println("Wait for all the players to join the game...");
    }


    @Override
    public void notifyStarterCard(){
        System.out.println("Starter card selected successfully");
    }

    @Override
    public void notifyColorsAvailable(List<PlayerColor> colors){
        System.out.println("The following colors are available :");
        for(PlayerColor color : colors){
            System.out.print(color.name() + ": ");
            switch(color){
                case RED -> System.out.println(AnsiColor.RED_DOT.getFormattedCharacter());
                case BLUE -> System.out.println(AnsiColor.BLUE_DOT.getFormattedCharacter());
                case GREEN -> System.out.println(AnsiColor.GREEN_DOT.getFormattedCharacter());
                case YELLOW -> System.out.println(AnsiColor.YELLOW_DOT.getFormattedCharacter());
            }
        }
    }


    @Override
    public void notifyCurrentPlayer(String nickname){
        //TODO: QUESTO FUNZIONA SOLO NEL TERMINALE, NON NELL'IDE! A JAR CREATO CONTROLLARE CHE FUNZIONI
        String clearcli = "\033[H\033[2J";
        System.out.println(clearcli);
        System.out.flush();

        if(!nickname.equals(client.getNickname()))
            printWithNicksColorNewLine("it' now " + nickname + "'s turn", nickname);
        else
            printWithNicksColorNewLine("it' now your turn", nickname);
        printAvailableCommands();
    }


    @Override
    public void notifyHandObjectives(ArrayList<ObjectiveCard> cards){
        if(cards.size() == 1)
            System.out.println("your objective card has been selected correctly!");
    }

    @Override
    public void notifyGameState(GameState state){
        if(!state.equals(client.getVirtualView().getState()))
            System.out.println("Game state updated to: '" + state +"'.");
        if(state.equals(GameState.ENDING)){
            System.out.println("it's your last turn!");
        }
        if(state.equals(GameState.ENDED))
            printAvailableCommands();
    }

    @Override
    public void notifyWinnerUpdate(String nick){
        if (nick.equals("tie")){
            System.out.println("It's a tie!");
        } else {
            printWithNicksColorNewLine("The winner is " + nick + "! Congrats!", nick);
        }
    }


    @Override
    public void notifyScores(Map<String, Integer> scores){
        if(client.getVirtualView().getState()!=GameState.ENDING)
            return;

        printScores();
    }

    @Override
    public void notifyColorChosen(PlayerColor color){
        System.out.println(color + " successfully set. ");
    }

    @Override
    public void notifyCurrentPlayerHasPlayed(boolean currentPlayerHasPlayed){
        if(currentPlayerHasPlayed) {
            System.out.println("Your card has been played!");
            printAvailableCommands();
        }
    }

    @Override
    public void notifyChatMessage(ChatMessage message){
        System.out.println(GRAY + "New chat message from " + getColoredNick(message.getSender()) + " : " + message.getMessage() + RESET);
    }

    @Override
    public void notifyException(String message) {
        System.out.println(message);
    }

    public void printAvailableCommands(){
        String startingString = "now you can insert one of the following commands:";
        String separatorString = "\nand";

        String lobbyCommands = "\n\t-> CreateGame" + "\n\t-> JoinGame" + "\n\t-> GetGameList";
        String chooseColorCommands = "\n\t-> GetColorAvailable \n\t-> ChooseColor";
        String chooseObjectiveCommands = "\n\t-> chooseObjective";
        String gameCommands = "\n\t-> PlayCard";
        String drawCommands = "\n\t-> DrawCard \n\t-> PickCard";
        String showCommands = "\n\t-> ShowGrid \n\t-> ShowMyGrid \n\t-> showCounters \n\t-> showDisplayed \n\t-> showDecksTop \n\t-> showHand \n\t-> showScores";
        String showObjectiveCommands = "\n\t-> ShowObjective";
        String chatCommands = "\n\t-> globalChat \n\t-> chat \n\t-> sendMessage";

        String helpCommand = "\n\t-> ? ";

        String notMyTurnCommands = showCommands + separatorString + chatCommands;

        GameState currentState = client.getVirtualView().getState();

        if(currentState == null) { //not connected yet
            System.out.println(startingString + lobbyCommands + helpCommand);
            return;
        }

        if(currentState.equals(GameState.WAITING_FOR_PLAYERS)){
            System.out.println(startingString + chatCommands + helpCommand);
            return;
        }

        boolean isMyTurn = client.getVirtualView().getCurrentPlayer().equals(client.getNickname());

        switch(currentState){
            case GameState.CHOOSING_STARTER_CARDS :
                if(isMyTurn)
                    System.out.println(startingString + gameCommands + separatorString + notMyTurnCommands + helpCommand);
                else
                    System.out.println(startingString + notMyTurnCommands + helpCommand);
                break;
            case GameState.CHOOSING_COLORS :
                if(isMyTurn)
                    System.out.println(startingString + chooseColorCommands + separatorString + notMyTurnCommands + helpCommand);
                else
                    System.out.println(startingString + notMyTurnCommands + helpCommand);
                break;
            case GameState.CHOOSING_OBJECTIVES :
                if(isMyTurn)
                    System.out.println(startingString + showObjectiveCommands + chooseObjectiveCommands+ separatorString + notMyTurnCommands + helpCommand);
                else
                    System.out.println(startingString + showObjectiveCommands + notMyTurnCommands + helpCommand);
                break;
            case GameState.STARTED:
                if(isMyTurn){
                    if(!client.getVirtualView().isCurrentPlayerhasPlayed())
                        System.out.println(startingString + gameCommands + separatorString + showObjectiveCommands + notMyTurnCommands + helpCommand);
                    else
                        System.out.println(startingString + drawCommands + separatorString + showObjectiveCommands + notMyTurnCommands + helpCommand);
                }
                else
                    System.out.println(startingString + showObjectiveCommands + notMyTurnCommands + helpCommand);
                break;
            case GameState.ENDING:
                if(isMyTurn)
                    System.out.println(startingString + gameCommands + separatorString + showObjectiveCommands + notMyTurnCommands + helpCommand);
                else
                    System.out.println(startingString + showObjectiveCommands + notMyTurnCommands + helpCommand);
                break;
            case GameState.ENDED:
                System.out.println(startingString + lobbyCommands + separatorString + showObjectiveCommands + notMyTurnCommands + helpCommand);
                break;

        }
    }

    public void handleInput(String input) {
        Message messageToSend;
        String name;
        input = input.toLowerCase();

        String errorString = "you can't use this command now";

        GameState currentState = client.getVirtualView().getState();

        try {
            switch (input) {
                case "creategame":
                    if(currentState != null && currentState != GameState.ENDED){
                        System.out.println(errorString);
                        break;
                    }
                    int numOfPlayers = askForIntInput("insert the number of players between 2 and 4", 2, 4);
                    name = askForStringInput("insert your nickname");
                    messageToSend = new CreateGameMessage(client.getClientID(), numOfPlayers, name);
                    client.sendMessage(messageToSend);
                    break;
                case "joingame":
                    if(currentState != null && currentState != GameState.ENDED){
                        System.out.println(errorString);
                        break;
                    }
                    int gameID = askForIntInput("insert the game ID", 0, 1000);
                    name = askForStringInput("insert your nickname");
                    messageToSend = new JoinGameMessage(client.getClientID(), name, gameID);
                    client.sendMessage(messageToSend);
                    break;
                case "getgamelist":
                    if(currentState != null && currentState != GameState.ENDED){
                        System.out.println(errorString);
                        break;
                    }
                    messageToSend = new GetGameListMessage(client.getClientID());
                    client.sendMessage(messageToSend);
                    break;
                case "getcoloravailable":
                    if(currentState != GameState.CHOOSING_COLORS){
                        System.out.println(errorString);
                        break;
                    }

                    messageToSend = new GetColorAvailableMessage(client.getClientID());
                    client.sendMessage(messageToSend);
                    break;
                case "choosecolor":
                    if(currentState != GameState.CHOOSING_COLORS){
                        System.out.println(errorString);
                        break;
                    }

                    String colorChoosen = null;
                    colorChoosen = askForStringInput("Which color do you want to choose?").toUpperCase();
                    if(!colorChoosen.equals("BLUE") && !colorChoosen.equals("YELLOW") && !colorChoosen.equals("RED") && !colorChoosen.equals("GREEN")){
                        System.out.println("Color invalid! Use the command \"getColorAvailable\" to know which color you can choose" );
                        printAvailableCommands();
                        break;
                    }
                    PlayerColor color = PlayerColor.valueOf(colorChoosen);
                    messageToSend = new WantThatColorMessage(client.getClientID(), client.getNickname(), color);
                    client.sendMessage(messageToSend);
                    break;
                case "chooseobjective":
                    if(currentState != GameState.CHOOSING_OBJECTIVES){
                        System.out.println(errorString);
                        break;
                    }

                    int objWanted = askForIdObjectiveInput("Which objective do you prefer?", (client.getVirtualView().getHandObjectives()));
                    messageToSend = new ObjectiveCardChosenMessage(client.getClientID(), client.getNickname(), objWanted);
                    client.sendMessage(messageToSend);
                    break;
                case "playcard":
                    if(currentState != GameState.CHOOSING_STARTER_CARDS && currentState != GameState.STARTED && currentState != GameState.ENDING){
                        System.out.println(errorString);
                        break;
                    }
                    if(client.getVirtualView().getGrids().get(client.getNickname()) != null && !client.getVirtualView().getGrids().get(client.getNickname()).isEmpty())
                        printGrid(client.getVirtualView().getGrids().get(client.getNickname()),true);

                    int idCard = askForIdCardInput("Among the cards in your hand, which one do you want to play? Here the cards' id:", client.getVirtualView().getHandCards());
                    boolean face = askForFaceToPlay();
                    Coordinate coords = askForCoordinateInput(client.getVirtualView().getAvailablePositions());
                    messageToSend = new PlayCardMessage(client.getClientID(), idCard, face, coords, client.getNickname());
                    client.sendMessage(messageToSend);
                    break;
                case "drawcard":
                    if(currentState != GameState.CHOOSING_STARTER_CARDS && currentState != GameState.STARTED){
                        System.out.println(errorString);
                        break;
                    }
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
                    if(currentState != GameState.CHOOSING_STARTER_CARDS && currentState != GameState.STARTED){
                        System.out.println(errorString);
                        break;
                    }
                    idCard = askForIdCardInput("Among those, which card do you want?", client.getVirtualView().getDisplayedCards());
                    messageToSend = new PickMessage(client.getClientID(), idCard, client.getNickname());
                    client.sendMessage(messageToSend);
                    break;
                case "showgrid":
                    if(currentState == null || currentState == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    if(client.getVirtualView().getScores() == null){
                        System.out.println("no one has played a card yet");
                        break;
                    }
                    Set<String> otherPlayersNick = client.getVirtualView().getScores().keySet();
                    otherPlayersNick.remove(client.getNickname());
                    if(otherPlayersNick.isEmpty()){
                        System.out.println("no one has played a card yet");
                        break;
                    }
                    String playerAsked;
                    System.out.println("Among the fields of the players, which one do you want to look?");
                    for (String p : otherPlayersNick) {
                        printWithNicksColor(p + " ", p);
                    }
                    System.out.println();
                    do {
                        playerAsked = scanner.nextLine();
                        if(!otherPlayersNick.contains(playerAsked)){
                            System.out.println("The requested player doesn't exist! Retry!");
                        }
                    } while (!otherPlayersNick.contains(playerAsked));
                    if(client.getVirtualView().getGrids().get(playerAsked) != null)
                        printGrid(client.getVirtualView().getGrids().get(playerAsked),false);
                    else
                        System.out.println("The player has not played yet");
                    break;
                case "showmygrid":
                    if(currentState == null || currentState == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    if(client.getVirtualView().getGrids().get(client.getNickname()) == null){
                        System.out.println("your grid is empty");
                        break;
                    }
                    printGrid(client.getVirtualView().getGrids().get(client.getNickname()),true);
                    break;
                case "showcounters":
                    if(currentState == null || currentState == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    if(client.getVirtualView().getGrids().get(client.getNickname()) == null){
                        System.out.println("you have no cards in your playerground. All your counters are 0.");
                        break;
                    }
                    for(Content c: client.getVirtualView().getResources(client.getNickname()).keySet()) {
                        AnsiColor a = AnsiColor.EMPTY_TEXT;
                        System.out.print(a.getFormattedCharacter(c, a));
                        System.out.println(" : " + client.getVirtualView().getResources(client.getNickname()).get(c));
                    }
                    break;
                case "showscores":
                    if(currentState == null || currentState == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    if(client.getVirtualView().getScores() != null && !client.getVirtualView().getScores().isEmpty()){
                        printScores();
                    }
                    else{
                        System.out.println("The game scores are: ");
                        for (String player : client.getVirtualView().getListOfPlayers())
                            System.out.println(player + ": " + 0);
                    }
                    break;
                case "showdisplayed":
                    if(currentState == null || currentState == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    printListOfCards(client.getVirtualView().getDisplayedCards(), true,true,true,false);
                    break;
                case "showdeckstop":
                    if(currentState == null || currentState == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    System.out.println("The back of the top card of the resource deck is: ");
                    printFace(((ResourceCard) client.getVirtualView().getResourceDeck().getCardList().getFirst()).getBack());
                    System.out.println("The back of the top card of the gold deck is: ");
                    printFace(((GoldCard) client.getVirtualView().getGoldDeck().getCardList().getFirst()).getBack());
                    break;
                case "showhand":
                    if(currentState == null || currentState == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    printPlayerHand();
                    break;
                case "showobjective":
                    if(currentState == null || currentState == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    if(client.getVirtualView().getDisplayedObjectiveCards() != null && !client.getVirtualView().getDisplayedObjectiveCards().isEmpty()){
                        System.out.println("COMMON OBJECTIVE CARDS: ");
                        printObjectiveCards(client.getVirtualView().getDisplayedObjectiveCards().get(0));
                        printObjectiveCards(client.getVirtualView().getDisplayedObjectiveCards().get(1));
                    }
                    System.out.println("YOUR OBJECTIVE CARD(s): ");
                    printObjectiveCards(client.getVirtualView().getHandObjectives().get(0));
                    if(client.getVirtualView().getHandObjectives().size() == 2){
                        printObjectiveCards(client.getVirtualView().getHandObjectives().get(1));
                    }
                    break;
                case "globalchat":
                    if(currentState == null){
                        System.out.println(errorString);
                        break;
                    }
                    for (ChatMessage m : client.getVirtualView().getGlobalChat()) {
                        System.out.println(getColoredNick(m.getSender()) + GRAY + ": " + m.getMessage() + RESET);
                    }
                    break;
                case "chat":
                    if(currentState == null){
                        System.out.println(errorString);
                        break;
                    }
                    String playerNick = askForStringInput("Which chat you want to show?" + printOtherPlayers());
                    if (client.getVirtualView().getPrivateChat(playerNick) != null){
                        for (ChatMessage m: client.getVirtualView().getPrivateChat(playerNick))
                            System.out.println(getColoredNick(m.getSender()) + GRAY + ": " + m.getMessage() + RESET);
                    } else {
                        System.out.println("There's no such player");
                    }
                    break;
                case "sendmessage":
                    if(currentState == null){
                        System.out.println(errorString);
                        break;
                    }
                    String recipient = askForStringInput("Who do you want to send the message to? \nglobal" + printOtherPlayers());
                    String chatMessage = askForStringInput("Type your message: ");
                    client.getVirtualView().sendPrivateMessage(new ChatMessage(client.getNickname(), recipient, chatMessage));
                    client.sendMessage(new sendChatMessage(client.getClientID(), client.getNickname(), recipient, chatMessage));
                    break;
                case "?":
                    printAvailableCommands();
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
               toReturn = toReturn.concat("\n" + getColoredNick(s));
        }
        return toReturn;
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

    public void printFrontPoints(Face face, AnsiColor cardType) {
        String frontPoints = "";

        int points = 0;
        if(face instanceof GoldFront)
            points = ((GoldFront) face).getPoints();
        else if(face instanceof NormalFace)
            points = ((NormalFace) face).getPoints();

        switch (points) {
            case 0 -> frontPoints += cardType.getFormattedCharacter();
            case 1 -> frontPoints += AnsiColor.POINT_ONE.getFormattedCharacter();
            case 2 -> frontPoints += AnsiColor.POINT_TWO.getFormattedCharacter();
            case 3 -> frontPoints += AnsiColor.POINT_THREE.getFormattedCharacter();
            case 5 -> frontPoints += AnsiColor.POINT_FIVE.getFormattedCharacter();
        }

        System.out.print(frontPoints);

        if(!(face instanceof GoldFront goldfront)) {
            System.out.print(cardType.getFormattedCharacter());
            return;
        }
        if (goldfront.getObjectNeeded() != null) {
            printCorner(goldfront.getObjectNeeded(), cardType);
        }else if (goldfront.isOverlapped()) {
            System.out.print(AnsiColor.OVERLAPPED.getFormattedCharacter());
        }else {
            System.out.print(cardType.getFormattedCharacter());
        }

    }

    public void printGoldBorder(AnsiColor cardType, int bORe) {
        String border = "";

        if (bORe == 0) {
            switch (cardType) {
                case PLANT_BACKGROUND -> border = AnsiColor.B_GOLD_PLANT_BACKGROUND.getFormattedCharacter();
                case ANIMAL_BACKGROUND -> border = AnsiColor.B_GOLD_ANIMAL_BACKGROUND.getFormattedCharacter();
                case INSECT_BACKGROUND -> border = AnsiColor.B_GOLD_INSECT_BACKGROUND.getFormattedCharacter();
                case FUNGI_BACKGROUND -> border = AnsiColor.B_GOLD_FUNGI_BACKGROUND.getFormattedCharacter();
            }
        } else if (bORe == 1) {
            switch (cardType) {
                case PLANT_BACKGROUND -> border = AnsiColor.E_GOLD_PLANT_BACKGROUND.getFormattedCharacter();
                case ANIMAL_BACKGROUND -> border = AnsiColor.E_GOLD_ANIMAL_BACKGROUND.getFormattedCharacter();
                case INSECT_BACKGROUND -> border = AnsiColor.E_GOLD_INSECT_BACKGROUND.getFormattedCharacter();
                case FUNGI_BACKGROUND -> border = AnsiColor.E_GOLD_FUNGI_BACKGROUND.getFormattedCharacter();
            }
        }

        System.out.print(border);
    }

    public AnsiColor getCardType(Face face){
        Content background = Card.getType(face.getIdCard());
        assert background != null;
        return switch(background){
            case FUNGI_KINGDOM -> AnsiColor.FUNGI_BACKGROUND;
            case PLANT_KINGDOM ->  AnsiColor.PLANT_BACKGROUND;
            case INSECT_KINGDOM -> AnsiColor.INSECT_BACKGROUND;
            case ANIMAL_KINGDOM -> AnsiColor.ANIMAL_BACKGROUND;
            case QUILL -> AnsiColor.STARTER_BACKGROUND;
            case INKWELL -> AnsiColor.STARTER_BACKGROUND;
            case MANUSCRIPT -> AnsiColor.STARTER_BACKGROUND;
            case EMPTY -> AnsiColor.STARTER_BACKGROUND;
            case HIDDEN -> AnsiColor.STARTER_BACKGROUND;
        };
    }

    public void printObjectiveCards(ObjectiveCard card){
        System.out.println("card id: "+ card.getIdCard());
        AnsiColor ansiBackground = null;
        String background;
        switch(card.getType(card.getIdCard())){
           case FUNGI_KINGDOM -> ansiBackground = AnsiColor.FUNGI_BACKGROUND;
           case PLANT_KINGDOM -> ansiBackground = AnsiColor.PLANT_BACKGROUND;
           case INSECT_KINGDOM -> ansiBackground = AnsiColor.INSECT_BACKGROUND;
           case ANIMAL_KINGDOM -> ansiBackground = AnsiColor.ANIMAL_BACKGROUND;
           case QUILL -> ansiBackground = AnsiColor.STARTER_BACKGROUND;
           case INKWELL -> ansiBackground = AnsiColor.STARTER_BACKGROUND;
           case MANUSCRIPT -> ansiBackground = AnsiColor.STARTER_BACKGROUND;
           case EMPTY -> ansiBackground = AnsiColor.STARTER_BACKGROUND;
           case HIDDEN -> ansiBackground = AnsiColor.STARTER_BACKGROUND;
        }
        background = ansiBackground.getFormattedCharacter();
        if(card instanceof StructuredObjectiveCard){
            switch(((StructuredObjectiveCard) card).getStructureType()){
                case LEFT_DIAGONAL :
                    //first row
                    System.out.print(background);
                    System.out.print(background);
                    printObjectivesPoints(card);
                    System.out.print(background);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(0), ansiBackground);
                    System.out.println();
                    //second row
                    for(int i=0; i<3; i++){
                        System.out.print(background);
                    }
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(1), ansiBackground);
                    System.out.println(background);
                    //third row
                    System.out.print(background);
                    System.out.print(background);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(2), ansiBackground);
                    System.out.print(background);
                    System.out.println(background);
                    break;
                case DOUBLE_UP_LEFT :
                    //first row
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(0), ansiBackground);
                    System.out.print(background);
                    printObjectivesPoints(card);
                    System.out.print(background);
                    System.out.println(background);
                    //second row
                    System.out.print(background);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(1), ansiBackground);
                    System.out.print(background);
                    System.out.print(background);
                    System.out.println(background);
                    //third row
                    System.out.print(background);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(2), ansiBackground);
                    System.out.print(background);
                    System.out.print(background);
                    System.out.println(background);
                    break;
                case DOUBLE_UP_RIGHT :
                    //first row
                    System.out.print(background);
                    System.out.print(background);
                    printObjectivesPoints(card);
                    System.out.print(background);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(0), ansiBackground);
                    System.out.println();
                    //second row
                    for(int i=0; i<3; i++){
                        System.out.print(background);
                    }
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(1), ansiBackground);
                    System.out.println(background);
                    //third row
                    for(int i=0; i<3; i++){
                        System.out.print(background);
                    }
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(2), ansiBackground);
                    System.out.println(background);
                    break;
                case DOUBLE_DOWN_LEFT :
                    //first row
                    System.out.print(background);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(0), ansiBackground);
                    printObjectivesPoints(card);
                    System.out.print(background);
                    System.out.println(background);
                    //second row
                    System.out.print(background);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(1), ansiBackground);
                    System.out.print(background);
                    System.out.print(background);
                    System.out.println(background);
                    //third row
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(2), ansiBackground);
                    System.out.print(background);
                    System.out.print(background);
                    System.out.print(background);
                    System.out.println(background);
                    break;
                case DOUBLE_DOWN_RIGHT :
                    //first row
                    for(int i=0; i<2; i++){
                        System.out.print(background);
                    }
                    printObjectivesPoints(card);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(0), ansiBackground);
                    System.out.println(background);
                    //third row
                    for(int i=0; i<3; i++){
                        System.out.print(background);
                    }
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(1), ansiBackground);
                    System.out.println(background);
                    //third row
                    System.out.print(background);
                    System.out.print(background);
                    System.out.print(background);
                    System.out.print(background);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(2), ansiBackground);
                    System.out.println();
                    break;
                case RIGHT_DIAGONAL :
                    //first row
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(0), ansiBackground);
                    System.out.print(background);
                    printObjectivesPoints(card);
                    System.out.print(background);
                    System.out.println(background);
                    //second row
                    System.out.print(background);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(1), ansiBackground);
                    System.out.print(background);
                    System.out.print(background);
                    System.out.println(background);
                    //third row
                    System.out.print(background);
                    System.out.print(background);
                    printCorner(((StructuredObjectiveCard) card).getResourceRequested().get(2), ansiBackground);
                    System.out.print(background);
                    System.out.println(background);
                    break;
            }
        }
        else if(card instanceof NotStructuredObjectiveCard){
            //first row
            System.out.print(background);
            System.out.print(background);
            printObjectivesPoints(card);
            System.out.print(background);
            System.out.println(background);
            //second row
            for(int i=0; i<5; i++){
                System.out.print(background);
            }
            System.out.println();
            //third row
            switch(((NotStructuredObjectiveCard) card).getObjectRequested().size()){
                case 2 :
                    System.out.print(background);
                    printCorner(((NotStructuredObjectiveCard) card).getObjectRequested().get(0), ansiBackground);
                    System.out.print(background);
                    printCorner(((NotStructuredObjectiveCard) card).getObjectRequested().get(1), ansiBackground);
                    System.out.println(background);
                    break;
                case 3:
                    System.out.print(background);
                    printCorner(((NotStructuredObjectiveCard) card).getObjectRequested().get(0), ansiBackground);
                    printCorner(((NotStructuredObjectiveCard) card).getObjectRequested().get(1), ansiBackground);
                    printCorner(((NotStructuredObjectiveCard) card).getObjectRequested().get(2), ansiBackground);
                    System.out.println(background);
                    break;
            }
        }
    }

    public void printObjectivesPoints(ObjectiveCard card){
        switch (card.getPoints()){
            case 2 -> System.out.print(AnsiColor.POINT_TWO.getFormattedCharacter());
            case 3 -> System.out.print(AnsiColor.POINT_THREE.getFormattedCharacter());
        }
    }

    public void printScores(){
        System.out.println("The game scores are: ");
        for (Map.Entry<String, Integer> e : client.getVirtualView().getScores().entrySet())
            System.out.println(e.getKey() + ": " + e.getValue());
    }

    public void printGrid(Map<Coordinate, Face> grid, boolean printCoordinate){
        //todo: stampare il centro delle starter
        int xmin = getMinX(grid.keySet());
        int ymin = getMinY(grid.keySet());
        int xmax = getMaxX(grid.keySet());
        int ymax = getMaxY(grid.keySet());

        String tab4 = "    ";

        AnsiColor cardType;

        Object[] returnArray;

        if(printCoordinate)
            System.out.print(tab4 + "   ");

        for(int j = (ymax*2)+1; j >= (ymin*2)-1; j--){
            for(int i = (xmin*2)-1; i <= (xmax*2)+1; i++){

                if( i%2 != 0 && j%2 != 0){
                    //se i e j sono dispari, significa che è un angolo --> cerco la carta che ha l'angolo NON coperto in quella posizione --> disegno quell'angolo
                    returnArray = searchCornerAndCardTypeAtPosition(new Coordinate(i,j), grid);
                    Content corner = (Content) returnArray[0];
                    cardType = (AnsiColor) returnArray[1];

                    if(corner != null)
                        printCorner(corner, cardType);
                    else
                        System.out.print(tab4);
                } else if(i%2 == 0 && j%2 == 0){
                    //se i è pari e j è pari, significa che è un centro --> stampo il centro
                    Coordinate coord = new Coordinate(i/2,j/2);

                    if(grid.containsKey(coord)) {
                        printCenter(coord, grid);
                    }else
                        System.out.print(tab4+tab4+tab4);
                } else if ( i%2 == 0 && j%2 != 0){
                    //se i è pari e j è dispari, significa che è sopra/sotto il centro --> stampo il sopra/sotto (punti/ objectNeeded/...)

                    returnArray = searchCenterVerticalAndCardTypeAtPosition(new Coordinate(i,j), grid);
                    cardType = (AnsiColor) returnArray[0];

                    if(cardType != null)
                        printCenterVertical(cardType, (boolean)returnArray[1], grid, new Coordinate(i,j));
                    else
                        System.out.print(tab4+tab4+tab4);
                } else if( i%2 != 0 && j%2 == 0){
                    //se i è dispari e j è pari, significa che è a destra/sinistra del centro --> stampo lati
                    returnArray = searchCenterHorizontalAndCardTypeAtPosition(new Coordinate(i,j), grid);
                    cardType = (AnsiColor) returnArray[0];

                    if(cardType != null)
                        printCenterHorizontal(cardType, (boolean)returnArray[1], grid, new Coordinate(i,j));
                    else
                        System.out.print(tab4);
                }
            }

            System.out.println();
            if(printCoordinate) {
                if (j % 2 != 0 && (j - 1) / 2 != ymin - 1) {

                    if((j-1)/2>=0 && (j-1)/2 < 10)
                        System.out.print("  ");
                    if(((j-1)/2 <= -1 && (j-1)/2 > -10) || ((j-1)/2 >=10))
                        System.out.print(" ");
                    System.out.print((j - 1) / 2 + tab4);
                } else
                    System.out.print("   " + tab4);
            }
        }
        if(printCoordinate){
            System.out.print("       ");
            for(int i = xmin; i <= xmax; i++){
                System.out.print("\t" +i+ "\t");
            }
        }
        System.out.println();

    }

    public void printFace(Face face){
        HashMap<Coordinate, Face> grid = new HashMap<>();
        grid.put(new Coordinate(0,0), face);

        printGrid(grid,false);
    }

    public void printListOfCards(ArrayList<PlayableCard> cards, boolean printId, boolean printCost, boolean printFront, boolean printBack){

        HashMap<Coordinate, Face> grid = new HashMap<>();
        String tab4 = "    ";

        //print the id's
        if(printId) {
            for (PlayableCard c : cards) {
                System.out.print("id card: " + c.getIdCard() + "\t\t\t");
            }
            System.out.println();
        }
        //print the needed resources

        if(printCost) {
            for (PlayableCard c : cards) {
                if (c.getFront() instanceof GoldFront) {
                    System.out.print("cost:\t");
                    int count = 6;
                    for (Content content : ((GoldFront) c.getFront()).getCost()) {
                        AnsiColor ansicolor = AnsiColor.EMPTY_TEXT;
                        System.out.print(ansicolor.getFormattedCharacter(content, ansicolor));
                        count--;
                    }
                    for(int j = 0; j<count; j+=2){
                        System.out.print("\t");
                    }

                } else {
                    System.out.print("\t\t\t\t");
                }
            }
            System.out.println();
        }

        System.out.println();

        //print the cards

        int i = 0;
        for (PlayableCard c : cards) {
            if(printFront)
                grid.put(new Coordinate(i,0),c.getFront());
            if(printBack)
                grid.put(new Coordinate(i,-2),c.getBack());

            i+=2;
        }


        printGrid(grid,false);
    }

    public void printPlayerHand() {
        ArrayList<PlayableCard> cards = client.getVirtualView().getHandCards();
        if (cards.isEmpty()){
            System.out.println("you have no cards");
            return;
        }

        printListOfCards(cards,true,true,true,true);
    }

    public Object[] searchCornerAndCardTypeAtPosition(Coordinate coord, Map<Coordinate,Face> grid){
        Coordinate check;
        Content content = null;
        boolean isCovered = true;
        AnsiColor cardType = null;

        Object[] returnArray = new Object[2];

        for(int i=-1; i<=1; i=i+2) {
            for (int j = -1; j <= 1; j = j + 2) {
                check = new Coordinate((coord.getX() + i)/2, (coord.getY() + j)/2);
                if(grid.containsKey(check)){
                    if(i==-1 && j==-1) { //bottom left card
                        content = grid.get(check).getTR();
                        isCovered = grid.get(check).isCoveredTR();
                        cardType = getCardType(grid.get(check));

                    }
                    else if(i==-1 && j==1) { //top left card
                        content = grid.get(check).getBR();
                        isCovered = grid.get(check).isCoveredBR();
                        cardType = getCardType(grid.get(check));
                    }
                    else if(i==1 && j==-1) { //bottom right card
                        content = grid.get(check).getTL();
                        isCovered = grid.get(check).isCoveredTL();
                        cardType = getCardType(grid.get(check));
                    }
                    else if(i==1 && j==1) { //top right card
                        content = grid.get(check).getBL();
                        isCovered = grid.get(check).isCoveredBL();
                        cardType = getCardType(grid.get(check));
                    }

                    returnArray[0] = content;
                    returnArray[1] = cardType;

                    if(!isCovered)
                        return returnArray;
                }
            }
        }

        returnArray[0] = content;
        returnArray[1] = cardType;
        return returnArray;
    }

    public Object[] searchCenterVerticalAndCardTypeAtPosition(Coordinate coord, Map<Coordinate,Face> grid){
        Coordinate check;

        AnsiColor cardType = null;
        boolean isAbove = false;

        Object[] returnArray = new Object[2];

        check = new Coordinate((coord.getX())/2, (coord.getY() + 1)/2);
        if(grid.containsKey(check)){
            //the center is above
            cardType =  getCardType(grid.get(check));
            isAbove = true;
        }

        check = new Coordinate((coord.getX())/2, (coord.getY() - 1)/2);
        if(grid.containsKey(check)){
            //the center is underneath
            cardType =  getCardType(grid.get(check));
            isAbove = false;
        }

        returnArray[0] = cardType;
        returnArray[1] = isAbove;

        return returnArray;
    }

    public Object[] searchCenterHorizontalAndCardTypeAtPosition(Coordinate coord, Map<Coordinate,Face> grid){
        Coordinate check;
        AnsiColor cardType = null;
        boolean isRight = false;

        Object[] returnArray = new Object[2];

        check = new Coordinate((coord.getX() + 1)/2, (coord.getY())/2);
        if(grid.containsKey(check)){
            //the center is on the right
            cardType =  getCardType(grid.get(check));
            isRight = true;
        }

        check = new Coordinate((coord.getX() - 1)/2, (coord.getY())/2);
        if(grid.containsKey(check)){
            //the center is on the left
            cardType =  getCardType(grid.get(check));
            isRight = false;
        }

        returnArray[0] = cardType;
        returnArray[1] = isRight;

        return returnArray;
    }

    public void printCorner(Content content, AnsiColor cardType){

        String cornerText = cardType.getFormattedCharacter(content,cardType);
        System.out.print(cornerText);
    }

    public void printCenter(Coordinate coord, Map<Coordinate, Face> grid){
        AnsiColor cardType = getCardType(grid.get(coord));
        Face face = grid.get(coord);
        String centerText = cardType.getFormattedCharacter();
        if (face instanceof NormalBack) {
            centerText += cardType.getFormattedCharacter(((NormalBack) face).getCenter(), cardType);
        }else if (face instanceof StarterFront){
            if(((StarterFront) face).getCenter().size() == 1) {
                centerText += cardType.getFormattedCharacter(((StarterFront) face).getCenter().getFirst(), cardType);
            } else {
                centerText += cardType.getFormattedCharacter(((StarterFront) face).getCenter().get(1), cardType);
            }

        }else {
            centerText += (cardType.getFormattedCharacter());
        }
        centerText +=  (cardType.getFormattedCharacter());
        System.out.print(centerText);
    }

    public void printCenterHorizontal(AnsiColor cardType, boolean isRight, Map<Coordinate, Face> grid, Coordinate coord){
        String centerText = "";
        int i = coord.getX();
        int j = coord.getY();

        if(isRight){
            // è a sinistra del centro --> per prendere la face giocata vado a destra
            Face face = grid.get(new Coordinate((i+1) / 2, j / 2));
            if (face instanceof GoldFront) {
                printGoldBorder(cardType, 0);
            } else {
                System.out.print(cardType.getFormattedCharacter());
            }

        }else {
            // è a destra del centro --> per prendere la face giocata vado a sinistra
            Face face = grid.get(new Coordinate((i-1) / 2, j / 2));
            if (face instanceof GoldFront) {
                printGoldBorder(cardType, 1);
            } else {
                System.out.print(cardType.getFormattedCharacter());
            }
        }

    }

    public void printCenterVertical(AnsiColor cardType, boolean isAbove, Map<Coordinate, Face> grid, Coordinate coord){
        String centerText = "";
        int i = coord.getX();
        int j = coord.getY();


        if(isAbove) {
            // il centro  è sopra
            centerText += cardType.getFormattedCharacter();
            Face face = grid.get(new Coordinate(i / 2, (j+1) / 2));
            if(face instanceof StarterFront){
                if(((StarterFront) face).getCenter().size() == 3) {
                    centerText += cardType.getFormattedCharacter(((StarterFront) face).getCenter().get(2), cardType);
                    centerText += cardType.getFormattedCharacter();
                }
                else {
                    for (i = 0; i < 2; i++) {
                        centerText += cardType.getFormattedCharacter();
                    }
                }
            }
            else {
                for (i = 0; i < 2; i++) {
                    centerText += cardType.getFormattedCharacter();
                }
            }
        }

        else {
            //il centro è sotto
            Face face = grid.get(new Coordinate(i / 2, (j-1) / 2));
            if (face instanceof NormalFace) { //resource or goldfront
                centerText += cardType.getFormattedCharacter();
                System.out.print(centerText);
                printFrontPoints(face, cardType);
                return;
            } else if (face instanceof StarterFront) {
                if(((StarterFront) face).getCenter().size() != 1) {
                    centerText += cardType.getFormattedCharacter();
                    centerText += cardType.getFormattedCharacter(((StarterFront) face).getCenter().getFirst(), cardType);
                    centerText += cardType.getFormattedCharacter();
                }
                else {
                    for (i = 0; i < 3; i++) {
                        centerText += cardType.getFormattedCharacter();
                    }
                }
            }
            else {
                for (i = 0; i < 3; i++) {
                    centerText += cardType.getFormattedCharacter();
                }
            }
        }

        System.out.print(centerText);

    }

    public void printWithNicksColor(String stringToPrompt, String nickname){
        String colorString = getColor(nickname);
        String white = getWhite();

        System.out.print(colorString + stringToPrompt + white);
    }

    public void printWithNicksColorNewLine(String stringToPrompt, String nickname){
        printWithNicksColor(stringToPrompt + "\n", nickname);
    }

    public String getColoredNick(String nickname){
        return getColor(nickname)+ nickname + getWhite();
    }

    public String getColor(String nickname){
        String blue = "\u001B[34m";
        String red = "\u001B[31m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String white = "\u001B[0m";

        PlayerColor color = client.getVirtualView().getPlayerColorMap().get(nickname);
        if(color == null)
            return white;

        String colorString;

        switch (color){
            case BLUE -> colorString = blue;
            case RED -> colorString = red;
            case GREEN -> colorString = green;
            case YELLOW -> colorString = yellow;
            default -> colorString = white;
        }

        return colorString;
    }

    public String getWhite(){
        return "\u001B[0m";
    }

    private static final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    /**
     * Validates if the given string is a valid IPv4 address.
     *
     * @param ip the IP address string to be validated
     * @return {@code true} if the input string is a valid IPv4 address,
     *         {@code false} otherwise
     */
    public static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }
}
