module it.polimi.ingsfw.ingsfwproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.rmi;
    requires java.desktop;


    exports it.polimi.ingsfw.ingsfwproject.Controller;
    exports it.polimi.ingsfw.ingsfwproject.Model;
    opens it.polimi.ingsfw.ingsfwproject to javafx.fxml;
    exports it.polimi.ingsfw.ingsfwproject.Exceptions;
    exports it.polimi.ingsfw.ingsfwproject.Network.Server;
    exports it.polimi.ingsfw.ingsfwproject.View;

    opens it.polimi.ingsfw.ingsfwproject.View.GUI to javafx.fxml;
    exports it.polimi.ingsfw.ingsfwproject.View.GUI to javafx.graphics;
    exports it.polimi.ingsfw.ingsfwproject.View2;

    exports it.polimi.ingsfw.ingsfwproject.Network.Messages;
    exports it.polimi.ingsfw.ingsfwproject.Network.Client;

    opens it.polimi.ingsfw.ingsfwproject.View2 to javafx.fxml;

}