package jraffic;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import jraffic.service.Traffic;

public class RoadController {

    Traffic traffic;
    @FXML
    public Circle upRed;
    @FXML
    public Circle upGreen;

    @FXML
    public Circle downRed;
    @FXML
    public Circle downGreen;

    @FXML
    public Circle leftRed;
    @FXML
    public Circle leftGreen;

    @FXML
    public Circle rightRed;
    @FXML
    public Circle rightGreen;

    @FXML
    public Pane intersectionPane;

    enum TrafficLightState {
        NORTH_SOUTH_GREEN,
        EAST_WEST_GREEN
    }


    @FXML
    public void initialize() {
        System.out.println("RoadController initialized!");

        if (intersectionPane != null) {
            traffic = new Traffic(intersectionPane, this);
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
