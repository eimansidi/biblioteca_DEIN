module com.eiman.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires net.sf.jasperreports.core;

    opens com.eiman.biblioteca.controllers to javafx.fxml;
    opens com.eiman.biblioteca.models to javafx.base;
    exports com.eiman.biblioteca;
}