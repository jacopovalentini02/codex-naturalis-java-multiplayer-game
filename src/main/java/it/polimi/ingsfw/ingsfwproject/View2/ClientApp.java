/*package it.polimi.ingsfw.ingsfwproject.View2;

import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        View view = null;

        do {
            System.out.println("Which interface you want to play with? \n1) CLI \n2) GUI");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1 || choice == 2) {
                    break;
                }
            } else {
                scanner.next();
            }
            System.out.println("invalid input, try again.");
        }while(true);

        if(choice == 1) {
            view = new CLI();
            Thread thread = new Thread(view);
            thread.start();
        }else if(choice == 2) {
            //todo: view = new GUI();
        }


    }


}*/
