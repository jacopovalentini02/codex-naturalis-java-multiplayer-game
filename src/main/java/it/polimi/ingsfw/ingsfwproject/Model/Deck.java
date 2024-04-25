package it.polimi.ingsfw.ingsfwproject.Model;

import it.polimi.ingsfw.ingsfwproject.Exceptions.DeckEmptyException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

public class Deck implements Serializable {


    private ArrayList<Card> cardList;


    public Deck(){
        cardList = new ArrayList<>();
    }

    public void shuffle(){
        Collections.shuffle(cardList);
    }

    public void addCard(Card card){
        cardList.add(card);
    }

    public ArrayList<Card> getCardList() {
        return cardList;
    }

    public void setCardList(ArrayList<Card> cardList) {
        this.cardList = cardList;
    }

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
        NormalBack backFace=new NormalBack(id, emptyCorners, center);
        NormalFace front=new NormalFace(id, points, corners);

        ResourceCard preRes = new ResourceCard(front, backFace, id);

        //preRes.createCard(id, center, points, corners);
        this.addCard(preRes);

    }

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
        GoldFront front=new GoldFront(id, points, corners, costList, overlapped, objectNeed);
        NormalBack back=new NormalBack(id, emptyCorners, center);

        GoldCard preGold = new GoldCard(back, front, id);

        this.addCard(preGold);


    }

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

        NormalFace back=new NormalFace(id, 0, cornerBa);
        StarterFront front=new StarterFront(id, cornerFr, centerList);

        StarterCard preStart = new StarterCard(id, back, front);
        this.addCard(preStart);

    }

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
    public void printCardsDeck() {
        ArrayList<Card> cards = this.getCardList();
        for (Card card : cards) {
            if (card instanceof ResourceCard) {
                ResourceCard resourceCard = (ResourceCard) card;
                resourceCard.printAll();
            }else if (card instanceof GoldCard) {
                GoldCard goldcard = (GoldCard) card;
                goldcard.printAll();
            } else if (card instanceof StarterCard) {
                StarterCard starterCard = (StarterCard) card;
                starterCard.printAll();
            } else if (card instanceof StructuredObjectiveCard) {
                StructuredObjectiveCard structObj = (StructuredObjectiveCard) card;
                System.out.println(structObj.toString());
            }else{
                NotStructuredObjectiveCard notstructObj = (NotStructuredObjectiveCard) card;
                System.out.println(notstructObj.toString());
            }
        }
    }



}
