package it.polimi.ingsfw.ingsfwproject.View.CLI;

import it.polimi.ingsfw.ingsfwproject.Model.Content;
import it.polimi.ingsfw.ingsfwproject.Model.NormalBack;

public enum AnsiColor {
    // text color with background -> busy corner
    FUNGI_TEXT("\u001B[43m \uD83C\uDF44 "),  // red on ocra background
    PLANT_TEXT("\u001B[43m \uD83C\uDF3F "),  // green on ocra background
    ANIMAL_TEXT("\u001B[43m \uD83D\uDC3A "), // blue on ocra background
    INSECT_TEXT("\u001B[43m \uD83E\uDD8B "), // magenta on ocra background
    QUILL_TEXT("\u001B[43m \uD83E\uDEB6 "),  // white on ocra background
    INKWELL_TEXT("\u001B[43m \uD83E\uDED9 "),// white on ocra background
    MANUSCRIPT_TEXT("\u001B[43m \uD83D\uDCDC "), // white on ocra background

    POINT_ONE("\u001B[33;103m 1\uFE0F⃣ "),
    POINT_TWO("\u001B[33;103m 2\uFE0F⃣ "),
    POINT_THREE("\u001B[33;103m 3\uFE0F⃣ "),
    POINT_FIVE("\u001B[33;103m 5\uFE0F⃣ "),



    // background colors
    PLANT_BACKGROUND("\u001B[42m    "),  // green background
    FUNGI_BACKGROUND("\u001B[41m    "),    // red background
    ANIMAL_BACKGROUND("\u001B[44m    "),   // blue background
    INSECT_BACKGROUND("\u001B[45m    "), // magenta background

    STARTER_BACKGROUND("\u001B[47m    "),

    B_GOLD_PLANT_BACKGROUND("\u001B[43m \u001B[42m   "),  // green background
    B_GOLD_FUNGI_BACKGROUND("\u001B[43m \u001B[41m   "),    // red background
    B_GOLD_ANIMAL_BACKGROUND("\u001B[43m \u001B[44m   "),   // blue background
    B_GOLD_INSECT_BACKGROUND("\u001B[43m \u001B[45m   "), // magenta background
    E_GOLD_PLANT_BACKGROUND("\u001B[42m   \u001B[43m "),  // green background
    E_GOLD_FUNGI_BACKGROUND("\u001B[41m   \u001B[43m "),    // red background
    E_GOLD_ANIMAL_BACKGROUND("\u001B[44m   \u001B[43m "),   // blue background
    E_GOLD_INSECT_BACKGROUND("\u001B[45m   \u001B[43m "), // magenta background


    EMPTY_TEXT("\u001B[43m    "), // ocra background -> empty corner

    RED_DOT("\uD83D\uDD34"),
    BLUE_DOT("\uD83D\uDD35"),
    YELLOW_DOT("\uD83D\uDFE1"),
    GREEN_DOT("\uD83D\uDFE2");


    private final String colorAndBackground;


    AnsiColor(String colorAndBackground) {
        this.colorAndBackground = colorAndBackground;
    }

    public String getFormattedCharacter() {
        // Restituisce il carattere con il colore del testo e dello sfondo specificati,
        // seguito dal codice di reset ANSI per assicurare che i colori successivi non siano influenzati
        return colorAndBackground + "\u001B[0m";
    }

    public String getFormattedCharacter(Content content, AnsiColor cardType){
        String text = "";
        switch (content) {
            case FUNGI_KINGDOM -> text = AnsiColor.FUNGI_TEXT.getFormattedCharacter();
            case PLANT_KINGDOM -> text = AnsiColor.PLANT_TEXT.getFormattedCharacter();
            case INSECT_KINGDOM -> text = AnsiColor.INSECT_TEXT.getFormattedCharacter();
            case ANIMAL_KINGDOM -> text = AnsiColor.ANIMAL_TEXT.getFormattedCharacter();
            case EMPTY -> text = AnsiColor.EMPTY_TEXT.getFormattedCharacter();
            case HIDDEN -> text = cardType.getFormattedCharacter();
            case QUILL -> text = AnsiColor.QUILL_TEXT.getFormattedCharacter();
            case MANUSCRIPT -> text = AnsiColor.MANUSCRIPT_TEXT.getFormattedCharacter();
            case INKWELL -> text = AnsiColor.INKWELL_TEXT.getFormattedCharacter();
        }
        return text;
    }
}