package it.polimi.ingsfw.ingsfwproject.Exceptions;

/**
 * Exception thrown when there are not enough resources in the playground
 * as required by the card.
 */
public class NotEnoughResourcesException extends Exception{
    /**
     * Constructs a new NotEnoughResourcesException with the specified detail message.
     *
     * @param message the detail message
     */
    public NotEnoughResourcesException(String message){
        super(message);
    }
}
