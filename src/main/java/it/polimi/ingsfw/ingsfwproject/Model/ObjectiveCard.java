package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serial;

/**
 * Class ObjectiveCard
 *
 * Description: This abstract class represents an objective card in the game. It extends the Card class and adds a points attribute. Subclasses should provide the implementation for verifying objectives.
 */
abstract public class ObjectiveCard extends Card {
    @Serial
    private static final long serialVersionUID = -428064471331310551L;
    private final int points;

    /**
     * Constructor for the ObjectiveCard class.
     *
     * @param idCard The ID of the card.
     * @param points The points awarded for completing the objective.
     */
    public ObjectiveCard(int idCard, int points) {
        super(idCard);
        this.points = points;
    }

    /**
     * @return The points awarded for completing the objective.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Verifies if the objective is completed based on the player's ground.
     * Subclasses should provide the specific implementation for this method.
     *
     * @param ground The player's ground to check for the required objects.
     * @return The points awarded for completing the objective.
     */
    public abstract int verifyObjective(PlayerGround ground);
}
