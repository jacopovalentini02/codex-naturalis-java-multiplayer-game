package it.polimi.ingsfw.ingsfwproject.Controller;

import it.polimi.ingsfw.ingsfwproject.Exceptions.*;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.ClientToServerMessage;
import it.polimi.ingsfw.ingsfwproject.Network.Messages.Message;

public interface Controller {

    public void handleMessage(ClientToServerMessage m) throws NickAlreadyTakenException, TurnException, GameFullException, GameNotExistingException, DeckEmptyException, DeckException, NotValidNumOfPlayerException, GamePhaseException, PositionNotAvailableException, ColorNotAvailableException, NotEnoughResourcesException, CardNotPresentException, CardNotInHandException;

}
