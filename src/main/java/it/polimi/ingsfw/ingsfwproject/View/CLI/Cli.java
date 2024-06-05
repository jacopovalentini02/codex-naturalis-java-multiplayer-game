package it.polimi.ingsfw.ingsfwproject.View.CLI;

import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

import java.util.*;

import java.util.HashMap;
import java.util.Scanner;

import it.polimi.ingsfw.ingsfwproject.View.View;

public class Cli extends View implements Runnable {

    Scanner scanner;

    String notMyTurnCommands = "these are the commands that you can perform at any time: \n\t-> ShowMyGrid \n\t-> ShowGrid \n\t-> showScores \n\t-> showDisplayed " +
            "\n\t-> showDecksTop \n\t-> showHand \n\t-> showObjective \n\t-> showCounters \n\t-> globalChat \n\t-> chat \n\t-> sendMessage";
    String lobbyCommands = "now you can insert one of the following commands:\n\t-> CreateGame" + "\n\t-> JoinGame" + "\n\t-> GetGameList";
    String gameCommands = "now you can do one of the following specific actions: \n\t-> PlayCard \nand " + notMyTurnCommands;
    String drawCommands = "now you can do one of the following actions: \n\t-> DrawCard \n\t-> PickCard \nand " + notMyTurnCommands;
    String chooseColorCommands = "now you can do one of the following actions: \n\t-> GetColorAvailable \n\t-> ChooseColor \nand " + notMyTurnCommands;
    String chooseObjectiveCommands = "now you can do one of the following actions: \n\t-> ShowObjective \n\t-> chooseObjective \nand " + notMyTurnCommands;
    String chooseStarterCommands = "now you can do one of the following actions: \n\t-> ShowHand \n\t-> PlayCard";


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
                for(PlayableCard c : cards){
                    System.out.println(c.getIdCard());
                    printFace(c.getFront());
                    if(c.getFront() instanceof GoldFront){
                        System.out.print("Costs: ");
                       for( Content cont : ((GoldFront) c.getFront()).getCost()){
                           System.out.print(cont + " ");
                       }
                    }
                    System.out.println();
                    printFace(c.getBack());
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
        System.out.println("Wait for all the players to join the game...");
    }

    @Override
    public void notifyNewPlayerJoined(ArrayList<String> nicknames){
        /*
        System.out.println("A new player has joined the game.");

        System.out.println("Players in this lobby: ");

        for (String s : nicknames) {
            System.out.print(s + " ");
        }
        System.out.println();

         */
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
    public void notifyGoldDeckUpdate(){
        //System.out.println("Gold deck updated!");
    }

    @Override
    public void notifyResourceDeckUpdate() {
        //System.out.println("Resourced deck updated!");
    }

    @Override
    public void notifyDisplayedCardsUpdate(ArrayList<PlayableCard> displayedCards) {
        /*
        System.out.println("Displayed playable cards updated! Here's how:\n");
        int i =1;
        for(PlayableCard pc : displayedCards){
            System.out.println("Card in place " + i +"of the displayed cards");
            printFace(pc.getFront());
            i++;
        }
         */
    }

    @Override
    public void notifyCurrentPlayer(String nickname){
        //clear cli
        //TODO: QUESTO FUNZIONA SOLO NEL TERMINALE, NON NELL'IDE! A JAR CREATO CONTROLLARE CHE FUNZIONI
        /*
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
         */

        printWithNicksColorNewLine("it' now " + nickname + "'s turn", nickname);
        printAvailableCommands();
    }

    @Override
    public void notifyAvailablePositions(ArrayList<Coordinate> coord) {
        /*
        if (coord.isEmpty()){
            System.out.println("You have no more coordinates available!");
            return;
        }
        System.out.println("your available positions have changed: now you can play a card in the following coordinates:");
        for(Coordinate c : coord)
            System.out.println(c);

         */
    }

    @Override
    public void notifyHandObjectives(ArrayList<ObjectiveCard> cards){
        if(cards.size() == 1)
            System.out.println("your objective card has been selected correctly!");
    }

    @Override
    public void notifyGameState(GameState state){
        if(!state.equals(client.getVirtualView().getState()))
            System.out.println("Game state updated to: '" + state);
        if(state.equals(GameState.ENDING)){
            System.out.println("it's your last turn!");
        }
        if(state.equals(GameState.ENDED))
            printAvailableCommands();
    }

    @Override
    public void notifyGridUpdate(String nickname, Map<Coordinate,Face> grid){
        /*
        System.out.println(nickname + "'s grid has been updated. Here's how: ");
        printGrid(client.getVirtualView().getGrids().get(nickname));

         */
    }

    @Override
    public void notifyResourcesUpdate(String nickname, HashMap<Content, Integer> resources) {
        /*
        System.out.println(nickname + "'s resource count has been updated to: ");
        for (Content c : resources.keySet())
            System.out.println(c + ": " + resources.get(c));
        System.out.println("\n");

         */
    }

    @Override
    public void notifyWinnerUpdate(String nick){
        printWithNicksColorNewLine("The winner is " + nick + "! Congrats!", nick);
    }

    @Override
    public void notifyHandCardsUpdate(ArrayList<PlayableCard> cards){
        /*
        System.out.println("Your hand has just been updated. Look at what you have: \n");
        printPlayerHand();

         */
    }

    @Override
    public void notifyDisplayedObjectives(List<ObjectiveCard> cards){
        /*
        System.out.println("Displayed objective cards updated. Here they are: ");
        for (ObjectiveCard o : cards)
            System.out.print(o.getIdCard() + " ");
        System.out.println("\n");

         */
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
                case GameState.STARTED, GameState.ENDING:
                    if(!client.getVirtualView().isCurrentPlayerhasPlayed()){ //se non ho ancora giocato
                        System.out.println(gameCommands);
                    }else {
                        System.out.println(drawCommands);
                    }
                    break;
                case GameState.ENDED:
                    System.out.println(lobbyCommands);
                    break;
            }

        }
    }


    @Override
    public void notifyChatMessage(ChatMessage message){
        System.out.println(GRAY + "New chat message from " + getColoredNick(message.getSender()) + " : " + message.getMessage() + RESET);
    }

    public void handleInput(String input) {
        Message messageToSend;
        String name;
        input = input.toLowerCase();

        String errorString = "you can't use this command now";

        try {
            switch (input) {
                case "creategame":
                    if(client.getVirtualView().getState() != null && client.getVirtualView().getState() != GameState.ENDED){
                        System.out.println(errorString);
                        break;
                    }
                    int numOfPlayers = askForIntInput("insert the number of players between 2 and 4", 2, 4);
                    name = askForStringInput("insert your nickname");
                    messageToSend = new CreateGameMessage(client.getClientID(), numOfPlayers, name);
                    client.sendMessage(messageToSend);
                    break;
                case "joingame":
                    if(client.getVirtualView().getState() != null && client.getVirtualView().getState() != GameState.ENDED){
                        System.out.println(errorString);
                        break;
                    }
                    int gameID = askForIntInput("insert the game ID", 0, 1000);
                    name = askForStringInput("insert your nickname");
                    messageToSend = new JoinGameMessage(client.getClientID(), name, gameID);
                    client.sendMessage(messageToSend);
                    break;
                case "getgamelist":
                    if(client.getVirtualView().getState() != null && client.getVirtualView().getState() != GameState.ENDED){
                        System.out.println(errorString);
                        break;
                    }
                    messageToSend = new GetGameListMessage(client.getClientID());
                    client.sendMessage(messageToSend);
                    break;
                case "getcoloravailable":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() != GameState.CHOOSING_COLORS){
                        System.out.println(errorString);
                        break;
                    }

                    messageToSend = new GetColorAvailableMessage(client.getClientID());
                    client.sendMessage(messageToSend);
                    break;
                case "choosecolor":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() != GameState.CHOOSING_COLORS){
                        System.out.println(errorString);
                        break;
                    }

                    String colorChoosen = null;
                    colorChoosen = askForStringInput("What color do you want to choose?").toUpperCase();
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
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() != GameState.CHOOSING_OBJECTIVES){
                        System.out.println(errorString);
                        break;
                    }

                    int objWanted = askForIdObjectiveInput("What objective do you prefer?", (client.getVirtualView().getHandObjectives()));
                    messageToSend = new ObjectiveCardChosenMessage(client.getClientID(), client.getNickname(), objWanted);
                    client.sendMessage(messageToSend);
                    break;
                case "playcard":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS || client.getVirtualView().getState() == GameState.ENDED){
                        System.out.println(errorString);
                        break;
                    }
                    if(client.getVirtualView().getGrids().get(client.getNickname()) != null && !client.getVirtualView().getGrids().get(client.getNickname()).isEmpty())
                        printGrid(client.getVirtualView().getGrids().get(client.getNickname()));

                    int idCard = askForIdCardInput("Among the cards in your hand, which one do you want to play? Here the cards' id:", client.getVirtualView().getHandCards());
                    boolean face = askForFaceToPlay();
                    Coordinate coords = askForCoordinateInput(client.getVirtualView().getAvailablePositions());
                    messageToSend = new PlayCardMessage(client.getClientID(), idCard, face, coords, client.getNickname());
                    client.sendMessage(messageToSend);
                    break;
                case "drawcard":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS || client.getVirtualView().getState() == GameState.ENDED){
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
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS || client.getVirtualView().getState() == GameState.ENDED){
                        System.out.println(errorString);
                        break;
                    }
                    idCard = askForIdCardInput("Among those, which card do you want?", client.getVirtualView().getDisplayedCards());
                    messageToSend = new PickMessage(client.getClientID(), idCard, client.getNickname());
                    client.sendMessage(messageToSend);
                    break;
                case "showgrid":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS || client.getVirtualView().getScores() == null){
                        System.out.println(errorString);
                        break;
                    }
                    Set<String> otherPlayersNick = client.getVirtualView().getScores().keySet();
                    otherPlayersNick.remove(client.getNickname());
                    String playerAsked;
                    System.out.println("Among the fields of the players, which one do you want to look?");
                    for (String p : otherPlayersNick) {
                        printWithNicksColor(p + " ", p);
                    }
                    do {
                        playerAsked = scanner.nextLine();
                        if(!otherPlayersNick.contains(playerAsked)){
                            System.out.println("The requested player doesn't exist! Retry!");
                        }
                    } while (!otherPlayersNick.contains(playerAsked));
                    printGrid(client.getVirtualView().getGrids().get(playerAsked));
                    break;

                case "showmygrid":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS || client.getVirtualView().getGrids().get(client.getNickname()) == null){
                        System.out.println(errorString);
                        break;
                    }
                    printGrid(client.getVirtualView().getGrids().get(client.getNickname()));
                    break;
                case "showscores":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    printScores();
                    break;
                case "showdisplayed":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    //todo: cambiare il metodo di stampa delle carte -> stampa lines in grid
                    for (PlayableCard c : client.getVirtualView().getDisplayedCards()) {
                        printFace(c.getFront());
                        if(c.getFront() instanceof GoldFront){
                            System.out.print("Costs: ");
                            for( Content cont : ((GoldFront) c.getFront()).getCost()){
                                System.out.print(cont + " ");
                            }
                        }
                        System.out.println();
                    }
                    break;
                case "showdeckstop":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    System.out.println("The back of the top card of the resource deck is: ");
                    printFace(((ResourceCard) client.getVirtualView().getResourceDeck().getCardList().getFirst()).getBack());
                    System.out.println("The back of the top card of the gold deck is: ");
                    printFace(((GoldCard) client.getVirtualView().getGoldDeck().getCardList().getFirst()).getBack());
                    break;
                case "showhand":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS){
                        System.out.println(errorString);
                        break;
                    }
                    printPlayerHand();
                    break;
                case "showobjective":
                    if(client.getVirtualView().getState() == null || client.getVirtualView().getState() == GameState.WAITING_FOR_PLAYERS){
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
                    if(client.getVirtualView().getState() == null){
                        System.out.println(errorString);
                        break;
                    }
                    for (ChatMessage m : client.getVirtualView().getGlobalChat()) {
                        System.out.println(getColoredNick(m.getSender()) + GRAY + ": " + m.getMessage() + RESET);
                    }
                    break;
                case "chat":
                    if(client.getVirtualView().getState() == null){
                        System.out.println(errorString);
                        break;
                    }
                    String playerNick = askForStringInput("Which chat you want to show? Type the nick of the other player: " + printOtherPlayers());
                    if (client.getVirtualView().getPrivateChat(playerNick) != null){
                        for (ChatMessage m: client.getVirtualView().getPrivateChat(playerNick))
                            System.out.println(getColoredNick(m.getSender()) + GRAY + ": " + m.getMessage() + RESET);
                    } else {
                        System.out.println("There's no such player");
                    }
                    break;
                case "sendmessage":
                    if(client.getVirtualView().getState() == null){
                        System.out.println(errorString);
                        break;
                    }
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
               toReturn = toReturn.concat(getColoredNick(s) + " ");
        }
        return toReturn;
    }

    @Override
    public void notifyException(String message) {
        System.out.println(message);
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


    public void printGoldFrontPoints(GoldFront face, AnsiColor cardType) {
        String goldFront = "";

        switch (face.getPoints()) {
            case 1 -> goldFront += AnsiColor.POINT_ONE.getFormattedCharacter();
            case 2 -> goldFront += AnsiColor.POINT_TWO.getFormattedCharacter();
            case 3 -> goldFront += AnsiColor.POINT_THREE.getFormattedCharacter();
            case 5 -> goldFront += AnsiColor.POINT_FIVE.getFormattedCharacter();
        }

        System.out.print(goldFront);

        if (face.getObjectNeeded() != null) {
            printCorner(face.getObjectNeeded(), cardType);
        }else if (face.isOverlapped()) {
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
        System.out.println("There's been a change in scores. The new ones are: ");
        for (Map.Entry<String, Integer> e : client.getVirtualView().getScores().entrySet())
            System.out.println(e.getKey() + ": " + e.getValue());
    }

    public void printGrid(Map<Coordinate, Face> grid){
        //todo: stampare il centro delle starter
        int xmin = getMinX(grid.keySet());
        int ymin = getMinY(grid.keySet());
        int xmax = getMaxX(grid.keySet());
        int ymax = getMaxY(grid.keySet());

        AnsiColor cardType;

        Object[] returnArray;

        System.out.print("\t");
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
                        System.out.print("    ");
                } else if(i%2 == 0 && j%2 == 0){
                    //se i è pari e j è pari, significa che è un centro --> stampo il centro
                    Coordinate coord = new Coordinate(i/2,j/2);

                    if(grid.containsKey(coord)) {
                        printCenter(coord, grid);
                    }else
                        System.out.print("            ");
                } else if ( i%2 == 0 && j%2 != 0){
                    //se i è pari e j è dispari, significa che è sopra/sotto il centro --> stampo il sopra/sotto (punti/ objectNeeded/...)

                    returnArray = searchCenterVerticalAndCardTypeAtPosition(new Coordinate(i,j), grid);
                    cardType = (AnsiColor) returnArray[0];

                    if(cardType != null)
                        printCenterVertical(cardType, (boolean)returnArray[1], grid, new Coordinate(i,j));
                    else
                        System.out.print("            ");
                } else if( i%2 != 0 && j%2 == 0){
                    //se i è dispari e j è pari, significa che è a destra/sinistra del centro --> stampo lati
                    returnArray = searchCenterHorizontalAndCardTypeAtPosition(new Coordinate(i,j), grid);
                    cardType = (AnsiColor) returnArray[0];

                    if(cardType != null)
                        printCenterHorizontal(cardType, (boolean)returnArray[1], grid, new Coordinate(i,j));
                    else
                        System.out.print("    ");
                }
            }

            System.out.println();

            if(j%2 != 0 && (j-1)/2 != ymin-1){
                System.out.print((j-1)/2 + "\t");
            }
            else
                System.out.print("\t");

        }
        System.out.print("\t");
        for(int i = xmin; i <= xmax; i++){
            System.out.print("\t\t"+i+"\t\t");
        }
        System.out.println();

    }

    public void printFace(Face face){
        HashMap<Coordinate, Face> grid = new HashMap<>();
        grid.put(new Coordinate(0,0), face);

        printGrid(grid);
    }

    public void printPlayerHand() {

        for (PlayableCard c : client.getVirtualView().getHandCards()) {
            System.out.println("Front of card " + c.getIdCard() + ":");
            printFace(c.getFront());
            if (c.getFront() instanceof GoldFront) {
                System.out.print("Costs: ");
                for (Content cost : ((GoldFront) c.getFront()).getCost()) {
                    System.out.print(cost.toString() + " ");
                }
                System.out.println();
            }
            System.out.println("Back of card " + c.getIdCard() + ":");
            printFace(c.getBack());
        }
        System.out.println();
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
            if (face instanceof GoldFront) {
                centerText += cardType.getFormattedCharacter();
                printGoldFrontPoints((GoldFront) face, cardType);
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
}
