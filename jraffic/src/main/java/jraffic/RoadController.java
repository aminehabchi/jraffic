package jraffic;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import jraffic.service.Traffic;

public class RoadController {

    Traffic traffic;
    @FXML
    private Circle northRed;
    @FXML
    private Circle northGreen;

    @FXML
    private Circle southRed;
    @FXML
    private Circle southGreen;

    @FXML
    private Circle eastRed;
    @FXML
    private Circle eastGreen;

    @FXML
    private Circle westRed;
    @FXML
    private Circle westGreen;
    @FXML
    private Pane intersectionPane;

    enum TrafficLightState {
        NORTH_SOUTH_GREEN,
        EAST_WEST_GREEN
    }
    private TrafficLightState currentState;

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

    public void updateTrafficLightsBasedOnCars() {
        boolean hasCarInside = !traffic.carsInside.isEmpty();

        // if (hasCarInside) {
        //     System.out.println("Cars in intersection (" + traffic.carsInside.size() + ") - lights locked");
        //     return;
        // }
       
        // int upDownCars = traffic.carsT.size() + traffic.carsD.size();
        // int leftRightCars = traffic.carsL.size() + traffic.carsR.size();
        // System.out.println(""+upDownCars+"**"+leftRightCars+"**"+hasCarInside);
        // if (upDownCars > leftRightCars) {

        //     setTrafficLightState(TrafficLightState.NORTH_SOUTH_GREEN);
        // } else if (leftRightCars > upDownCars) {

        //     setTrafficLightState(TrafficLightState.EAST_WEST_GREEN);
        // }

    }

    private void setTrafficLightState(TrafficLightState newState) {
        currentState = newState;

        switch (currentState) {
            case NORTH_SOUTH_GREEN:

                northRed.setFill(Color.web("#660000"));

                northGreen.setFill(Color.LIME);
                southRed.setFill(Color.web("#660000"));
                southGreen.setFill(Color.LIME);

                eastRed.setFill(Color.RED);
                eastGreen.setFill(Color.web("#006600"));
                westRed.setFill(Color.RED);
                westGreen.setFill(Color.web("#006600"));
                break;

            case EAST_WEST_GREEN:

                northRed.setFill(Color.RED);
                northGreen.setFill(Color.web("#006600"));
                southRed.setFill(Color.RED);
                southGreen.setFill(Color.web("#006600"));

                eastRed.setFill(Color.web("#660000"));
                eastGreen.setFill(Color.LIME);
                westRed.setFill(Color.web("#660000"));
                westGreen.setFill(Color.LIME);
                break;
        }

    }

    private void handleKeyPress(KeyEvent event) {
        traffic.createCar(event.getCode());
    }
}
