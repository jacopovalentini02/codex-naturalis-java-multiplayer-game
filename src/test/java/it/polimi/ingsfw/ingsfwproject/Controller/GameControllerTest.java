package it.polimi.ingsfw.ingsfwproject.Controller;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    @Test
    void testChooseObjectiveCard() {
    }

    @Test
    void testPlayCard() {
    }

    @Test
    void testDrawDisplayedPlayableCard() {
    }

    @Test
    void testDraw() {
    }

    @Test
    void testChooseColor() {
    }

    @Test
    void testAddMessageToGlobalChat() {
    }

    @Test
    void testForwardPrivateChatMessage() {
    }

    @Test
    void testClientDisconnected() {
    }

    @Test
    void testSendTokenAvailable() {
    }
/*
    @Test
    public void test() throws TurnException, GamePhaseException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException, DeckEmptyException, ColorNotAvailableException, CardNotPresentException, DeckException {
        GameManager manager = new GameManager();
        manager.createGame(2, "Jaco");
        assertEquals(1, manager.getGameList().size());

        Game game1 = manager.getGameList().get(0);
        assert game1 != null;

        GameController controller = game1.getController();
        assert controller != null;

        assert game1.getState() == GameState.WAITING_FOR_PLAYERS;

        manager.joinGame("Bea", 0);

        Player jaco = game1.getListOfPlayers().getFirst();
        Player bea = game1.getListOfPlayers().get(1);

        assert Objects.equals(jaco.getUsername(), "Jaco");
        assert Objects.equals(bea.getUsername(), "Bea");

        assert game1.getListOfPlayers().size() == 2;
        assert game1.getState() == GameState.CHOOSING_STARTER_CARDS;

        for (Player p: game1.getListOfPlayers()) {
            assert p.getHandCard().size() == 1;
            assert p.getHandCard().getFirst() instanceof StarterCard;
        }

        controller.playCard(jaco, jaco.getHandCard().getFirst(), true, new Coordinate(0,0));

        assert jaco.getGround().getGrid().size() == 1;
        assert jaco.getHandCard().isEmpty();

        controller.playCard(bea, bea.getHandCard().getFirst(), true, new Coordinate(0,0));
        assert bea.getGround().getGrid().size() == 1;
        assert bea.getHandCard().isEmpty();

        assert game1.getState() == GameState.CHOOSING_COLORS;

        assert jaco.getHandCard().isEmpty();
        assert bea.getHandCard().isEmpty();

        assert game1.getCurrentPlayer() == jaco;

        controller.chooseColor(jaco, PlayerColor.BLUE);
        assert game1.getState() == GameState.CHOOSING_COLORS;
        assert game1.getCurrentPlayer() == bea;

        controller.chooseColor(bea, PlayerColor.RED);
        assert game1.getState() == GameState.CHOOSING_OBJECTIVES;
        assert game1.getCurrentPlayer() == jaco;

        assert jaco.getToken() == PlayerColor.BLUE && bea.getToken()==PlayerColor.RED;

        assert jaco.getHandCard().size() == 3;
        assert bea.getHandCard().size() == 3;

       assert game1.getState() == GameState.CHOOSING_OBJECTIVES;
       assertThrows(GamePhaseException.class, ()->controller.playCard(jaco, jaco.getHandCard().getFirst(),true, new Coordinate(1,1)));

       for (Player p : game1.getListOfPlayers())
           assert p.getHandObjective().size() == 2;

       int ObjectiveDecksizeprecall = game1.getObjectiveDeck().getCardList().size();

       controller.chooseObjectiveCard(jaco, jaco.getHandObjective().getFirst());
       assert jaco.getHandObjective().size() == 1;
       assert game1.getObjectiveDeck().getCardList().size() == ObjectiveDecksizeprecall + 1;
       assert game1.getState() == GameState.CHOOSING_OBJECTIVES;

       controller.chooseObjectiveCard(bea, bea.getHandObjective().getFirst());
       assert bea.getHandObjective().size() == 1;
       assert game1.getObjectiveDeck().getCardList().size() == ObjectiveDecksizeprecall + 2;

       assert game1.getState() == GameState.STARTED;

       for (Player p: game1.getListOfPlayers()) {
           assert p.getPoints() == 0;
           assert p.getHandCard().size() == 3;
           assert p.getHandObjective().size() == 1;
       }

       Player firstPlayer = game1.getCurrentPlayer();



       controller.playCard(firstPlayer, firstPlayer.getHandCard().getFirst(), true, firstPlayer.getGround().getAvailablePositions().getFirst());
       assert firstPlayer.getGround().getGrid().size() == 2;
       assert game1.getifCurrentPlayerhasPlayed();
       assert game1.getCurrentPlayer() == firstPlayer;


    }

*/


}