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

    public List<Car> carsT;
    public List<Car> carsD;
    public List<Car> carsL;
    public List<Car> carsR;
    public List<Car> carsInside;
    private Pane roadPane;
    private TrafficHelper helper;
    private int id;

    public Traffic(Pane roadPane) {
        this.roadPane = roadPane;
        id = 0;
        this.carsT = new ArrayList<>();
        this.carsD = new ArrayList<>();
        this.carsL = new ArrayList<>();
        this.carsR = new ArrayList<>();
        this.carsInside = new ArrayList<>();
        this.helper = new TrafficHelper();
    }

    public void moveCars() {
        helper.moveCarList(carsT, true); // true for vertical movement
        helper.moveCarList(carsD, true);
        helper.moveCarList(carsL, false); // false for horizontal movement
        helper.moveCarList(carsR, false);
        algo();
    }

    private void algo() {
        if (!carsInside.isEmpty()) {
            return;
        }

        List<Car> cars = helper.getStopedCars(carsD, carsL, carsR, carsT);
        if (cars == null || cars.isEmpty()) {
            return;
        }
        int index = helper.getIndexOfFirstCarInQueue(cars);
        Car car = cars.get(index);
        car.move();
        carsInside.add(car);
        for (int i = 0; i < cars.size(); i++) {
            if (i != index) {
                Car c = cars.get(index);
                if (checkIfCanMove(car, c)) {
                    c.move();
                    carsInside.add(c);
                }
            }
        }

    }

    public boolean checkIfCanMove(Car car1, Car car2) {
        if (car2.getToward() == Towards.Right) {
            return true;
        }

        if (car1.getDirection() == Direction.Down && car2.getDirection() == Direction.Up) {
            return true;
        }
        if (car2.getDirection() == Direction.Down && car1.getDirection() == Direction.Up) {
            return true;
        }
        return false;
    }

    public void removeCarsofOutIntersection(){

    }

    public void createCar(KeyCode code) {
        Car car = null;
        List<Car> targetList = null;
        Direction direction = null;

        switch (code) {
            case UP:
                if (carsT.size() <= Constants.MAXCARS) {
                    direction = Direction.Up;
                    targetList = carsT;
                }
                break;
            case DOWN:
                if (carsD.size() <= Constants.MAXCARS) {
                    direction = Direction.Down;
                    targetList = carsD;
                }
                break;
            case LEFT:
                if (carsL.size() <= Constants.MAXCARS) {
                    direction = Direction.Left;
                    targetList = carsL;
                }
                break;
            case RIGHT:
                if (carsR.size() <= Constants.MAXCARS) {
                    direction = Direction.Right;
                    targetList = carsR;
                }
                break;
            default:
                return;
        }

        if (direction != null && targetList != null) {
            car = new Car(direction, helper.getRandomTowards(), id);
            id++;
            targetList.add(car);
            roadPane.getChildren().add(car.getShape());
        }
    }
}

class TrafficHelper {
    private Random random;

    public TrafficHelper() {
        this.random = new Random();
    }

    public int getIndexOfFirstCarInQueue(List<Car> cars) {
        int minIndex = -1;
        int minId = Integer.MAX_VALUE;

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            int id = car.getId();
            if (id < minId) {
                minId = id;
                minIndex = i;
            }
        }
        return minIndex;
    }

    public List<Car> getStopedCars(List<Car> cars1, List<Car> cars2, List<Car> cars3, List<Car> cars4) {
        List<Car> cars = new ArrayList<>();

        if (!cars1.isEmpty()) {
            if (isMustStop(cars1.get(0))) {
                cars.add(cars1.get(0));
            }
        }
        if (!cars2.isEmpty()) {
            if (isMustStop(cars2.get(0))) {
                cars.add(cars2.get(0));
            }
        }
        if (!cars3.isEmpty()) {
            if (isMustStop(cars3.get(0))) {
                cars.add(cars3.get(0));
            }
        }
        if (!cars4.isEmpty()) {
            if (isMustStop(cars4.get(0))) {
                cars.add(cars4.get(0));
            }
        }

        return cars;
    }

    public void moveCarList(List<Car> cars, boolean isVertical) {
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (isMustStop(car)) {
                continue;
            }
            if (i == 0) {
                car.move();
            } else {
                Car previousCar = cars.get(i - 1);
                double distance = isVertical ? Math.abs(previousCar.getY() - car.getY())
                        : Math.abs(previousCar.getX() - car.getX());

                if (distance >= Constants.SAFEDISTANCE) {
                    car.move();
                }
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

    public List<Car> whoMustGoWithFirst(List<Car> cars) {

        return cars;
    }

    public Towards getRandomTowards() {
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
}