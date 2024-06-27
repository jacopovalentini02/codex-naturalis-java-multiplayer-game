package it.polimi.ingsfw.ingsfwproject.Exceptions;

/**
 * Exception thrown when draw is attempted on an empty deck.
 */
public class DeckEmptyException extends Exception {
    /**
     * Constructs a new DeckEmptyException with the specified detail message.
     *
     * @param message the detail message
     */
    public DeckEmptyException(String message) {
        super(message);
    }
}
