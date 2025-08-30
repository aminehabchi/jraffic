package jraffic;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import jraffic.service.Traffic;

public class RoadController {
    Traffic traffic;

    @FXML
    private Pane intersectionPane;

    @FXML
    public void initialize() {
        System.out.println("RoadController initialized!");

        if (intersectionPane != null) {
            traffic = new Traffic(intersectionPane);
            intersectionPane.setFocusTraversable(true);
            intersectionPane.requestFocus();
            intersectionPane.setOnKeyPressed(this::handleKeyPress);

            AnimationTimer gameLoop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    traffic.moveCars();
                }
            };
            gameLoop.start();
        } else {
            System.err.println("intersectionPane or redR is null - check fx:id in FXML");
        }
    }

    private void handleKeyPress(KeyEvent event) {
        traffic.createCar(event.getCode());
    }
}
