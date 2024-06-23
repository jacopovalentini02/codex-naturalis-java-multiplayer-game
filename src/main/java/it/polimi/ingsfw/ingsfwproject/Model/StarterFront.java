package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.*;

/**
 * The {@code StarterFront} class represents the front face of a {@code StarterCard}
 * This class is a subclass of {@code Face} and it adds the central {@code Content} list.
 */
public class StarterFront extends Face{
    private ArrayList<Content> center;

    /**
     * Constructs a new {@code StarterFront} with the specified values.
     * @param id the card's id it belongs to
     * @param corners the array that contains the {@code Content} of each corner. The contents' order must be:
     *                <ul>
     *                <li>corners[0] for the top left {@code Content}</li>
     *                <li>corners[1] for the top right {@code Content}</li>
     *                <li>corners[2] for the bottom left {@code Content}</li>
     *                <li>corners[3] for the bottom right {@code Content}</li>
     *                </ul>
     * @param center the central {@code Content} list
     * @param imagePath the path of the image that represents this {@code StarterFront}. This image will be used in GUI.
     */
    public StarterFront(int id, Content[] corners, ArrayList<Content> center, String imagePath) {
        super(id, corners, imagePath);
        this.center = center;
    }

    /**
     * Returns an ArrayList with the central {@code Content} list.
     * @return an ArrayList with the central {@code Content} list
     */

    public ArrayList<Content> getCenter() {
        return center;
    }
}
