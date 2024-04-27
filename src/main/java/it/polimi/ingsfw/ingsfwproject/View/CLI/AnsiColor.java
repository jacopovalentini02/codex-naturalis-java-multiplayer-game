package it.polimi.ingsfw.ingsfwproject.View.CLI;

public enum AnsiColor {
    // text color with background -> busy corner
    FUNGI_TEXT("F", "\u001B[31m\u001B[43m"),  // red on ocra background
    PLANT_TEXT("P", "\u001B[32m\u001B[43m"),  // green on ocra background
    ANIMAL_TEXT("A", "\u001B[34m\u001B[43m"), // blue on ocra background
    INSECT_TEXT("I", "\u001B[35m\u001B[43m"), // magenta on ocra background
    QUILL_TEXT("Q", "\u001B[47m\u001B[43m"),  // white on ocra background
    INKWELL_TEXT("I", "\u001B[47m\u001B[43m"),// white on ocra background
    MANUSCRIPT_TEXT("M", "\u001B[47m\u001B[43m"), // white on ocra background

    // background colors
    PLANT_BACKGROUND(" ", "\u001B[42m"),  // green background
    FUNGI_BACKGROUND(" ", "\u001B[41m"),    // red background
    ANIMAL_BACKGROUND(" ", "\u001B[44m"),   // blue background
    INSECT_BACKGROUND(" ", "\u001B[45m"), // magenta background

    EMPTY_BACKGROUND(" ","\u001B[43m"), // ocra background -> empty corner

    RED_DOT("•", "\u001B[31m"),
    BLUE_DOT("•", "\u001B[34m"),
    YELLOW_DOT("•", "\u001B[33m"),
    GREEN_DOT("•", "\u001B[32m"),
    BLACK_DOT("•", "\u001B[30m");

    private final String character;
    private final String colorAndBackground;

    AnsiColor(String character, String colorAndBackground) {
        this.character = character;
        this.colorAndBackground = colorAndBackground;
    }

    public String getFormattedCharacter() {
        // Restituisce il carattere con il colore del testo e dello sfondo specificati,
        // seguito dal codice di reset ANSI per assicurare che i colori successivi non siano influenzati
        return colorAndBackground + character + "\u001B[0m";
    }
}