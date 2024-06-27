package it.polimi.ingsfw.ingsfwproject.Model;

import java.io.Serial;
import java.io.Serializable;

/**
 * The abstract {@code Card} class represents a card of the game.
 * Each card has an integer ID.
 * It implements {@link java.io.Serializable} to allow the coordinate objects to be serialized.
 */

abstract public class Card implements Serializable {
    private int idCard;
    @Serial
    private static final long serialVersionUID=19088521543661542L;

    /**
     * Constructs a new {@code Card} with the specified value.
     * @param idCard the ID of the card.
     */
    public Card(int idCard) {
        this.idCard = idCard;
    }

    /**
     * Returns this card's ID.
     * @return an integer with this card's ID.
     */
    public int getIdCard() {
        return idCard;
    }

    /**
     * This static method returns the {@code Content} representing this card's type.
     * Returns null if the card has no type (i.e. {@code StarterCard} or {@code ObjectiveCard}).
     * @param id the id of the card whose type you want to have
     * @return
     */
    public static Content getType(int id){
        if(id<=10 || (id>=41 && id<=50))
            return Content.FUNGI_KINGDOM;
        else if(id <= 20 || (id >= 51 && id <= 60))
            return Content.PLANT_KINGDOM;
        else if(id<=30 || (id>=52 && id<=70))
            return Content.ANIMAL_KINGDOM;
        else if(id<=40 || (id>=71 && id<=80))
            return Content.INSECT_KINGDOM;
        else
            return Content.EMPTY;
    }
}
