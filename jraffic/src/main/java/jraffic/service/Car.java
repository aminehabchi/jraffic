package jraffic.service;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jraffic.helpers.Constants;
import jraffic.helpers.Direction;
import jraffic.helpers.Towards;

public class Car {
    private int x;
    private int y;
    private Direction direction;
    private Towards toward = Towards.Left;
    private Color color;
    private final int width = 50;
    private final int height = 50;
    private Rectangle shape;
    public boolean isWait = false;
    public boolean isInIntersectoin = false;

    public Car(Direction d, Towards t) {
        direction = d;
        toward = t;
        switch (t) {
            case Forward:
                color = Constants.ORANGE;
                break;
            case Left:
                color = Constants.BLUE;
                break;
            case Right:
                color = Constants.YELLOW;
                break;
            default:
                throw new AssertionError();
        }
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

    public Direction getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getShape() {
        // singlton desgin pattenrs
        if (shape == null) {
            shape = new Rectangle(width, height, color);
            shape.setX(x);
            shape.setY(y);
        }
        return shape;
    }

    public void move() {
     
        switch (direction) {
            case Up:
                if (toward.equals(toward.Right) && y >= Constants.ROAD_HEIGHT) {
                    x -= Constants.SPEED;
                } else if (toward.equals(toward.Left) && y >= Constants.ROAD_HEIGHT + height) {
                    x += Constants.SPEED;
                } else {
                    y += Constants.SPEED;
                }
                break;
            case Down:
                if (toward.equals(toward.Right)
                        && y + width <= Constants.WINDOW_HEIGHT - Constants.ROAD_HEIGHT) {
                    x += Constants.SPEED;
                } else if (toward.equals(toward.Left) && y <= Constants.ROAD_HEIGHT) {
                    x -= Constants.SPEED;
                } else {
                    y -= Constants.SPEED;
                }
                break;
            case Left:
                if (toward.equals(toward.Right) && x >= Constants.ROAD_HEIGHT) {
                    y += Constants.SPEED;
                } else if (toward.equals(toward.Left) && x >= Constants.ROAD_HEIGHT + height) {
                    y -= Constants.SPEED;
                } else {
                    x += Constants.SPEED;
                }
                break;
            case Right:
                if (toward.equals(toward.Right)
                        && x + width <= Constants.WINDOW_HEIGHT - Constants.ROAD_HEIGHT) {
                    y -= Constants.SPEED;
                } else if (toward.equals(toward.Left)
                        && x <= Constants.ROAD_HEIGHT) {
                    y += Constants.SPEED;
                } else {
                    x -= Constants.SPEED;
                }
                break;
            default:
        }
        updateShape();
    }

   

    private void updateShape() {
        shape.setX(x);
        shape.setY(y);
    }

}