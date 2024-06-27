package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serial;

/**
 * the {@code PlayableCard} is an abstract class representing all the cards that can be played on the playground,
 * i.e. {@code StarterCard}, {@code GoldCard}, and {@code ResourceCard}.
 * It is a subclass of {@code Card} abstract class.
 */
abstract public class PlayableCard extends Card {

    @Serial
    private static final long serialVersionUID = 8054374489721882225L;

    /**
     * Constructs a new {@code PlayableCard} instance with the specified card ID.
     *
     * @param idCard the ID of the card
     */
    public PlayableCard(int idCard) {
        super(idCard);
    }

    /**
     * Returns the front face of this card.
     *
     * @return The {@code Face} object representing the front face of this card.
     */
    public abstract Face getFront();

    /**
     * Returns the back face of this card.
     *
     * @return The {@code Face} object representing the back face of this card.
     */
    public abstract Face getBack();

}
