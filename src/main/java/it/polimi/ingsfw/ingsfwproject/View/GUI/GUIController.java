package it.polimi.ingsfw.ingsfwproject.View.GUI;

import javafx.stage.Stage;

/**
 * Abstract base class for GUI controllers.
 *
 * <p>Defines an abstract method {@code start} that must be implemented by subclasses
 * to initialize and start the graphical user interface for the application.
 */
public abstract class GUIController {
    /**
     * Abstract method to initialize and start the GUI.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during initialization
     */
    public abstract void start(Stage stage) throws Exception;
}
