module it.polimi.ingsfw.ingsfwproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.rmi;
    requires java.desktop;


    exports it.polimi.ingsfw.ingsfwproject.Controller;
    exports it.polimi.ingsfw.ingsfwproject.Model;
    opens it.polimi.ingsfw.ingsfwproject.Model to javafx.fxml;
    exports it.polimi.ingsfw.ingsfwproject.Exceptions;
    exports it.polimi.ingsfw.ingsfwproject.Network.Server;


    exports it.polimi.ingsfw.ingsfwproject.Network.Messages;


}