package it.polimi.ingsfw.ingsfwproject.Model;

/**
 * The {@code NormalCard} class is an abstract class representing a playable card with a {@code NormalBack} back face.
 * It extends the {@code PlayableCard} class and includes additional properties and methods specific to cards with a {@code NormalBack} back face.
 */
abstract public class NormalCard extends PlayableCard{
    private NormalBack backface;

    /**
     * Constructs a new {@code NormalCard} instance with the specified parameters.
     *
     * @param id the unique identifier for this card
     * @param backface the {@code NormalBack} object representing the back face of this card
     */
    public NormalCard(int id, NormalBack backface) {
        super(id);
        this.backface = backface;
    }

    /**
     * Returns the back face of this card.
     *
     * @return The {@code NormalBack} representing the back face of this card
     */
    public NormalBack getBackface() {
        return backface;
    }
}
