package it.polimi.ingsfw.ingsfwproject.View.CLI;

import it.polimi.ingsfw.ingsfwproject.Model.Game;
import it.polimi.ingsfw.ingsfwproject.Network.Client.Client;
import it.polimi.ingsfw.ingsfwproject.Network.Client.RMIClient;
import it.polimi.ingsfw.ingsfwproject.Network.Client.SocketClient;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.CreateGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.GetGameListMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServer.JoinGameMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;
import it.polimi.ingsfw.ingsfwproject.View.CLI.Cli;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        Cli view = null;

        do {
            System.out.println("Con che interfaccia vuoi giocare? \n1) CLI \n2) GUI");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice == 1 || choice == 2) {
                    break;
                }
            } else {
                scanner.next();
            }
            System.out.println("Input non valido. Riprova.");
        }while(true);

        if(choice == 1) {
            view = new Cli();
            Thread thread = new Thread(view);
            thread.start();
        }else if(choice == 2) {
            view = new GUIView();
            Thread thread = new Thread(view);
            thread.start();
        }


    }


}
