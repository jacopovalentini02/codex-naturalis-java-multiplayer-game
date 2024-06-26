package it.polimi.ingsfw.ingsfwproject.Controller;


import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Model.*;
import it.polimi.ingsfw.ingsfwproject.Network.Server.GameServerInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GameControllerTest {
    private GameServerInstance gameServerInstance;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Game game;

    private GameManager manager;
    @BeforeEach
    void setUp() {
        gameServerInstance=new GameServerInstance();
        player1 = new Player("user1", gameServerInstance, 0);
        game = new Game(gameServerInstance,new GameManager(), 1, 4, player1);
        player2 = new Player("user2", gameServerInstance, 1);
        player3 = new Player("user3", gameServerInstance,2);
        player4 = new Player("user4", gameServerInstance,3);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);

        game.setupField();
        game.setupHandsAndObjectives();

        manager = new GameManager();


    }
    @Test
    void testChooseObjectiveCardNoCurrentPlayer() {
        game.setCurrentPlayer(player1);
        game.getController().chooseObjectiveCard(player2.getUsername(), player2.getHandObjective().getFirst().getIdCard());
    }

    @Test
    void testChooseObjectivePhaseException(){
        game.setState(GameState.ENDING);
        game.setCurrentPlayer(player1);
        game.getController().chooseObjectiveCard(player1.getUsername(),player1.getHandObjective().getFirst().getIdCard());

    }


    @Test
    void testDrawDisplayedPlayableCard() {
        game.setState(GameState.STARTED);
        game.setCurrentPlayer(player1);

        game.getController().playCard(player1.getUsername(),player1.getHandCard().getFirst().getIdCard(), true, new Coordinate(0,0));
        game.setCurrentPlayer(player1);

        game.setCurrentPlayerhasPlayed(true);
        game.getController().drawDisplayedPlayableCard(player1.getUsername(),game.getDisplayedPlayableCard().getFirst().getIdCard());
        assert player1.getHandCard().size()==4;
    }

    @Test
    void testDrawDisplayedPlayableCard_CardDoesNotExist() {
        game.setState(GameState.STARTED);
        game.setCurrentPlayer(player1);
        game.getController().playCard(player1.getUsername(),player1.getHandCard().getFirst().getIdCard(), true, new Coordinate(0,0));
        game.getController().drawDisplayedPlayableCard(player1.getUsername(), 9999); // ID di carta che non esiste
        assert player1.getHandCard().size() == 3;
    }

    @Test
    void testDrawDisplayedPlayableCard_DrawNotPossible() {
        game.setState(GameState.STARTED);
        game.setCurrentPlayer(player1);
        game.getController().playCard(player1.getUsername(),player1.getHandCard().getFirst().getIdCard(), true, new Coordinate(0,0));
        player1.setCanPlay(false);
        game.getController().drawDisplayedPlayableCard(player1.getUsername(), game.getDisplayedPlayableCard().getFirst().getIdCard());
        assert player1.getHandCard().size() == 3;
    }

    @Test
    void testPickLastTurn(){
        game.setState(GameState.STARTED);
        game.setCurrentPlayer(player1);
        game.setPotentialWinner(player1);
        game.setCurrentPlayerhasPlayed(true);
        game.getController().drawDisplayedPlayableCard(player1.getUsername(), game.getDisplayedPlayableCard().getFirst().getIdCard());
        assertEquals(game.getState(), GameState.ENDING);
    }

    @Test
    void testDraw_FromResourceDeck() {
        game.setState(GameState.STARTED);
        game.setCurrentPlayer(player1);

        game.getController().playCard(player1.getUsername(),player1.getHandCard().getFirst().getIdCard(), true, new Coordinate(0,0));
        game.setCurrentPlayer(player1);

        game.setCurrentPlayerhasPlayed(true);
        game.getController().draw(player1.getUsername(), true);
        assert player1.getHandCard().size() == 4;
    }

    @Test
    void testDraw_FromGoldDeck() {
        game.setState(GameState.STARTED);
        game.setCurrentPlayer(player1);

        game.getController().playCard(player1.getUsername(),player1.getHandCard().getFirst().getIdCard(), true, new Coordinate(0,0));
        game.setCurrentPlayer(player1);

        game.setCurrentPlayerhasPlayed(true);
        game.getController().draw(player1.getUsername(), false);
        assert player1.getHandCard().size() == 4;
    }

    @Test
    void testCheckIfDrawPossible_GameStateNotAllowed() {
        game.setCurrentPlayer(player1);
        game.getController().playCard(player1.getUsername(),player1.getHandCard().getFirst().getIdCard(), true, new Coordinate(0,0));
        GameState[] notAllowedStates = {
                GameState.WAITING_FOR_PLAYERS,
                GameState.CHOOSING_STARTER_CARDS,
                GameState.CHOOSING_OBJECTIVES,
                GameState.ENDING,
                GameState.CHOOSING_COLORS
        };

        for (GameState ignored : notAllowedStates) {
            game.getController().draw(player1.getUsername(), false);

        }
    }

    @Test
    void testCheckIfDrawPossible_PlayerCantPlay(){
        game.setState(GameState.STARTED);
        game.setCurrentPlayer(player1);
        player1.setCanPlay(false);
        game.getController().draw(player1.getUsername(), false);
    }

    @Test
    void testCheckIfDrawLastTurn(){
        game.setState(GameState.STARTED);
        game.setCurrentPlayer(player1);
        game.setPotentialWinner(player1);
        game.setCurrentPlayerhasPlayed(true);
        game.getController().draw(player1.getUsername(), false);
        assertEquals(game.getState(), GameState.ENDING);
    }


    @Test
    void testChooseColorWrongTurn() {
        game.setState(GameState.CHOOSING_COLORS);
        game.setCurrentPlayer(player1);
        game.getController().chooseColor(player2.getUsername(),PlayerColor.BLUE);
    }

    @Test
    void testChooseColorWrongState() {
        game.setState(GameState.ENDING);
        game.setCurrentPlayer(player1);
        game.getController().chooseColor(player1.getUsername(),PlayerColor.BLUE);
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

    @Test
    public void testAddMessageToGlobalChat() {
        ChatMessage message = new ChatMessage("sender", "recipient", "Hello, World!");
        game.getController().addMessageToGlobalChat(message);
        assertEquals(1, game.getController().globalChat.size());
        assertEquals(message, game.getController().globalChat.peek());

    }

    @Test
    public void testGetPlayer() {
        gameServerInstance=new GameServerInstance();
        player1 = new Player("user1", gameServerInstance, 0);
        Game game2 = new Game(gameServerInstance,new GameManager(), 1, 4, player1);
        Player actualPlayer = game2.getController().getPlayer("user1");
        assertEquals(player1, actualPlayer);
    }


    @Test
    public void test() throws TurnException, GamePhaseException, PositionNotAvailableException, NotEnoughResourcesException, CardNotInHandException, DeckEmptyException, ColorNotAvailableException, CardNotPresentException, DeckException {
        manager.createGame(2, "Jaco", 0);
        assertEquals(1, manager.getGameList().size());

        Game game1 = manager.getGameList().get(0);
        assert game1 != null;

        GameController controller = game1.getController();
        assert controller != null;

        assert game1.getState() == GameState.WAITING_FOR_PLAYERS;

        manager.joinGame("Bea", 0, 1);

        Player jaco = game1.getListOfPlayers().getFirst();
        Player bea = game1.getListOfPlayers().get(1);

        assert Objects.equals(jaco.getUsername(), "Jaco");
        assert Objects.equals(bea.getUsername(), "Bea");

        assert game1.getListOfPlayers().size() == 2;
        assert game1.getState() == GameState.WAITING_FOR_PLAYERS;

        game1.setupField();
        for (Player p: game1.getListOfPlayers()) {
            assert p.getHandCard().size() == 1;
            assert p.getHandCard().getFirst() instanceof StarterCard;
        }

        controller.playCard(jaco.getUsername(), jaco.getHandCard().getFirst().getIdCard(), true, new Coordinate(0,0));

        assert jaco.getGround().getGrid().size() == 1;
        assert jaco.getHandCard().isEmpty();

        controller.playCard(bea.getUsername(), bea.getHandCard().getFirst().getIdCard(), true, new Coordinate(0,0));
        assert bea.getGround().getGrid().size() == 1;
        assert bea.getHandCard().isEmpty();

        assert game1.getState() == GameState.CHOOSING_COLORS;

        assert jaco.getHandCard().isEmpty();
        assert bea.getHandCard().isEmpty();

        assert game1.getCurrentPlayer() == jaco;

        controller.chooseColor(jaco.getUsername(), PlayerColor.BLUE);
        assert game1.getState() == GameState.CHOOSING_COLORS;
        assert game1.getCurrentPlayer() == bea;

        controller.chooseColor(bea.getUsername(), PlayerColor.RED);
        assert game1.getState() == GameState.CHOOSING_OBJECTIVES;
        assert game1.getCurrentPlayer() == jaco;

        assert jaco.getToken() == PlayerColor.BLUE && bea.getToken()==PlayerColor.RED;

        assert jaco.getHandCard().size() == 3;
        assert bea.getHandCard().size() == 3;

       assert game1.getState() == GameState.CHOOSING_OBJECTIVES;
       //assertThrows(GamePhaseException.class, ()->controller.playCard(jaco.getUsername(), jaco.getHandCard().getFirst().getIdCard(),true, new Coordinate(1,1)));

       for (Player p : game1.getListOfPlayers())
           assert p.getHandObjective().size() == 2;

       int ObjectiveDecksizeprecall = game1.getObjectiveDeck().getCardList().size();

       controller.chooseObjectiveCard(jaco.getUsername(), jaco.getHandObjective().getFirst().getIdCard());
       assert jaco.getHandObjective().size() == 1;
       assert game1.getObjectiveDeck().getCardList().size() == ObjectiveDecksizeprecall + 1;
       assert game1.getState() == GameState.CHOOSING_OBJECTIVES;

       controller.chooseObjectiveCard(bea.getUsername(), bea.getHandObjective().getFirst().getIdCard());
       assert bea.getHandObjective().size() == 1;
       assert game1.getObjectiveDeck().getCardList().size() == ObjectiveDecksizeprecall + 2;

       //game1.setupHandsAndObjectives();

        assert game1.getState() == GameState.STARTED;

       for (Player p: game1.getListOfPlayers()) {
           //assert p.getPoints() == 0;
           assert p.getHandCard().size() == 3;
           assert p.getHandObjective().size() == 1;
       }

       Player firstPlayer = game1.getCurrentPlayer();



       controller.playCard(firstPlayer.getUsername(), firstPlayer.getHandCard().getFirst().getIdCard(), true, firstPlayer.getGround().getAvailablePositions().getFirst());
       assert firstPlayer.getGround().getGrid().size() == 2;
       assert game1.getifCurrentPlayerhasPlayed();
       assert game1.getCurrentPlayer() == firstPlayer;


    }




}