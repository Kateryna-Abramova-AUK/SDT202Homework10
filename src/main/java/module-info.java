module com.example.week13 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.week13 to javafx.fxml;
    exports com.example.week13;
}