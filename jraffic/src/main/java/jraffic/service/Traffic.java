package jraffic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import jraffic.helpers.Constants;
import jraffic.helpers.Direction;
import jraffic.helpers.Towards;

public class Traffic {

    public List<Car> cars;
    private Pane roadPane;
    private Direction direction;
    private Random random;
    private int[] carNbr = { 0, 0, 0, 0 };

    public Traffic(Pane roadPane) {
        this.roadPane = roadPane;
        this.cars = new ArrayList<>();
    }

    public void moveCars() {
        int a = -100;
        int b = -100;
        int c = -100;
        int d = -100;

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);

            switch (car.getDirection()) {
                case Up:
                    if ((a == -100 || (Math.abs(a - car.getY()) >= Constants.SAFEDISTANCE)) && !isMustStop(car)) {
                        car.move();
                    }
                    a = car.getY();
                    break;
                case Down:
                    if ((b == -100 || (Math.abs(b - car.getY()) >= Constants.SAFEDISTANCE)) && !isMustStop(car)) {
                        car.move();
                    }
                    b = car.getY();
                    break;
                case Left:
                    if ((c == -100 || (Math.abs(c - car.getX()) >= Constants.SAFEDISTANCE)) && !isMustStop(car)) {
                        car.move();
                    }
                    c = car.getX();
                    break;
                case Right:
                    if ((d == -100 || (Math.abs(d - car.getX()) >= Constants.SAFEDISTANCE)) && !isMustStop(car)) {
                        car.move();
                    }
                    d = car.getX();
                    break;
                default:
            }
        }
    }

    private boolean isMustStop(Car car) {
        switch (car.getDirection()) {
            case Up:
                if (Constants.ROAD_HEIGHT - Constants.CARSIZE - 2 < car.getY()
                        && Constants.ROAD_HEIGHT - Constants.CARSIZE >= car.getY()) {
                    return true;
                }
                break;
            case Down:
                if (Constants.WINDOW_HEIGHT - Constants.ROAD_HEIGHT + 2 > car.getY()
                        && Constants.WINDOW_HEIGHT - Constants.ROAD_HEIGHT <= car.getY()) {
                    return true;
                }
                break;
            case Left:
                if (Constants.ROAD_HEIGHT - Constants.CARSIZE - 2 < car.getX()
                        && Constants.ROAD_HEIGHT - Constants.CARSIZE >= car.getX()) {
                    return true;
                }
                break;
            case Right:
                if (Constants.WINDOW_HEIGHT - Constants.ROAD_HEIGHT + 2 > car.getX()
                        && Constants.WINDOW_HEIGHT - Constants.ROAD_HEIGHT <= car.getX()) {
                    return true;
                }
                break;
            default:
        }
        return false;
    }

    public void createCar(KeyCode code) {
        Car car = null;
        switch (code) {
            case UP:
                if (carNbr[0] <= Constants.MAXCARS) {
                    car = new Car(Direction.Up, getTowards());
                    carNbr[0]++;
                }
                break;
            case DOWN:
                if (carNbr[1] <= Constants.MAXCARS) {
                    car = new Car(Direction.Down, getTowards());
                    carNbr[1]++;
                }
                break;
            case LEFT:
                if (carNbr[2] <= Constants.MAXCARS) {
                    car = new Car(Direction.Left, getTowards());
                    carNbr[2]++;
                }
                break;
            case RIGHT:
                if (carNbr[3] <= Constants.MAXCARS) {
                    car = new Car(direction.Right, getTowards());
                    carNbr[3]++;
                }
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