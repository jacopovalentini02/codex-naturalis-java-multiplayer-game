package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serial;

/**
 * The {@code ResourceCard} class represents a resource card of the game.
 * It extends {@code NormalCard} and it adds a specific {@code NormalFace} front face.
 */
public class ResourceCard extends NormalCard{
    @Serial
    private static final long serialVersionUID = 6051959552075268556L;
    private NormalFace front;

    /**
     * Constructs a new {@code ResourceCard} instance with the specified values.
     * @param front the {@code NormalFace} front face of this card
     * @param backface the {@code NormalBack} back face of this card
     * @param id the ID of this card
     */
    public ResourceCard(NormalFace front, NormalBack backface, int id) {
        super(id, backface);
        this.front = front;
    }

    /**
     * Returns the front face of this card.
     * @return The {@code NormalFace} object representing the front face of this card.
     */
    public NormalFace getFront() {
        return front;
    }

    /**
     * Returns the back face of this card.
     * @return The {@code Face} object representing the back face of this card.
     */
    @Override
    public Face getBack() {
        return this.getBackface();
    }

}
