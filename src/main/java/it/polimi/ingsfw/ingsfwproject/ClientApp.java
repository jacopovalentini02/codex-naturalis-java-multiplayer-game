package it.polimi.ingsfw.ingsfwproject;

import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network2.Client.ClientSocket;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ClientToServer.GetGameList;
import it.polimi.ingsfw.ingsfwproject.Network2.Messages.ClientToServer.JoinGameMessage;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {
    public static void main(String [] args)  {
        Client client = null;

        Scanner scanner = new Scanner(System.in);
        try {
            int choice;

            System.out.println("Inserisci il tuo username:");
            String username = scanner.nextLine();

            do{
                System.out.println("Come ti vuoi connettere? ");
                System.out.println("1. Socket");
                System.out.println("2. RMI");
                choice = scanner.nextInt();
            }while(choice<1 || choice>2);

            if(choice==1)
                client = new ClientSocket(username, "127.0.0.1",1337);
            //TODO CREO CLIENT RMI


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


            client.startConnection();

            if(choice==1){
                System.out.println("Inserisci il numero di giocatori per questa partita, da 2 a 4: ");
                int numOfPlayers = scanner.nextInt();
                scanner.nextLine();
                //client.sendMessage(new CreateGameMessage(numOfPlayers, username));
            }else if(choice==2){
                //client.sendMessage(new GetGameList());
                //Server manda la list
                //client socket fa la print dei game in corso
                System.out.println("Inserisci ID del game a cui ti vuoi connettere");
                int gameID = scanner.nextInt();
                //client.sendMessage(new JoinGameMessage(username, gameID));

            }

        } catch (IOException e) {
            System.err.println(e.getMessage()); } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
