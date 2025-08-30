package jraffic;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class RoadController {

    @FXML
    private Pane intersectionPane;

    @FXML
    private Circle redL; // Injected from FXML, do NOT create new Circle

    @FXML
    public void initialize() {
        System.out.println("RoadController initialized!");

        if (intersectionPane != null && redL != null) {

            intersectionPane.setFocusTraversable(true);
            intersectionPane.requestFocus();
            intersectionPane.setOnKeyPressed(this::handleKeyPress);
        } else {
            System.err.println("intersectionPane or redR is null - check fx:id in FXML");
        }
    }

    private void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();
        
    }
}
