package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a deck of cards used in a game. Handles operations such as shuffling,
 * adding, drawing, and creating various types of cards based on JSON configurations.
 */
public class Deck implements Serializable {

    @Serial
    private static final long serialVersionUID = -3945441290086881571L;
    private ArrayList<Card> cardList;

    /**
     * Constructs an empty deck of cards.
     */
    public Deck(){
        cardList = new ArrayList<>();
    }

    /**
     * Shuffles the cards in the deck in a random order.
     */
    public void shuffle(){
        Collections.shuffle(cardList);
    }

    /**
     * Adds a card to the deck.
     * @param card The card to add to the deck.
     */
    public void addCard(Card card){
        cardList.add(card);
    }

    /**
     * Retrieves the list of all cards in the deck.
     * @return An ArrayList containing all the cards in the deck.
     */
    public ArrayList<Card> getCardList() {
        return cardList;
    }

    /**
     * Creates a resource card from a JSON object specification and adds it to the deck.
     * @param cardObject The JSON object containing the card's specifications.
     * @param id The unique identifier for the card.
     */
    public void createResourceCard(JSONObject cardObject, int id){
        Content[] corners = new Content[4];

        String centerS = cardObject.getString("center");
        int points = cardObject.getInt("points");
        JSONArray cornersArray = cardObject.getJSONArray("corners");
        for (int j = 0; j < cornersArray.length(); j++) {
            corners[j] = Content.valueOf(cornersArray.getString(j));
        }
        Content center = Content.valueOf(centerS);

        // Crea un oggetto Card e aggiungilo alla lista
        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};
        String id1 = String.format("%03d", id);
        String imageFront="/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_front/" + id1 + ".png";
        String imageBack="/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_back/" + id1 + ".png";
        NormalBack backFace=new NormalBack(id, emptyCorners, center, imageBack);
        NormalFace front=new NormalFace(id, points, corners, imageFront);

        ResourceCard preRes = new ResourceCard(front, backFace, id);

        //preRes.createCard(id, center, points, corners);
        this.addCard(preRes);

    }

    /**
     * Creates a gold card based on JSON object specifications and adds it to the deck.
     * This type of card typically has special cost and ability requirements.
     * @param cardObject The JSON object containing the gold card's specifications.
     * @param id The unique identifier for the card.
     */
    public void createGoldCard(JSONObject cardObject, int id){

        Content objectNeed = null;
        Content[] corners = new Content[4];
        ArrayList<Content> costList = new ArrayList<>();

        String centerS = cardObject.getString("center");
        int points = cardObject.getInt("points");
        JSONArray costArray = cardObject.getJSONArray("cost");
        if (cardObject.has("objectNeeded") && !cardObject.isNull("objectNeeded"))
            objectNeed=Content.valueOf(cardObject.getString("objectNeeded"));
        boolean overlapped=cardObject.getBoolean("overlapped");
        JSONArray corner = cardObject.getJSONArray("corners");

        for (int j = 0; j < corner.length(); j++) {
            corners[j] = Content.valueOf(corner.getString(j));
        }

        for (int j = 0; j < costArray.length(); j++) {
            costList.add(Content.valueOf(costArray.getString(j)));
        }

        Content center = Content.valueOf(centerS);
        //Card creation
        Content[] emptyCorners = {Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY"),Content.valueOf("EMPTY")};

        String id1 = String.format("%03d", id);
        String imageFront="/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_front/" + id1 + ".png";
        String imageBack="/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_back/" + id1 + ".png";

        GoldFront front=new GoldFront(id, points, corners, costList, overlapped, objectNeed, imageFront);
        NormalBack back=new NormalBack(id, emptyCorners, center, imageBack);

        GoldCard preGold = new GoldCard(back, front, id);

        this.addCard(preGold);


    }

    /**
     * Creates a starter card from a JSON object and adds it to the deck.
     * Starter cards are typically used at the beginning of a game.
     * @param cardObject The JSON object containing the starter card's specifications.
     * @param id The unique identifier for the card.
     */
    public void createStarterCard(JSONObject cardObject, int id){

        ArrayList<Content> centerList = new ArrayList<>();
        Content[] cornerBa = new Content[4];
        Content[] cornerFr = new Content[4];

        JSONArray centerArray = cardObject.getJSONArray("center");
        for (int j = 0; j < centerArray.length(); j++) {
            centerList.add(Content.valueOf(centerArray.getString(j)));
        }

        JSONArray cornerFrArray = cardObject.getJSONArray("cornerFront");
        for (int j = 0; j < cornerFrArray.length(); j++) {
            cornerFr[j] = Content.valueOf(cornerFrArray.getString(j));
        }

        JSONArray cornerBaArray = cardObject.getJSONArray("cornerBack");
        for (int j = 0; j < cornerBaArray.length(); j++) {
            cornerBa[j] = Content.valueOf(cornerBaArray.getString(j));
        }
        String id1 = String.format("%03d", id);
        String imageFront="/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_front/" + id1 + ".png";
        String imageBack="/it/polimi/ingsfw/ingsfwproject/Images/CODEX_cards_gold_back/" + id1 + ".png";
        NormalFace back=new NormalFace(id, 0, cornerBa, imageBack);
        StarterFront front=new StarterFront(id, cornerFr, centerList, imageFront);

        StarterCard preStart = new StarterCard(id, back, front);
        this.addCard(preStart);

    }

    /**
     * Creates a structured objective card from a JSON object and adds it to the deck.
     * This type of card typically represents a game objective that involves building
     * a structure, incorporating specific game mechanics as specified in the JSON object.
     * The method parses the JSON object to extract the required attributes like points,
     * structure type, and associated resources.
     *
     * @param cardObject The JSON object containing the structured objective card's specifications.
     * @param id The unique identifier for the card.
     */
    public void createStructObjective(JSONObject cardObject, int id){

        int points = cardObject.getInt("points");
        String structureS = cardObject.getString("structure");
        Structure structure = Structure.valueOf(structureS);

        ArrayList<Content> resourceList = new ArrayList<>();
        JSONArray resArray = cardObject.getJSONArray("resource");
        for (int j = 0; j < resArray.length(); j++) {
            resourceList.add(Content.valueOf(resArray.getString(j)));
        }

        StructuredObjectiveCard structObj=new StructuredObjectiveCard(id, points,structure,resourceList);

        this.addCard(structObj);

    }

    /**
     * Creates a non-structured objective card from a JSON object and adds it to the deck.
     * Unlike structured objectives, these cards often focus on accumulating resources or
     * achieving certain conditions without the need for constructing physical structures.
     * This method parses various attributes from the JSON object such as points and resources
     * required to fulfill the objective.
     *
     * @param cardObject The JSON object containing the non-structured objective card's specifications.
     * @param id The unique identifier for the card.
     */
    public void createNotStructObjective(JSONObject cardObject, int id){

        int points = cardObject.getInt("points");

        ArrayList<Content> resourceList = new ArrayList<>();
        JSONArray resArray = cardObject.getJSONArray("resource");
        for (int j = 0; j < resArray.length(); j++) {
            resourceList.add(Content.valueOf(resArray.getString(j)));
        }

        NotStructuredObjectiveCard notStructObj=new NotStructuredObjectiveCard(id, points, resourceList);

        this.addCard(notStructObj);

    }

    /**
     * Draws the top card from the deck. If the deck is empty, it throws an exception.
     * @return The card drawn from the deck.
     * @throws DeckEmptyException if the deck is empty and no cards are available to be drawn.
     */
    public Card draw() throws DeckEmptyException {
        if (this.cardList!=null && !this.cardList.isEmpty()) {
            Card drawnCard= this.cardList.getFirst();
            this.cardList.removeFirst();
            return drawnCard;
        } else {
            // if the deck is empty throw exception
            throw new DeckEmptyException("This deck is empty, cannot draw a card");

        }
    }

    /**
     * Sets the current list of cards in the deck to a new list.
     * @param cardList The new list of cards to be used for the deck.
     */
    public void setCardList(ArrayList<Card> cardList) {
        this.cardList = cardList;
    }
}
