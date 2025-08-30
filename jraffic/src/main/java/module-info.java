module jraffic {
    requires javafx.controls;
    requires javafx.fxml;

    opens jraffic to javafx.fxml;
    exports jraffic;
}
