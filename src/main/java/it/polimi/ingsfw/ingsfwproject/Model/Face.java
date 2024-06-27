package it.polimi.ingsfw.ingsfwproject.Model;


import java.io.Serial;
import java.io.Serializable;

/**
 * The abstract {@code Face} class represents a face of a card.
 * Each face has the ID of the {@code Card} it belongs to, the contents of its corners and the information about them
 * that states if a corner is covered by another face or not, and the image of the {@code Face}.
 * It implements {@link java.io.Serializable} to allow the coordinate objects to be serialized.
 *
 */
abstract public class Face implements Serializable {
    @Serial
    private static final long serialVersionUID = -4109737873096935172L;
    /*cornerList[0] = top left
        cornerList[1] = top right
        cornerList[2] = bottom left
        cornerList[3] = bottom right
         */
    private int idCard;
    private Content[] cornerList;
    private boolean[] coveredCorner;

    private String imagePath;

    /**
     * Constructs a new {@code Face} with the specified values.
     * @param id the card's id it belongs to
     * @param corners the array that contains the {@code Content} of each corner. The contents' order must be:
     *                <ul>
     *                <li>corners[0] for the top left {@code Content}</li>
     *                <li>corners[1] for the top right {@code Content}</li>
     *                <li>corners[2] for the bottom left {@code Content}</li>
     *                <li>corners[3] for the bottom right {@code Content}</li>
     *                </ul>
     * @param imagePath the path of the image that represents this {@code Face}. This image will be used in GUI.
     */
    public Face(int id, Content[] corners, String imagePath){
        cornerList = new Content[4];
        coveredCorner = new boolean[4];
        idCard=id;
        this.imagePath=imagePath;

        System.arraycopy(corners, 0, cornerList, 0, 4);
    }

    /**
     * Returns a copy of the array that contains the {@code Content} of each corner. The contents' order is:
     *                <ul>
     *                <li>getCornerList()[0] for the top left {@code Content}</li>
     *                <li>getCornerList()[1] for the top right {@code Content}</li>
     *                <li>getCornerList()[2] for the bottom left {@code Content}</li>
     *                <li>getCornerList()[3] for the bottom right {@code Content}</li>
     *                </ul>
     * @return an array that contains the {@code Content} of each corner
     */
    public Content[] getCornerList() {
        return cornerList.clone();
    }

    /**
     * Returns the ID of the {@code Card} which this face belongs to
     * @return the ID of the {@code Card} which this face belongs to
     */
    public int getIdCard() {
        return idCard;
    }

    /**
     * Returns the {@code Content} in the top left corner.
     * @return the {@code Content} in the top left corner
     */
    public Content getTL(){
        return this.cornerList[0];
    }

    /**
     * Returns the {@code Content} in the top right corner.
     * @return the {@code Content} in the top right corner
     */
    public Content getTR(){
        return this.cornerList[1];
    }

    /**
     * Returns the {@code Content} in the bottom left corner.
     * @return the {@code Content} in the bottom left corner
     */
    public Content getBL(){
        return this.cornerList[2];
    }

    /**
     * Returns the {@code Content} in the bottom right corner.
     * @return the {@code Content} in the bottom right corner
     */
    public Content getBR(){
        return this.cornerList[3];
    }

    /**
     * Sets the top left corner as covered
     */
    public void setCoveredTL(){
        this.coveredCorner[0] = true;
    }

    /**
     * Sets the top right corner as covered
     */
    public void setCoveredTR(){
        this.coveredCorner[1] = true;
    }

    /**
     * Sets the bottom left corner as covered
     */
    public void setCoveredBL(){
        this.coveredCorner[2] = true;
    }

    /**
     * Sets the bottom right corner as covered
     */
    public void setCoveredBR(){
        this.coveredCorner[3] = true;
    }

    /**
     * Checks if the top left corner is covered by another face.
     * @return {@code true} if the top left corner is covered, {@code false} if it is not
     */
    public boolean isCoveredTL(){
        return this.coveredCorner[0];
    }

    /**
     * Checks if the top right corner is covered by another face.
     * @return {@code true} if the top right corner is covered, {@code false} if it is not
     */
    public boolean isCoveredTR(){
        return this.coveredCorner[1];
    }

    /**
     * Checks if the bottom left corner is covered by another face.
     * @return {@code true} if the bottom left corner is covered, {@code false} if it is not
     */
    public boolean isCoveredBL(){
        return this.coveredCorner[2];
    }

    /**
     * Checks if the bottom right corner is covered by another face.
     * @return {@code true} if the bottom right corner is covered, {@code false} if it is not
     */

    public boolean isCoveredBR(){
        return this.coveredCorner[3];
    }

    /**
     *
     * @return the image path of this {@code Face}
     */
    public String getImagePath() {
        return imagePath;
    }
}
