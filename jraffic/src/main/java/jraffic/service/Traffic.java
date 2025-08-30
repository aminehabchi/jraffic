package jraffic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import jraffic.helpers.Direction;
import jraffic.helpers.Towards;

public class Traffic {

    public List<Car> cars;
    private Pane roadPane;
    private Direction direction;
    private Random random;

    public Traffic(Pane roadPane) {
        this.roadPane = roadPane;
        this.cars = new ArrayList<>();

    }

    public void createCar(KeyCode code) {
        Car car = null;
        switch (code) {
            case UP:
                car = new Car(Direction.Up, getTowards());
                break;
            case DOWN:
                car = new Car(Direction.Down, getTowards());
                break;
            case LEFT:
                car = new Car(Direction.Left, getTowards());
                break;
            case RIGHT:
                car = new Car(direction.Right, getTowards());
                break;
            default:
                break;
        }

        if (car != null) {
            cars.add(car);
            roadPane.getChildren().add(car.getShape());
        }
    }

    private Towards getTowards() {
        if (random == null) {
            random = new Random();
        }
        int method = random.nextInt(3);

        switch (method) {
            case 0:
                return Towards.Forward;
            case 1:
                return Towards.Left;
            case 2:
                return Towards.Right;
            default:
                return Towards.Forward;
        }
    }

    public List<Car> getCars() {
        return cars;
    }

}