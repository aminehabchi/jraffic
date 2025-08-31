package jraffic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jraffic.RoadController;
import jraffic.helpers.Constants;
import jraffic.helpers.Direction;
import jraffic.helpers.Towards;

public class Traffic {

    public List<Car> carsT;
    public List<Car> carsD;
    public List<Car> carsL;
    public List<Car> carsR;
    public List<Car> carsInside;
    public List<Car> carsOutside;
    private final Pane roadPane;
    private final TrafficHelper helper;
    private int id;
    private final long[] lastCarTime = new long[4]; // [UP, DOWN, LEFT, RIGHT]
    private final RoadController roadController;

    public Traffic(Pane roadPane, RoadController roadController) {
        this.roadController = roadController;
        this.roadPane = roadPane;
        id = 0;
        this.id = 0;
        this.carsT = new ArrayList<>();
        this.carsD = new ArrayList<>();
        this.carsL = new ArrayList<>();
        this.carsR = new ArrayList<>();
        this.carsInside = new ArrayList<>();
        this.carsOutside = new ArrayList<>();
        this.helper = new TrafficHelper();
    }

    public void moveCars() {
        helper.moveCarList(carsT, true); // true for vertical movement
        helper.moveCarList(carsD, true);
        helper.moveCarList(carsL, false); // false for horizontal movement
        helper.moveCarList(carsR, false);

        // Move cars outside intersection
        for (Car c : carsOutside) {
            c.move();
        }

        // Remove cars that are completely off screen
        for (int i = carsOutside.size() - 1; i >= 0; i--) {
            Car car = carsOutside.get(i);
            if (isCarOutside(0, 0, 800, (int) car.getX(), (int) car.getY(), Constants.CARSIZE)) {
                roadPane.getChildren().remove(car.getShape());
                carsOutside.remove(i);
            }
        }

        // Move cars inside intersection
        for (Car c : carsInside) {
            c.move();
        }

        // Move cars from inside to outside when they exit intersection
        for (int i = carsInside.size() - 1; i >= 0; i--) {
            Car car = carsInside.get(i);
            if (isCarOutside(350, 350, 100, (int) car.getX(), (int) car.getY(), Constants.CARSIZE)) {
                carsOutside.add(car);
                carsInside.remove(i);
            }
        }

        ///

        algo();
        System.err.println("Inside: " + carsInside.size() + " Outside: " + carsOutside.size()
                + " T: " + carsT.size() + " D: " + carsD.size()
                + " R: " + carsR.size() + " L: " + carsL.size());
    }

    public boolean isCarOutside(int x, int y, int rectSize, int carX, int carY, int carSize) {
        int rectLeft = x;
        int rectTop = y;
        int rectRight = x + rectSize;
        int rectBottom = y + rectSize;

        int carLeft = carX;
        int carTop = carY;
        int carRight = carX + carSize;
        int carBottom = carY + carSize;

        return (carRight < rectLeft
                || carLeft > rectRight
                || carBottom < rectTop
                || carTop > rectBottom);
    }

    private void algo() {
        if (!carsInside.isEmpty()) {
            return;
        }

        List<Car> stoppedCars = helper.getStoppedCars(carsD, carsL, carsR, carsT);
        if (stoppedCars == null || stoppedCars.isEmpty()) {
            return;
        }

        int index = helper.getIndexOfFirstCarInQueue(stoppedCars);
        if (index == -1) {
            turnOffAllLights();
            return; // No valid cars found
        }

        Car firstCar = stoppedCars.get(index);
        if (firstCar != null) {
            carsInside.add(firstCar);
            removeCarFromLane(firstCar);
        }

        // Check which other cars can move with the first car
        for (int i = 0; i < stoppedCars.size(); i++) {
            if (i != index) {
                Car otherCar = stoppedCars.get(i);
                if (otherCar != null && checkIfCanMove(firstCar, otherCar)) {
                    carsInside.add(otherCar);
                    removeCarFromLane(otherCar);
                }
            }
        }

        updateLights();
    }

    private final Color DARK_RED = Color.web("#660000");
    private final Color RED = Color.web("#ff4444");
    private final Color DARK_GREEN = Color.web("#006600");
    private final Color GREEN = Color.web("#44ff44");

    private void turnOffAllLights() {
        roadController.upRed.setFill(DARK_RED);
        roadController.upGreen.setFill(DARK_GREEN);

        roadController.downRed.setFill(DARK_RED);
        roadController.downGreen.setFill(DARK_GREEN);

        roadController.leftRed.setFill(DARK_RED);
        roadController.leftGreen.setFill(DARK_GREEN);

        roadController.rightRed.setFill(DARK_RED);
        roadController.rightGreen.setFill(DARK_GREEN);
    }

    private void updateLights() {
        turnOffAllLights();

        // Set red lights for stopped cars
        if (!carsT.isEmpty() && helper.isMustStop(carsT.get(0))) {
            roadController.upRed.setFill(RED);
        }
        if (!carsD.isEmpty() && helper.isMustStop(carsD.get(0))) {
            roadController.downRed.setFill(RED);
        }
        if (!carsL.isEmpty() && helper.isMustStop(carsL.get(0))) {
            roadController.leftRed.setFill(RED);
        }
        if (!carsR.isEmpty() && helper.isMustStop(carsR.get(0))) {
            roadController.rightRed.setFill(RED);
        }

        // Set green lights for cars currently in intersection
        for (Car c : carsInside) {
            switch (c.getDirection()) {
                case Up:
                    roadController.upGreen.setFill(GREEN);
                    break;
                case Down:
                    roadController.downGreen.setFill(GREEN);
                    break;
                case Left:
                    roadController.leftGreen.setFill(GREEN);
                    break;
                case Right:
                    roadController.rightGreen.setFill(GREEN);
                    break;
                default:
                    break;
            }
        }
    }

    private void removeCarFromLane(Car car) {
        switch (car.getDirection()) {
            case Up:
                if (!carsT.isEmpty() && carsT.get(0) == car) {
                    carsT.remove(0);
                }
                break;
            case Down:
                if (!carsD.isEmpty() && carsD.get(0) == car) {
                    carsD.remove(0);
                }
                break;
            case Left:
                if (!carsL.isEmpty() && carsL.get(0) == car) {
                    carsL.remove(0);
                }
                break;
            case Right:
                if (!carsR.isEmpty() && carsR.get(0) == car) {
                    carsR.remove(0);
                }
                break;
            default:
                break;
        }
    }

    public boolean checkIfCanMove(Car car1, Car car2) {
        return (car1.getDirection() == Direction.Down && car2.getDirection() == Direction.Up)
                || (car1.getDirection() == Direction.Up && car2.getDirection() == Direction.Down)
                || (car1.getDirection() == Direction.Left && car2.getDirection() == Direction.Right)
                || (car1.getDirection() == Direction.Right && car2.getDirection() == Direction.Left);
    }

    public void createCar(KeyCode code) {
        // 1. Get direction from key
        Direction direction = helper.getDirectionFromKeyCode(code);
        if (direction == null) {
            return;
        }

        // 2. Get the list for this direction
        List<Car> targetList = getCarListByDirection(direction);

        // 3. Direction index for lastCarTime array
        int directionIndex = getDirectionIndex(direction);

        // 4. Check max cars
        if (targetList.size() > Constants.MAXCARS) {
            return;
        }

        // 5. Check time since last car
        long now = System.currentTimeMillis();
        if (now - lastCarTime[directionIndex] < Constants.DELAY) {
            return;
        }

        // 6. Create car
        Car car = new Car(direction, helper.getRandomTowards(), id++);
        targetList.add(car);
        roadPane.getChildren().add(car.getShape());

        // 7. Update last spawn time
        lastCarTime[directionIndex] = now;
    }

    private int getDirectionIndex(Direction code) {
        switch (code) {
            case Up:
                return 0;
            case Down:
                return 1;
            case Left:
                return 2;
            case Right:
                return 3;
            default:
                return -1;
        }
    }

    private List<Car> getCarListByDirection(Direction direction) {
        if (direction == null) {
            return null;
        }

        switch (direction) {
            case Up:
                return carsT;
            case Down:
                return carsD;
            case Left:
                return carsL;
            case Right:
                return carsR;
            default:
                return null;
        }
    }
}

class TrafficHelper {

    private final Random random;

    public TrafficHelper() {
        this.random = new Random();
    }

    public int getIndexOfFirstCarInQueue(List<Car> cars) {
        int minIndex = -1;
        int minId = Integer.MAX_VALUE;

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (car != null) {
                int id = car.getId();
                if (id < minId) {
                    minId = id;
                    minIndex = i;
                }
            }
        }
        return minIndex;
    }

    public List<Car> getStoppedCars(List<Car> cars1, List<Car> cars2, List<Car> cars3, List<Car> cars4) {
        List<Car> stoppedCars = new ArrayList<>();

        stoppedCars.add(getFirstStoppedCar(cars1));
        stoppedCars.add(getFirstStoppedCar(cars2));
        stoppedCars.add(getFirstStoppedCar(cars3));
        stoppedCars.add(getFirstStoppedCar(cars4));

        return stoppedCars;
    }

    private Car getFirstStoppedCar(List<Car> cars) {
        if (cars.isEmpty()) {
            return null;
        }

        Car firstCar = cars.get(0);
        return isMustStop(firstCar) ? firstCar : null;
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
                double distance = isVertical
                        ? Math.abs(previousCar.getY() - car.getY())
                        : Math.abs(previousCar.getX() - car.getX());

                if (distance >= Constants.SAFEDISTANCE) {
                    car.move();
                }
            }
        }
    }

    public boolean isMustStop(Car car) {
        if (car == null) {
            return false;
        }

        switch (car.getDirection()) {
            case Up:
                return car.getY() + Constants.SPEED >= Constants.ROAD_HEIGHT - Constants.CARSIZE;
            case Down:
                return car.getY() - Constants.SPEED <= Constants.WINDOW_HEIGHT - Constants.ROAD_HEIGHT;
            case Left:
                return car.getX() + Constants.SPEED >= Constants.ROAD_HEIGHT - Constants.CARSIZE;
            case Right:
                return car.getX() - Constants.SPEED <= Constants.WINDOW_HEIGHT - Constants.ROAD_HEIGHT;
            default:
                return false;
        }
    }

    public Direction getDirectionFromKeyCode(KeyCode code) {
        switch (code) {
            case UP:
                return Direction.Down;
            case DOWN:
                return Direction.Up;
            case LEFT:
                return Direction.Right;
            case RIGHT:
                return Direction.Left;
            case R:
                return randomDirection();
            case ESCAPE:
                System.exit(0);
                return null;
            default:
                return null;
        }
    }

    private Direction randomDirection() {
        Direction[] directions = Direction.values();
        int index = new java.util.Random().nextInt(directions.length);
        return directions[index];
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
