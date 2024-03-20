module it.polimi.ingsfw.ingsfwproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens it.polimi.ingsfw.ingsfwproject to javafx.fxml;
    exports it.polimi.ingsfw.ingsfwproject;


}