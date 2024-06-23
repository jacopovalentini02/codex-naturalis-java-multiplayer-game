package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serializable;

/**
 * This enum class represents all the possible state of a game.
 */
public enum GameState implements Serializable {
    //do not change order!
    WAITING_FOR_PLAYERS,
    CHOOSING_STARTER_CARDS,
    CHOOSING_COLORS,
    CHOOSING_OBJECTIVES,
    STARTED,
    ENDING,
    ENDED

}
