package jraffic;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("road.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 800);
            
            // Set application properties
            stage.setTitle("Traffic Intersection Simulator");
            stage.setScene(scene);
            stage.setResizable(false); // Keep fixed size for consistent intersection view
            stage.centerOnScreen();
            
            // Show the application
            stage.show();
            
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}