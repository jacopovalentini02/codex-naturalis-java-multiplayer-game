package it.polimi.ingsfw.ingsfwproject.View.CLI;

public enum AnsiColor {
    // text color with background -> busy corner
    FUNGI_TEXT("\u001B[43m \uD83C\uDF44 "),  // red on ocra background
    PLANT_TEXT("\u001B[43m \uD83C\uDF3F "),  // green on ocra background
    ANIMAL_TEXT("\u001B[43m \uD83D\uDC3A "), // blue on ocra background
    INSECT_TEXT("\u001B[43m \uD83E\uDD8B "), // magenta on ocra background
    QUILL_TEXT("\u001B[43m \uD83E\uDEB6 "),  // white on ocra background
    INKWELL_TEXT("\u001B[43m \uD83E\uDED9 "),// white on ocra background
    MANUSCRIPT_TEXT("\u001B[43m \uD83D\uDCDC "), // white on ocra background

    // background colors
    PLANT_BACKGROUND("\u001B[42m    "),  // green background
    FUNGI_BACKGROUND("\u001B[41m    "),    // red background
    ANIMAL_BACKGROUND("\u001B[44m    "),   // blue background
    INSECT_BACKGROUND("\u001B[45m    "), // magenta background

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
}