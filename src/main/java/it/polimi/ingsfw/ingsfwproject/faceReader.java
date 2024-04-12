package it.polimi.ingsfw.ingsfwproject;
import java.util.Scanner;
public class faceReader {
        public static boolean getBoolean() {
            // Creazione di un oggetto Scanner per leggere l'input dell'utente
            Scanner scanner = new Scanner(System.in);

            // Chiedi all'utente di inserire un valore booleano
            System.out.print("Inserisci come vuoi giocare la carta: fronte(true) o retro(false): ");

            // Leggi il valore booleano inserito dall'utente
            boolean valoreBooleano = scanner.nextBoolean();

            scanner.close();

            return valoreBooleano;

            // Chiudi lo scanner per liberare le risorse
        }

}
