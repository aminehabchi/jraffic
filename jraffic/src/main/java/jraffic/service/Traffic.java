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
    public List<Car> carsOutside;
    private Pane roadPane;
    private TrafficHelper helper;
    private int id;

    public Traffic(Pane roadPane) {
        this.roadPane = roadPane;
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
            if (isCarOutside(0, 0, 800, (int)car.getX(), (int)car.getY(), Constants.CARSIZE)) {
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
            if (isCarOutside(350, 350, 100, (int)car.getX(), (int)car.getY(), Constants.CARSIZE)) {
                carsOutside.add(car);
                carsInside.remove(i);
            }
        }
        
        algo();
        System.err.println("Inside: " + carsInside.size() + " Outside: " + carsOutside.size() + 
                          " T: " + carsT.size() + " D: " + carsD.size() + 
                          " R: " + carsR.size() + " L: " + carsL.size());
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

        // Car is completely outside if it does NOT overlap at all
        return (carRight < rectLeft || // completely left
                carLeft > rectRight || // completely right
                carBottom < rectTop || // completely above
                carTop > rectBottom); // completely below
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
        if (car2.getToward() == Towards.Right) {
            return true;
        }

        if ((car1.getDirection() == Direction.Down && car2.getDirection() == Direction.Up) ||
            (car1.getDirection() == Direction.Up && car2.getDirection() == Direction.Down) ||
            (car1.getDirection() == Direction.Left && car2.getDirection() == Direction.Right) ||
            (car1.getDirection() == Direction.Right && car2.getDirection() == Direction.Left)) {
            return true;
        }
        
        return false;
    }

    public void createCar(KeyCode code) {
        Direction direction = helper.getDirectionFromKeyCode(code);
        List<Car> targetList = getCarListByDirection(direction);
        
        if (direction != null && targetList != null && targetList.size() <= Constants.MAXCARS) {
            Car car = new Car(direction, helper.getRandomTowards(), id++);
            targetList.add(car);
            roadPane.getChildren().add(car.getShape());
        }
    }
    
    private List<Car> getCarListByDirection(Direction direction) {
        if (direction == null) return null;
        
        switch (direction) {
            case Up: return carsT;
            case Down: return carsD;
            case Left: return carsL;
            case Right: return carsR;
            default: return null;
        }
    }
}

class TrafficHelper {
    private Random random;

    public TrafficHelper() {
        this.random = new Random();
    }

    /**
     * Gets the index of the car that arrived first (lowest ID) in the intersection
     * @param cars List of cars at the intersection
     * @return Index of the first car, or -1 if no valid cars
     */
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

    /**
     * Gets cars that are stopped at the intersection from all directions
     * @param cars1 Cars from first direction
     * @param cars2 Cars from second direction
     * @param cars3 Cars from third direction
     * @param cars4 Cars from fourth direction
     * @return List of stopped cars (may contain nulls for empty lanes)
     */
    public List<Car> getStoppedCars(List<Car> cars1, List<Car> cars2, List<Car> cars3, List<Car> cars4) {
        List<Car> stoppedCars = new ArrayList<>();

        stoppedCars.add(getFirstStoppedCar(cars1));
        stoppedCars.add(getFirstStoppedCar(cars2));
        stoppedCars.add(getFirstStoppedCar(cars3));
        stoppedCars.add(getFirstStoppedCar(cars4));

        return stoppedCars;
    }

    /**
     * Gets the first stopped car from a lane, or null if no stopped cars
     * @param cars List of cars in a lane
     * @return First stopped car or null
     */
    private Car getFirstStoppedCar(List<Car> cars) {
        if (cars.isEmpty()) {
            return null;
        }
        
        Car firstCar = cars.get(0);
        return isMustStop(firstCar) ? firstCar : null;
    }

    /**
     * Moves cars in a list maintaining safe distance
     * @param cars List of cars to move
     * @param isVertical true for vertical movement (Y axis), false for horizontal (X axis)
     */
    public void moveCarList(List<Car> cars, boolean isVertical) {
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (!car.isPass && isMustStop(car)) {
                continue;
            }
            if (i == 0) {
                car.move();
            } else {
                Car previousCar = cars.get(i - 1);
                double distance = isVertical ? 
                    Math.abs(previousCar.getY() - car.getY()) : 
                    Math.abs(previousCar.getX() - car.getX());

                if (distance >= Constants.SAFEDISTANCE) {
                    car.move();
                }
            }
        }
    }

    /**
     * Determines if a car must stop based on traffic rules and intersection boundaries
     * @param car The car to check
     * @return true if car must stop, false otherwise
     */
    private boolean isMustStop(Car car) {
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

    /**
     * Converts KeyCode to Direction enum
     * @param code KeyCode from user input
     * @return Corresponding Direction or null if invalid
     */
    public Direction getDirectionFromKeyCode(KeyCode code) {
        switch (code) {
            case UP: return Direction.Up;
            case DOWN: return Direction.Down;
            case LEFT: return Direction.Left;
            case RIGHT: return Direction.Right;
            default: return null;
        }
    }

    /**
     * Generates a random direction for cars to turn at intersection
     * @return Random Towards direction (Forward, Left, or Right)
     */
    public Towards getRandomTowards() {
        int method = random.nextInt(3);
        switch (method) {
            case 0: return Towards.Forward;
            case 1: return Towards.Left;
            case 2: return Towards.Right;
            default: return Towards.Forward;
        }
    }
}