package it.polimi.ingsfw.ingsfwproject.Network2;
import it.polimi.ingsfw.ingsfwproject.Controller.LobbyController;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class RMIServer {

    public static void main(String[] args){

        GameManager manager = new GameManager();
        LobbyController lobbyController = new LobbyController(manager);


        try{
            LocateRegistry.createRegistry(1099);
            ClientHandlerFactory clientHandlerFactory = new ClientHandlerFactory(lobbyController, manager);
            Naming.rebind("Factory", clientHandlerFactory);
            System.out.println("Factory has been successfully registered");
            System.out.println("Waiting for connections..");

        } catch (RemoteException e) {
            System.out.println("A RemoteException occoured: " + e.getMessage());
            e.printStackTrace();
            return;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Scanner scan = new Scanner(System.in);
        while (true){
            System.out.print("> ");
            int command = scan.nextInt();
            if (command == 0) {
                break;
            } else {
                System.out.println("Invalid command. Type 0 to shut the server down.");
            }
        }
    }
}
