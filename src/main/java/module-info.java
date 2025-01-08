module com.example.javarendu {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jdi;


    opens com.example.javarendu to javafx.fxml;
    exports com.example.javarendu;
}