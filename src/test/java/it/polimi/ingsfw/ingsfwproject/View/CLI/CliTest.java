package it.polimi.ingsfw.ingsfwproject.View.CLI;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CliTest {

    @Test
    void testPrintFacePlayed() throws DeckEmptyException {
        Player player1=new Player("user1");
        Game game=new Game(new GameManager(),1,2,player1);
        game.setupField();
        Cli cli = new Cli();
        ResourceCard card = (ResourceCard) game.getResourceDeck().draw();
        Face face = card.getFront();
        cli.printFacePlayed(card, face);
    }
}