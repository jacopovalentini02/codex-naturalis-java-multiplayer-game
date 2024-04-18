package it.polimi.ingsfw.ingsfwproject.Model;

abstract public class Card {
    private int idCard;

    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

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
            return null;
    }

    public Card(int idCard) {
        this.idCard = idCard;
    }
}
