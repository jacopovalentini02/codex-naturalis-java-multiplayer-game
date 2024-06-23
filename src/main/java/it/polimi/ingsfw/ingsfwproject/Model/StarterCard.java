package it.polimi.ingsfw.ingsfwproject.Model;

import java.util.Arrays;

/**
 * The {@code StarterCard} class represents a starter card of the game.
 * It extends {@code PlayableCard} and it adds a specific {@code NormalFace} back face and a {@code StarterFront } front face.
 */
public class StarterCard extends PlayableCard{
    private NormalFace back;
    private StarterFront front;

    /**
     * Constructs a new {@code StarterCard} instance with the specified values.
     * @param idCard the ID of the card
     * @param back the {@code NormalFace} back face of the card
     * @param front the {@code StarterFront} front face of the card
     */
    public StarterCard(int idCard, NormalFace back, StarterFront front) {
        super(idCard);
        this.back = back;
        this.front = front;
    }

    /**
     * Returns the back face of this card.
     * @return The {@code NormalFace} object representing the back face of this card.
     */
    @Override
    public NormalFace getBack() {
        return back;
    }

    /**
     * Returns the front face of this card.
     * @return The {@code StarterFront} object representing the front face of this card.
     */
    @Override
    public StarterFront getFront() {
        return front;
    }

}
