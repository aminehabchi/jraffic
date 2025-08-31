package jraffic.service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jraffic.helpers.Constants;
import jraffic.helpers.Direction;
import jraffic.helpers.Towards;

public class Car {
    private int id;
    private int x;
    private int y;
    private Direction direction;
    private Towards toward;
    private final int width = 50;
    private final int height = 50;
    public boolean isPass = false;

    private ImageView shape;
    private String color; // "blue", "yellow", "green"

    public boolean isWait = false;
    public boolean isInIntersectoin = false;

    public Car(Direction d, Towards t, int id) {
        direction = d;
        toward = t;
        this.id = id;
        // pick car color based on "towards" - Updated mapping
        switch (t) {
            case Forward:
                color = "blue"; // Blue cars go forward
                break;
            case Left:
                color = "green"; // Green cars turn left
                break;
            case Right:
                color = "yellow"; // Yellow cars turn right
                break;
            default:
                color = "blue";
        }

        // starting position
        switch (d) {
            case Up:
                x = Constants.START_TOP[0];
                y = Constants.START_TOP[1];
                break;
            case Down:
                x = Constants.START_BOTTOM[0];
                y = Constants.START_BOTTOM[1];
                break;
            case Left:
                x = Constants.START_LEFT[0];
                y = Constants.START_LEFT[1];
                break;
            case Right:
                x = Constants.START_RIGHT[0];
                y = Constants.START_RIGHT[1];
                break;
            default:
        }
    }

    public int getId() {
        return id;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ImageView getShape() {
        if (shape == null) {
            shape = new ImageView();
            shape.setFitWidth(width);
            shape.setFitHeight(height);
            updateImage(); // load initial image
            shape.setX(x);
            shape.setY(y);
        }
        return shape;
    }

    public void move() {
        // same movement logic
        switch (direction) {
            case Up:
                if (toward.equals(Towards.Right) && y >= Constants.ROAD_HEIGHT) {
                    x -= Constants.SPEED;
                    direction = Direction.Right;
                    toward = Towards.Forward;
                } else if (toward.equals(Towards.Left) && y >= Constants.ROAD_HEIGHT + height) {
                    x += Constants.SPEED;
                    direction = Direction.Left;
                    toward = Towards.Forward;
                } else {
                    y += Constants.SPEED;
                }
                break;
            case Down:
                if (toward.equals(Towards.Right)
                        && y + width <= Constants.WINDOW_HEIGHT - Constants.ROAD_HEIGHT) {
                    x += Constants.SPEED;
                    direction = Direction.Left;
                    toward = Towards.Forward;
                } else if (toward.equals(Towards.Left) && y <= Constants.ROAD_HEIGHT) {
                    x -= Constants.SPEED;
                    direction = Direction.Right;
                    toward = Towards.Forward;
                } else {
                    y -= Constants.SPEED;
                }
                break;
            case Left:
                if (toward.equals(Towards.Right) && x >= Constants.ROAD_HEIGHT) {
                    y += Constants.SPEED;
                    direction = Direction.Up;
                    toward = Towards.Forward;
                } else if (toward.equals(Towards.Left) && x >= Constants.ROAD_HEIGHT + height) {
                    y -= Constants.SPEED;
                    direction = Direction.Down;
                    toward = Towards.Forward;
                } else {
                    x += Constants.SPEED;
                }
                break;
            case Right:
                if (toward.equals(Towards.Right)
                        && x + width <= Constants.WINDOW_HEIGHT - Constants.ROAD_HEIGHT) {
                    y -= Constants.SPEED;
                    direction = Direction.Down;
                    toward = Towards.Forward;
                } else if (toward.equals(Towards.Left)
                        && x <= Constants.ROAD_HEIGHT) {
                    y += Constants.SPEED;
                    direction = Direction.Up;
                    toward = Towards.Forward;
                } else {
                    x -= Constants.SPEED;
                }
                break;
            default:
        }
        updateShape();
        updateImage(); // refresh image when direction changes
    }

    private void updateShape() {
        shape.setX(x);
        shape.setY(y);
    }

    private void updateImage() {
        int dirNum;
        switch (direction) {
            case Up:
                dirNum = 4;
                break;
            case Right:
                dirNum = 1;
                break;
            case Down:
                dirNum = 2;
                break;
            case Left:
                dirNum = 3;
                break;
            default:
                dirNum = 1;
        }

        String filename = String.format("/jraffic/car_24px_%s_%d.png", color, dirNum);
        Image carImg = new Image(getClass().getResource(filename).toString());
        shape.setImage(carImg);
    }
}