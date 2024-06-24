package it.polimi.ingsfw.ingsfwproject.Network.Client;

/**
 * Interface for listening to game status changes.
 */
public interface InGameListener {
    /**
     * Called when the game status changes.
     *
     * @param inGame the new game status
     */
    void onGameStatusChanged(boolean inGame);
}
