package it.polimi.ingsfw.ingsfwproject.Model;

/**
 * The {@code NormalFace} class represents the front face of a {@code ResourceCard} or
 * the back face of a {@code StarterCard}.
 * This class is a subclass of {@code Face} and it adds points attribute.
 */
public class NormalFace extends Face{
    private int points;

    /**
     * Constructs a new {@code NormalFace} with the specified values.
     * @param id the card's id it belongs to
     * @param points the points given by this face
     * @param corners the array that contains the {@code Content} of each corner. The contents' order must be:
     *                <ul>
     *                <li>corners[0] for the top left {@code Content}</li>
     *                <li>corners[1] for the top right {@code Content}</li>
     *                <li>corners[2] for the bottom left {@code Content}</li>
     *                <li>corners[3] for the bottom right {@code Content}</li>
     *                </ul>
     * @param imagePath the path of the image that represents this {@code NormalFace}. This image will be used in GUI.
     */
    public NormalFace(int id, int points, Content[] corners, String imagePath) {
        super(id, corners, imagePath);
        this.points=points;

    }

    /**
     * Returns the points given by this face.
     * @return the points given by this face
     */
    public int getPoints() {
        return points;
    }
}
