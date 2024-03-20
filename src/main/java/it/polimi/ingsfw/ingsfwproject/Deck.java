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

    public void createResourceCard(JSONObject cardObject, List<ResourceCard> cardList, int id){
        String centerS = cardObject.getString("center");
        int points = cardObject.getInt("points");
        JSONArray cornersArray = cardObject.getJSONArray("corners");
        String[] cornerS = new String[cornersArray.length()];
        for (int j = 0; j < cornersArray.length(); j++) {
            cornerS[j] = cornersArray.getString(j);
        }

        // Crea un oggetto Card e aggiungilo alla lista
        ResourceCard preRes = new ResourceCard();
        Content center = Content.valueOf(centerS);
        Content[] corners = new Content[4];
        for (int k = 0; k < 4; k++) {
            corners[k] = Content.valueOf(cornerS[k]);
        }
        preRes.createCard(id, center, points, corners);
        this.addCard(preRes); //THIS O CARDSLIST????????

    }

    public void createGoldCard(JSONObject cardObject, List<GoldCard> cardList, int id, JSONObject jsonObject){
        String objectNeed = null;
        Content object = null;

        String centerS = cardObject.getString("center");
        int points = cardObject.getInt("points");
        JSONArray costArray = cardObject.getJSONArray("cost");
        if (jsonObject.has("objectNeeded") && !jsonObject.isNull("objectNeeded"))
            objectNeed=cardObject.getString("objectNeeded");
        boolean overlapped=cardObject.getBoolean("overlapped");
        JSONArray corner = cardObject.getJSONArray("corners");

        String[] cornerS = new String[corner.length()];
        for (int j = 0; j < corner.length(); j++) {
            cornerS[j] = corner.getString(j);
        }

        String[] costS = new String[costArray.length()];
        for (int j = 0; j < costArray.length(); j++) {
            costS[j] = costArray.getString(j);
        }

        //Card creation
        GoldCard preGold = new GoldCard();
        Content center = Content.valueOf(centerS);
        if(objectNeed != null)
            object=Content.valueOf(objectNeed);
        Content[] corners = new Content[4];
        for (int k = 0; k < 4; k++) {
            corners[k] = Content.valueOf(cornerS[k]);
        }

        ArrayList<Content> costList = new ArrayList<>();
        for (String s : costS) {
            costList.add(Content.valueOf(s));
        }

        preGold.createCard(id, center, points,corners,costList,object,overlapped);
        this.addCard(preGold); //THIS O CARDSLIST????????

    }
}
