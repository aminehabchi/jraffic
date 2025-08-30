package jraffic;

import java.util.List;

import javafx.scene.input.KeyCode;

public class Traffic {
    public List<Car> cars;

    public Traffic() {

    }

    public void createCar(KeyCode code) {

        switch (code) {
            case UP:
                System.out.println("Arrow Up pressed");
              
                break;
            case DOWN:
                System.out.println("Arrow Down pressed");
                break;
            case LEFT:
                System.out.println("Arrow Left pressed");
                break;
            case RIGHT:
                System.out.println("Arrow Right pressed");
                break;
            default:
                break;
        }
    }

}