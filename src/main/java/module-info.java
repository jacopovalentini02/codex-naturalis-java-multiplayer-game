module it.polimi.ingsfw.ingsfwproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.ingsfw.ingsfwproject to javafx.fxml;
    exports it.polimi.ingsfw.ingsfwproject;
}