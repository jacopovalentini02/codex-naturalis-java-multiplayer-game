package it.polimi.ingsfw.ingsfwproject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Deck {


    private ArrayList<Card> cardList;

    /*public Card draw(){

    }*/

    public void shuffle(){

    }

    public void addCard(Card card){
        cardList.add(card);
    }

    public ArrayList<Card> getCardList() {
        return cardList;
    }

    public void createResourceCard(JSONObject cardObject, int id){
        this.cardList = new ArrayList<>();
        Content[] corners = new Content[4];

        String centerS = cardObject.getString("center");
        int points = cardObject.getInt("points");
        JSONArray cornersArray = cardObject.getJSONArray("corners");
        for (int j = 0; j < cornersArray.length(); j++) {
            corners[j] = Content.valueOf(cornersArray.getString(j));
        }

        // Crea un oggetto Card e aggiungilo alla lista
        ResourceCard preRes = new ResourceCard();
        Content center = Content.valueOf(centerS);

        preRes.createCard(id, center, points, corners);
        this.addCard(preRes);
        this.printCardsDeck();

    }

    public void createGoldCard(JSONObject cardObject, int id, JSONObject jsonObject){
        this.cardList = new ArrayList<>();

        String objectNeed = null;
        Content object = null;
        Content[] corners = new Content[4];
        ArrayList<Content> costList = new ArrayList<>();

        String centerS = cardObject.getString("center");
        int points = cardObject.getInt("points");
        JSONArray costArray = cardObject.getJSONArray("cost");
        if (jsonObject.has("objectNeeded") && !jsonObject.isNull("objectNeeded"))
            objectNeed=cardObject.getString("objectNeeded");
        boolean overlapped=cardObject.getBoolean("overlapped");
        JSONArray corner = cardObject.getJSONArray("corners");

        for (int j = 0; j < corner.length(); j++) {
            corners[j] = Content.valueOf(corner.getString(j));
        }

        for (int j = 0; j < costArray.length(); j++) {
            costList.add(Content.valueOf(costArray.getString(j)));
        }

        //Card creation
        GoldCard preGold = new GoldCard();
        Content center = Content.valueOf(centerS);
        if(objectNeed != null)
            object=Content.valueOf(objectNeed);

        preGold.createCard(id, center, points,corners,costList,object,overlapped);
        this.addCard(preGold);
        this.printCardsDeck();



    }

    public void createStarterCard(JSONObject cardObject, int id){
        this.cardList = new ArrayList<>();

        ArrayList<Content> centerList = new ArrayList<>();
        Content[] cornerBa = new Content[4];
        Content[] cornerFr = new Content[4];

        JSONArray centerArray = cardObject.getJSONArray("center");
        for (int j = 0; j < centerArray.length(); j++) {
            centerList.add(Content.valueOf(centerArray.getString(j)));
        }

        JSONArray cornerBaArray = cardObject.getJSONArray("cornerBack");
        for (int j = 0; j < cornerBaArray.length(); j++) {
            cornerBa[j] = Content.valueOf(cornerBaArray.getString(j));
        }

        JSONArray cornerFrArray = cardObject.getJSONArray("cornerFront");
        for (int j = 0; j < cornerFrArray.length(); j++) {
            cornerFr[j] = Content.valueOf(cornerFrArray.getString(j));
        }

        StarterCard preStart = new StarterCard();
        preStart.createCard(id, centerList, cornerBa,cornerFr);
        this.addCard(preStart);
        this.printCardsDeck();

    }

    public void createStructObjective(JSONObject cardObject, int id){
        this.cardList = new ArrayList<>();

        int points = cardObject.getInt("points");
        String structureS = cardObject.getString("structure");
        Structure structure = Structure.valueOf(structureS);

        ArrayList<Content> resourceList = new ArrayList<>();
        JSONArray resArray = cardObject.getJSONArray("resource");
        for (int j = 0; j < resArray.length(); j++) {
            resourceList.add(Content.valueOf(resArray.getString(j)));
        }

        StructuredObjectiveCard structObj=new StructuredObjectiveCard();
        structObj.setIdCard(id);
        structObj.setPoints(points);
        structObj.setStructureType(structure);
        structObj.setResourceRequested(resourceList);

        this.addCard(structObj);
        this.printCardsDeck();

    }

    public void createNotStructObjective(JSONObject cardObject, int id){

        int points = cardObject.getInt("points");

        ArrayList<Content> resourceList = new ArrayList<>();
        JSONArray resArray = cardObject.getJSONArray("resource");
        for (int j = 0; j < resArray.length(); j++) {
            resourceList.add(Content.valueOf(resArray.getString(j)));
        }

        NotStructuredObjectiveCard notStructObj=new NotStructuredObjectiveCard();
        notStructObj.setIdCard(id);
        notStructObj.setPoints(points);
        notStructObj.setObjectRequested(resourceList);

        this.addCard(notStructObj);
        this.printCardsDeck();

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
