package it.polimi.ingsfw.ingsfwproject.Network2;
import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RMIClient {

    public static <Map> void main(String [] args) throws RemoteException, MalformedURLException, NotBoundException, NotValidNumOfPlayerException {

        Registry registry = LocateRegistry.getRegistry("localhost", 1099);

        String remoteObjectName = "Factory";
        ClientHandlerFactoryInterface clientHandlerFactory = (ClientHandlerFactoryInterface) registry.lookup(remoteObjectName);

        if (clientHandlerFactory != null)
            System.out.println("Success");

        assert clientHandlerFactory != null;
        LobbyClientHandlerInterface lobbyHandler = (LobbyClientHandlerInterface) clientHandlerFactory.getLobbyHandler();

        System.out.println("Lista di partite in corso sul server: ");
        System.out.printf("%-10s %-20s %n", "ID", "Numero di Giocatori");
        HashMap<Integer, Integer> gamesMap = lobbyHandler.getGameList();

        for (HashMap.Entry<Integer, Integer> entry : gamesMap.entrySet()) {
            System.out.printf("%-10d %-20d %n", entry.getKey(), entry.getValue());
        }

        Scanner scanner = new Scanner(System.in);
        int choice = 0;

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

        scanner.nextLine();
        int id = -1;
        String username = null;

        if (choice == 1){
            do {
                try {
                    System.out.println("Inserisci il numero di giocatori per questa partita, da 2 a 4: ");
                    int numOfPlayers = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Inserisci il tuo username:");
                    username = scanner.nextLine();
                    id = lobbyHandler.createGame(numOfPlayers, username);
                } catch (NotValidNumOfPlayerException e){
                    System.out.println(e.getMessage());
                }
            } while (id == -1);
        }

        if (choice == 2){
            do {
                try {
                    System.out.printf("%-10s %-20s %n", "ID", "Numero di Giocatori");
                    gamesMap = lobbyHandler.getGameList();
                    for (HashMap.Entry<Integer, Integer> entry : gamesMap.entrySet()) {
                        System.out.printf("%-10d %-20d %n", entry.getKey(), entry.getValue());
                    }
                    System.out.println("Inserisci l'ID della partita a cui vuoi unirti: ");
                    int GameID = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Inserisci il tuo username: ");
                    username = scanner.nextLine();
                    id = lobbyHandler.joinGame(GameID, username);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            } while (id == -1);
        }

        GameClientHandlerInterface gameHandler = (GameClientHandlerInterface) clientHandlerFactory.getGameHandler(id);
        if (gameHandler != null) {
            System.out.println("Successfully obtained GameClientHandler");
        } else {
            System.out.println("Error occoured while obtaining ClientHandler");
            return;
        }

        ClientCallbackInterface clientCallback = new ClientCallback();
        gameHandler.registerClient(clientCallback, username);

        System.out.println("Successfully registered ClientListener to Server");

        ClientCallback gameStatus = (ClientCallback) clientCallback;

        Player me = gameStatus.getPlayer();

        System.out.println(me.getUsername());

        while (gameStatus.getState() == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        while (gameStatus.getState() != GameState.ENDED){
            System.out.println("Actual game state is " + gameStatus.getState());
            String input = scanner.nextLine();
            boolean validInput = false;

            while(!validInput){

                switch (input.toLowerCase()){

                    case "playcard":{
                        System.out.println("You have these cards in your hand: ");
                        for (PlayableCard c : gameStatus.getHandCards()){
                            System.out.print(c.getIdCard());
                        }
                        System.out.println("Which card do you want to play? Type the ID of the card:");
                        int cardID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Do you want to play it upwards or backwards? Type 1 for upwards, 2 for backwards");
                        int orientationChoice = scanner.nextInt();
                        boolean upwards;
                        if (orientationChoice == 1) {
                            upwards = true;
                        } else {
                            upwards = false;
                        }
                        PlayableCard cardToPlay = null;
                        for (PlayableCard p: gameStatus.getHandCards()){
                            if (p.getIdCard() == cardID)
                                cardToPlay = p;
                        }
                        System.out.println("In which position you want to play the card?");

                        for (Coordinate c: gameStatus.getAvailablePositions())
                            System.out.print(c.toString());

                        System.out.println("Type the x coordinate");
                        int x = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Type the y coordinate");
                        int y = scanner.nextInt();
                        try {
                            gameHandler.playCard(me, cardToPlay, upwards, new Coordinate(x, y));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        validInput = true;
                        break;
                    }












            }







            }




        }


    }
}
