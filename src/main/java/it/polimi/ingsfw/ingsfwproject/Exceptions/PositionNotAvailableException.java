package it.polimi.ingsfw.ingsfwproject.Exceptions;

/**
 * Exception thrown when a position is not available for a specific operation.
 */
public class PositionNotAvailableException extends Exception{
    /**
     * Constructs a new PositionNotAvailableException with the specified detail message.
     *
     * @param message the detail message
     */
    public PositionNotAvailableException(String message){
        super(message);
    }
}
