package it.polimi.ingsfw.ingsfwproject.View.CLI;

import it.polimi.ingsfw.ingsfwproject.Model.*;

import java.util.Iterator;
import java.util.Map;

public class Cli {

    public void printGrid(PlayerGround playerGround) {

    }
    public void printFacePlayed(Face face){
        // TODO: Questo metodo possiamo lasciarlo qui oppure creare una classe printer per la CLI e metterla li
        // TODO: Inoltre, forse non è necessare passare sia Card che Face...da rivedere!
        // TODO: Questo metodo si può usare per tutte le carte della grid tranne che per la back face della starter
        // TODO: -> Consigliato metodo a parte
        int i;
        AnsiColor cardType = getCardType(face);
        //upper-left corner
        printCorner(face.getTL(), 0, cardType);
        //space
        for (i=0; i<9; i++){
            System.out.print(cardType.getFormattedCharacter());
        }
        //upper-right corner
         printCorner(face.getTR(), 1, cardType);
        for(i=0; i<5; i++){
            System.out.print(cardType.getFormattedCharacter());
        }
        //center, if exists
        if(face instanceof NormalBack){
            switch(((NormalBack) face).getCenter()){
                case FUNGI_KINGDOM -> System.out.println(AnsiColor.FUNGI_TEXT.getFormattedCharacter());
                case PLANT_KINGDOM -> System.out.println(AnsiColor.PLANT_TEXT.getFormattedCharacter());
                case INSECT_KINGDOM -> System.out.println(AnsiColor.INSECT_TEXT.getFormattedCharacter());
                case ANIMAL_KINGDOM -> System.out.println(AnsiColor.ANIMAL_TEXT.getFormattedCharacter());
            }
        }
        else{
            System.out.print(cardType.getFormattedCharacter());
        }
        for(i=0; i<4; i++){
            System.out.print(cardType.getFormattedCharacter());
        }
        System.out.println(cardType.getFormattedCharacter());
        //lower-left corner
        printCorner(face.getBL(), 0, cardType);
        //space
        for (i=0; i<9; i++){
            System.out.print(cardType.getFormattedCharacter());
        }
        //lower-right corner
        printCorner(face.getBR(), 1, cardType);
    }

    public void printCorner(Content content, int rOrl, AnsiColor cardType) {
        if (rOrl == 0) {
            switch (content) {
                case FUNGI_KINGDOM -> System.out.print(AnsiColor.FUNGI_TEXT.getFormattedCharacter());
                case PLANT_KINGDOM -> System.out.print(AnsiColor.PLANT_TEXT.getFormattedCharacter());
                case INSECT_KINGDOM -> System.out.print(AnsiColor.INSECT_TEXT.getFormattedCharacter());
                case ANIMAL_KINGDOM -> System.out.print(AnsiColor.ANIMAL_TEXT.getFormattedCharacter());
                case EMPTY -> System.out.print(AnsiColor.EMPTY_BACKGROUND.getFormattedCharacter());
                case HIDDEN -> System.out.print(cardType.getFormattedCharacter());
                case QUILL -> System.out.print(AnsiColor.QUILL_TEXT.getFormattedCharacter());
                case MANUSCRIPT -> System.out.print(AnsiColor.MANUSCRIPT_TEXT.getFormattedCharacter());
                case INKWELL -> System.out.print(AnsiColor.INKWELL_TEXT.getFormattedCharacter());
            }
        } else {
            switch (content) {
                case FUNGI_KINGDOM -> System.out.println(AnsiColor.FUNGI_TEXT.getFormattedCharacter());
                case PLANT_KINGDOM -> System.out.println(AnsiColor.PLANT_TEXT.getFormattedCharacter());
                case INSECT_KINGDOM -> System.out.println(AnsiColor.INSECT_TEXT.getFormattedCharacter());
                case ANIMAL_KINGDOM -> System.out.println(AnsiColor.ANIMAL_TEXT.getFormattedCharacter());
                case EMPTY -> System.out.println(AnsiColor.EMPTY_BACKGROUND.getFormattedCharacter());
                case HIDDEN -> System.out.println(cardType.getFormattedCharacter());
                case QUILL -> System.out.println(AnsiColor.QUILL_TEXT.getFormattedCharacter());
                case MANUSCRIPT -> System.out.println(AnsiColor.MANUSCRIPT_TEXT.getFormattedCharacter());
                case INKWELL -> System.out.println(AnsiColor.INKWELL_TEXT.getFormattedCharacter());
            }
        }
    }

    public AnsiColor getCardType(Face face){
        Content background = Card.getType(face.getIdCard());
        return switch(background){
            case FUNGI_KINGDOM -> AnsiColor.FUNGI_BACKGROUND;
            case PLANT_KINGDOM ->  AnsiColor.PLANT_BACKGROUND;
            case INSECT_KINGDOM -> AnsiColor.INSECT_BACKGROUND;
            case ANIMAL_KINGDOM -> AnsiColor.ANIMAL_BACKGROUND;
            case QUILL -> null;
            case INKWELL -> null;
            case MANUSCRIPT -> null;
            case EMPTY -> null;
            case HIDDEN -> null;
        };
    }

    public void printPlayerHand(Player player){
        int i =1;
        for (PlayableCard c :player.getHandCard()){
            System.out.println("Front of card "+ i + ":");
            printFacePlayed(c.getFront());
            System.out.println("Back of card "+ i + ":");
            printFacePlayed(c.getBack());
            i++;
        }
    }
}
