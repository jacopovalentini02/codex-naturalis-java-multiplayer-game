package it.polimi.ingsfw.ingsfwproject.View2;

import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.JoinGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ServerToClient.SendGameListMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Scanner;

public class CLI extends View implements Runnable {
    Scanner scanner;

    public CLI(){
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void scegliPrimaAzione(){
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
            creaPartita();
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
    public void scegliMetodoConnessione(){
        String ip;
        int port;
        int option = 0;
        Message messageToSend;

        do {
            if(option <0 || option > 1)
                System.out.println("opzione non valida");
            System.out.println("vuoi usare RMI o Socket? 1 per RMI, 2 per Socket");
            if(scanner.hasNextInt())
                option = scanner.nextInt();

        }while(option <0 || option > 1);

        System.out.println("Inserisci l'IP del server: ");
        ip = scanner.nextLine();

        System.out.println("Inserisci la porta a cui connettersi");
        port = scanner.nextInt();


        try {
            this.client = (option == 0? new RMIClient(ip,port) : new SocketClient(ip,port));
            client.startConnection();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void creaPartita() {
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
    public void scegliPartitaEUnisciti(HashMap<Integer, Integer> gamesMap) {
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
        this.scegliMetodoConnessione();
        this.scegliPrimaAzione();

        while (true){
            synchronized (messages) {
                while (messages.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                handleMessage(messages.remove());
            }
        }

    }

    @Override
    public void handleMessage(Message message){
        switch (message.getType()) {
            case SEND_GAME_LIST :
                scegliPartitaEUnisciti(((SendGameListMessage) message).getGameList());
        }
    }


}
