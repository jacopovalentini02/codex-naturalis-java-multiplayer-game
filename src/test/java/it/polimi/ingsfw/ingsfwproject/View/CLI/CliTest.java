package it.polimi.ingsfw.ingsfwproject.View.CLI;
import it.polimi.ingsfw.ingsfwproject.Network.Client.*;
import it.polimi.ingsfw.ingsfwproject.View.*;


import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Model.GameManager;
import it.polimi.ingsfw.ingsfwproject.Model.Player;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class CliTest {
    /*
    @Test
    void testPrintFacePlayed() throws DeckEmptyException, RemoteException {
        GameServerInstance gameServerInstance=new GameServerInstance();
        Player player1=new Player("user1", gameServerInstance, 0);
        Game game=new Game(gameServerInstance,new GameManager(),1,2,player1);
        //ClientCallback clientCallback = new ClientCallback();
        //game.getController().addClient("user1", clientCallback);
        game.setupField();
        Cli cli = new Cli();
        ResourceCard card = (ResourceCard) game.getResourceDeck().draw();
        Face face = card.getFront();
        cli.printFacePlayed(face);
    }

    @Test
    void testPrintGrid() throws DeckEmptyException, RemoteException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException {
        GameManager gameManager = new GameManager();
        GameServerInstance gameserver = new GameServerInstance();
        Player player = new Player("peppo", gameserver, 0);
        gameserver.addPlayer(player, 0);
        Game game = new Game(gameserver, gameManager, 0, 2, player);
        game.setUpCards();
        game.setupField();
        player.getHandCard().remove(0);
        game.setupHandsAndObjectives();
        player.getHandCard().remove(2);
        player.draw(game.getResourceDeck());
        Cli cli = new Cli();
        cli.printPlayerHand();
        player.playCard(player.getHandCard().get(0), true, new Coordinate(0,0));
        player.playCard(player.getHandCard().get(0), true, new Coordinate(1,1));
        player.playCard(player.getHandCard().get(0), true, new Coordinate(1, -1));
        System.out.println("ecco la grid:");
        cli.printGrid(player.getGround());
    }


    @Test
    void testPrintCorner() {
    }

    @Test
    void testGetCardType() {
    }

    @Test
    void testPrintPlayerHand() throws RemoteException, DeckEmptyException {
        GameManager gameManager = new GameManager();
        GameServerInstance gameserver = new GameServerInstance();
        Player player = new Player("peppo", gameserver, 0);
        gameserver.addPlayer(player, 0);
        Game game = new Game(gameserver, gameManager, 0, 2, player);
        game.setUpCards();
        game.setupField();
        player.getHandCard().remove(0);
        game.setupHandsAndObjectives();
        Cli cli = new Cli();
        Client client = new RMIClient("localhost", 1099, cli);
        cli.printPlayerHand();
    }
    */

    @Test
    void testPrintFace(){
        GameManager gameManager = new GameManager();
        GameServerInstance gameserver = new GameServerInstance();
        Player player = new Player("peppo", gameserver, 0);
        gameserver.addPlayer(player, 0);
        Game game = new Game(gameserver, gameManager, 0, 2, player);
        game.setupField();

        Cli cli =  new Cli();
        Client client = new SocketClient("localhost", 1337, cli);
        VirtualView virtualView = new VirtualView();
        client.setVirtualView(virtualView);


        Card card = game.getGoldDeck().getCardList().getFirst();
        System.out.println(card.getIdCard());
        cli.printFace(((GoldCard)card).getFront());
    }

    @Test
    void testPrintGrid(){
        GameManager gameManager = new GameManager();
        GameServerInstance gameserver = new GameServerInstance();
        Player player = new Player("peppo", gameserver, 0);
        gameserver.addPlayer(player, 0);
        Game game = new Game(gameserver, gameManager, 0, 2, player);
        game.setupField();

        Cli cli =  new Cli();
        Client client = new SocketClient("localhost", 1337, cli);
        VirtualView virtualView = new VirtualView();
        client.setVirtualView(virtualView);

        Map<Coordinate, Face> grid = new HashMap<>();
        for(int i = 0; i<15; i++) {
            Card card = game.getGoldDeck().getCardList().removeFirst();
            grid.put(new Coordinate(i, i), ((GoldCard) card).getFront());
            System.out.println(card.getIdCard());
        }

        cli.printGrid(grid);
    }

    @Test
    void printStarter(){
        GameServerInstance gameServerInstance = new GameServerInstance();
        Player p1 = new Player("player1", gameServerInstance, 0);
        PlayerGround ground = p1.getGround();
        GameManager manager = new GameManager();
        Game game = new Game(gameServerInstance, manager, 1, 4, p1);
        game.setUpCards();

        Cli cli =  new Cli();
        Client client = new SocketClient("localhost", 1337, cli);
        VirtualView virtualView = new VirtualView();
        client.setVirtualView(virtualView);

        Map<Coordinate, Face> grid = new HashMap<>();

        for(int i = 0; i<6; i++) {
            Card card = game.getStarterDeck().getCardList().removeFirst();
            grid.put(new Coordinate(0, -6*i), ((StarterCard) card).getFront());
            grid.put(new Coordinate(0, (-6*i)-2), ((StarterCard) card).getBack());

            System.out.println(card.getIdCard());
        }

        cli.printGrid(grid);
    }
}