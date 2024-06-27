package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serial;

/**
 * The {@code NormalBack} class represents the back face of a {@code ResourceCard} or {@code GoldCard}.
 * This class is a subclass of {@code Face} and it adds a center {@code Content}.
 */
public class NormalBack extends Face{
    @Serial
    private static final long serialVersionUID = 1573831788109124200L;
    private final Content center;

    /**
     * Constructs a new {@code NormalBack} with the specified values.
     * @param id the card's id it belongs to
     * @param corners the array that contains the {@code Content} of each corner. The contents' order must be:
     *                <ul>
     *                <li>corners[0] for the top left {@code Content}</li>
     *                <li>corners[1] for the top right {@code Content}</li>
     *                <li>corners[2] for the bottom left {@code Content}</li>
     *                <li>corners[3] for the bottom right {@code Content}</li>
     *                </ul>
     * @param center the center {@code content} of the back face
     * @param imagePath the path of the image that represents this {@code NormalBack}. This image will be used in GUI.
     */
    public NormalBack(int id, Content[] corners, Content center, String imagePath) {
        super(id, corners, imagePath);
        this.center=center;
    }

    /**
     * Returns the center {@code Content} of this {@code NormalBack}.
     * @return the center {@code Content} of this {@code NormalBack}.
     */
    public Content getCenter() {
        return center;
    }
}
