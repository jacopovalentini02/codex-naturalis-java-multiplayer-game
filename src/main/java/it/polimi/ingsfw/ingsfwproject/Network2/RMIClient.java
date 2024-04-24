package it.polimi.ingsfw.ingsfwproject.Network2;
import it.polimi.ingsfw.ingsfwproject.Exceptions.NotValidNumOfPlayerException;
import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;

public class RMIClient {

    public static void main(String [] args) throws RemoteException, MalformedURLException, NotBoundException, NotValidNumOfPlayerException {

        Registry registry = LocateRegistry.getRegistry("localhost", 1099);

        String remoteObjectName = "Factory";
        ClientHandlerFactoryInterface clientHandlerFactory = (ClientHandlerFactoryInterface) registry.lookup(remoteObjectName);

        if (clientHandlerFactory != null)
            System.out.println("Success");

        assert clientHandlerFactory != null;
        LobbyClientHandlerInterface prova = (LobbyClientHandlerInterface) clientHandlerFactory.getLobbyHandler();

        if (prova != null)
            System.out.println("YO");

        int id = prova.createGame(4,"Jaco");

        System.out.println(id);

        GameClientHandlerInterface game = (GameClientHandlerInterface) clientHandlerFactory.getGameHandler(id);

        if (game != null)
            System.out.println("SUCCESS");

        Player player = new Player("Gigio");
        Card card = new StructuredObjectiveCard(1, 1, Structure.RIGHT_DIAGONAL, new ArrayList<>());

        System.out.println(game.sum(player,2));

        game.chooseObjectiveCard(player, card);

    }
}
