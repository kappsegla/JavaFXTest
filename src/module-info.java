module javafxtest {
    requires javafx.controls;
    requires javafx.fxml;
    opens sample to javafx.graphics, javafx.fxml;
}