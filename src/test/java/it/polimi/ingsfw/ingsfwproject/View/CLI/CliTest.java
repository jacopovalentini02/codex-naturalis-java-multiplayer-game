package it.polimi.ingsfw.ingsfwproject.View.CLI;



import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Model.Player;
import it.polimi.ingsfw.ingsfwproject.Network2.ClientCallback;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class CliTest {

    @Test
    void testPrintFacePlayed() throws DeckEmptyException, RemoteException {
        Player player1=new Player("user1");
        Game game=new Game(new GameManager(),1,2,player1);
        ClientCallback clientCallback = new ClientCallback();
        game.getController().addClient("user1", clientCallback);
        game.setupField();
        Cli cli = new Cli();
        ResourceCard card = (ResourceCard) game.getResourceDeck().draw();
        Face face = card.getFront();
        cli.printFacePlayed(face);
    }

    @Test
    void testPrintGrid() throws DeckEmptyException, RemoteException {

    }


    @Test
    void testPrintCorner() {
    }

    @Test
    void testGetCardType() {
    }

    @Test
    void testPrintPlayerHand() throws RemoteException, DeckEmptyException {
        Player player1=new Player("user1");
        Game game=new Game(new GameManager(),1,2,player1);
        ClientCallback clientCallback = new ClientCallback();
        game.getController().addClient("user1", clientCallback);
        game.setupField();
        Cli cli = new Cli();
        player1.getHandCard().remove(0);
        player1.getHandCard().add((ResourceCard) game.getResourceDeck().draw());
        player1.getHandCard().add((ResourceCard) game.getResourceDeck().draw());
        player1.getHandCard().add((ResourceCard) game.getResourceDeck().draw());
        cli.printPlayerHand(player1);
    }
}