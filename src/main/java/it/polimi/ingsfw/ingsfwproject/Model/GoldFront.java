package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serial;
import java.util.*;

/**
* The {@code GoldFront} class represents the front face of a {@code GoldCard}.
 * This class is a subclass of {@code NormalFace}, and it provides additional functionalities specific to the GoldCard.
 *
 * <p>In addition to the inherited properties and methods from {@code NormalFace}, the {@code GoldFront} class includes:</p>
 *
 * <ul>
 *   <li>{@code cost} - The resources required to place the card in the play area.</li>
 *   <li>{@code overlapped} - A boolean indicating the condition for scoring points:
 *     <ul>
 *       <li>If {@code true}, points are awarded based on covered corners.</li>
 *       <li>If {@code false}, points are awarded based on visible objects in the play area.</li>
 *     </ul>
 *   </li>
 *   <li>{@code objectNeeded} - In the case when {@code overlapped} is {@code false}, this represents the visible objects in the play area that grant points.</li>
 * </ul>
*/
public class GoldFront extends NormalFace {
    @Serial
    private static final long serialVersionUID = -6831857852149718598L;
    private final ArrayList<Content> cost;
    private final boolean overlapped;
    private final Content objectNeeded;

    /**
     * Constructs a {@code GoldFront} with the specifies values.
     *
     * @param id the card's id it belongs to
     * @param points the points given by this face
     * @param corners  the array that contains the {@code Content} of each corner. The contents' order must be:
     *                <ul>
     *                <li>corners[0] for the top left {@code Content}</li>
     *                <li>corners[1] for the top right {@code Content}</li>
     *                <li>corners[2] for the bottom left {@code Content}</li>
     *                <li>corners[3] for the bottom right {@code Content}</li>
     *                </ul>
     * @param cost the resources required to place the card in the play area
     * @param overlapped A boolean indicating the condition for scoring points:
     *     <ul>
     *       <li>If {@code true}, points are awarded based on covered corners.</li>
     *       <li>If {@code false}, points are awarded based on visible objects in the play area.</li>
     *     </ul>
     * @param objectNeeded in the case when {@code overlapped} is {@code false}, this represents the visible objects in the play area that grant points
     * @param imagePath the path of the image that represents this {@code GoldFront}. This image will be used in GUI.
     */

    public GoldFront(int id, int points, Content[] corners, ArrayList<Content> cost, boolean overlapped, Content objectNeeded, String imagePath) {
        super(id, points, corners, imagePath);
        this.cost = cost;
        this.overlapped = overlapped;
        this.objectNeeded = objectNeeded;
    }

    /**
     * Returns whether the scoring for this card is based on covered corners or visible objects on the play area.
     *
     * @return {@code true} if points are awarded based on covered corners;
     *         {@code false} if points are awarded based on visible objects in the play area.
     */
    public boolean isOverlapped() {
        return overlapped;
    }

    /**
     * Returns the content object representing the object in the play area that gives points.
     *
     * @return The {@code Content} object needed for scoring points if {@code overlapped} is {@code false}, {@code null} otherwise
     */
    public Content getObjectNeeded() {
        return objectNeeded;
    }

    /**
     * Returns the list of resources required to place the card in the play area.
     *
     * @return An {@code ArrayList} of {@code Content} objects representing the cost to place the card.
     */
    public ArrayList<Content> getCost() {
        return cost;
    }
}
