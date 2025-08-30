package jraffic;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class Traffic {
    public List<Car> cars;
    public Pane intersectionPane;

    public Traffic(Pane intersectionPane) {
        cars = new ArrayList<>();
        this.intersectionPane = intersectionPane;
    }

    public void addCar(Car c) {
        cars.add(c);
    }

    public void createCar(KeyCode code) {

        switch (code) {
            case UP:
                Car a = new Car(Direction.Up);
                intersectionPane.getChildren().add(a.getShape());
                addCar(a);
                break;
            case DOWN:
                Car b = new Car(Direction.Down);
                intersectionPane.getChildren().add(b.getShape());
                addCar(b);
                break;
            case LEFT:
                Car c = new Car(Direction.Left);
                intersectionPane.getChildren().add(c.getShape());
                addCar(c);
                break;
            case RIGHT:
                Car d = new Car(Direction.Right);
                intersectionPane.getChildren().add(d.getShape());
                addCar(d);
                break;
            default:
                break;
        }
    }

}