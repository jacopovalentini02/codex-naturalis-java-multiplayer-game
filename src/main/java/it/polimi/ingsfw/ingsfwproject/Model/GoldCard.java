package it.polimi.ingsfw.ingsfwproject.Model;

/**
 * The {@code GoldCard} class represents a gold card of the game.
 * It is a subclass of {@code NormalCard} and it adds a {@code GoldFront} attribute.
 */
public class GoldCard extends NormalCard{
    private GoldFront front;

    /**
     * Constructs a  {@code GoldCard} with the specified values.
     * @param backface the {@code NormalBack} back face of the card
     * @param front the {@code GoldFront} front of the card
     * @param id the card's ID
     */
    public GoldCard(NormalBack backface, GoldFront front, int id) {
        super(id, backface);
        this.front=front;
    }

    /**
     * Returns the front face of the card.
     * @return the front face of the card
     */
    public GoldFront getFront() {
        return front;
    }

    /**
     * Returns the back face of the card.
     * @return the back face of the card
     */
    @Override
    public Face getBack() {
        return this.getBackface();
    }

}

