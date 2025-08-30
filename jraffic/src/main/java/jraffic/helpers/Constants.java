package jraffic.helpers;

import javafx.scene.paint.Color;

public class Constants {

    // Window size
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 800;

    // Road dimensions
    public static final int ROAD_WIDTH = 100;
    public static final int ROAD_HEIGHT = 350;

    // Traffic speed (pixels per update)
    public static final int SPEED = 2;

    // Starting positions for cars (x, y)
    // Example: startT = top, startB = bottom, startL = left, startR = right
    public static final int[] START_TOP = { 400 - 50, 0 }; // x = 400, y = 0
    public static final int[] START_BOTTOM = { 400, 800 - 50 }; // x = 400, y = 800
    public static final int[] START_LEFT = { 0, 400 }; // x = 0, y = 400
    public static final int[] START_RIGHT = { 800 - 50, 400 - 50 }; // x = 800, y = 400

    // Traffic light colors
    public static final Color RED = Color.RED;
    public static final Color GREEN = Color.GREEN;

    // Cars Colors
    public static final Color ORANGE = Color.rgb(255, 140, 0);
    public static final Color BLUE = Color.rgb(30, 144, 255);
    public static final Color YELLOW = Color.web("#fffb00ff");

}
