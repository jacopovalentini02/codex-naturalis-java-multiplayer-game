package it.polimi.ingsfw.ingsfwproject;

import it.polimi.ingsfw.ingsfwproject.Network.Server.ServerApp;
import it.polimi.ingsfw.ingsfwproject.View.CLI.Cli;
import it.polimi.ingsfw.ingsfwproject.View.ClientApp;
import it.polimi.ingsfw.ingsfwproject.View.GUI.GUIView;

import java.util.Scanner;

public class MainApp {
        public static void main(String[] args){
                Scanner scanner = new Scanner(System.in);
                int choice;



                do {

                        System.out.println("do you want to start server or client?");
                        System.out.println("1. Start server");
                        System.out.println("2. Start client");
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
                        ServerApp serverApp = new ServerApp();
                        serverApp.start();
                }else{
                        ClientApp clientApp = new ClientApp();
                        clientApp.start();
                }


        }
}
